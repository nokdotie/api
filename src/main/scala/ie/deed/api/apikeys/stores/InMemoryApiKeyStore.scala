package ie.deed.api.apikeys.stores

import ie.deed.api.apikeys.ApiKey
import ie.deed.api.users.UserIdentifier
import ie.deed.api.utils.Base64Uuid
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import scala.collection.mutable.{SortedSet => MutableSortedSet}
import zio.ZLayer

object InMemoryApiKeyStore extends ApiKeyStore {
  val live: ZLayer[Any, Nothing, ApiKeyStore] = ZLayer.succeed(this)

  private val store: MutableSortedSet[ApiKey] =
    MutableSortedSet.empty(Ordering.by { -_.createdAt.getEpochSecond() })

  def create(userIdentifier: UserIdentifier, description: String): ApiKey =
    ApiKey(Base64Uuid.random(), description, Instant.now, userIdentifier)
      .tap { store.add }

  def delete(userIdentifier: UserIdentifier, key: String): Unit =
    store
      .find { apiKey =>
        apiKey.userIdentifier == userIdentifier && apiKey.id.value == key
      }
      .foreach { store.remove }

  def getPage(
      userIdentifier: UserIdentifier,
      limit: Int,
      createdAtBefore: Option[Instant]
  ): List[ApiKey] =
    store
      .filter { apiKey =>
        apiKey.userIdentifier == userIdentifier &&
        apiKey.createdAt.isBefore(createdAtBefore.getOrElse(Instant.MAX))
      }
      .take(limit)
      .toList

}
