package ie.deed.api.credits.stores

import ie.deed.api.credits.{Credit, CreditSource}
import ie.deed.api.users.UserIdentifier
import ie.deed.api.utils.Base64Uuid
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import scala.collection.mutable.{SortedSet => MutableSortedSet}
import zio.ZLayer

object InMemoryCreditStore extends CreditStore {
  val live: ZLayer[Any, Nothing, CreditStore] = ZLayer.succeed(this)

  private val store: MutableSortedSet[Credit] =
    MutableSortedSet.empty(Ordering.by { -_.createdAt.getEpochSecond() })

  def create(
      userIdentifier: UserIdentifier,
      amount: Int,
      source: CreditSource
  ): Credit =
    Credit(Base64Uuid.random(), amount, source, Instant.now, userIdentifier)
      .tap { store.add }

  def getPage(
      userIdentifier: UserIdentifier,
      limit: Int,
      createdAtBefore: Option[Instant]
  ): List[Credit] =
    store
      .filter { credit =>
        credit.userIdentifier == userIdentifier &&
        credit.createdAt.isBefore(createdAtBefore.getOrElse(Instant.MAX))
      }
      .take(limit)
      .toList

  def getPageByStripeCheckoutSessionId(
      checkoutSessionId: String,
      limit: Int,
      createdAtBefore: Option[Instant]
  ): List[Credit] =
    store
      .filter { credit =>
        credit.source == CreditSource.StripePurchase(checkoutSessionId) &&
        credit.createdAt.isBefore(createdAtBefore.getOrElse(Instant.MAX))
      }
      .take(limit)
      .toList
}
