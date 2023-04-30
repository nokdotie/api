package ie.deed.api.credits.graphql

import ie.deed.api.credits.CreditSource
import ie.deed.api.credits.stores.CreditStore
import ie.deed.api.users.UserIdentifier
import ie.deed.api.utils.graphql.{JsonCursor, Pagination}
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO

object CreditResolver {
  // TODO: value should be comming from the JWT in the Authorization header
  val userIdentifier = UserIdentifier("123")

  def credits(
      args: CreditsArgs
  ): ZIO[CreditStore, Nothing, CreditConnection] = {
    val limit = Pagination.limit(args)
    val createdAtBefore = Pagination.cursor(args)

    for {
      store <- CreditStore.getPage(userIdentifier, limit, createdAtBefore)
      graphql = store.map { Credit.fromInternal }
      connection = Pagination.connection(
        CreditConnection.apply,
        _.createdAt.pipe(JsonCursor.apply),
        CreditEdge.apply,
        graphql
      )
    } yield connection
  }

  def purchaseCredit(
      args: PurchaseCreditArgs
  ): ZIO[CreditStore, Nothing, Credit] =
    CreditStore
      .create(userIdentifier, args.amount, CreditSource.Purchase)
      .map { Credit.fromInternal }
}
