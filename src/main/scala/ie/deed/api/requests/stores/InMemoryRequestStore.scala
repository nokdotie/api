package ie.deed.api.requests.stores

import ie.deed.api.requests.Request
import ie.deed.api.users.UserIdentifier
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import scala.collection.mutable.{SortedSet => MutableSortedSet}
import zio.ZLayer

object InMemoryRequestStore extends RequestStore {
  val live: ZLayer[Any, Nothing, RequestStore] = ZLayer.succeed(this)

  private val store: MutableSortedSet[Request] =
    MutableSortedSet.empty(Ordering.by { -_.createdAt.getEpochSecond() })

  def getPage(
      userIdentifier: UserIdentifier,
      limit: Int,
      createdAtBefore: Option[Instant]
  ): List[Request] =
    store
      .filter { credit =>
        credit.userIdentifier == userIdentifier &&
        credit.createdAt.isBefore(createdAtBefore.getOrElse(Instant.MAX))
      }
      .take(limit)
      .toList
}
