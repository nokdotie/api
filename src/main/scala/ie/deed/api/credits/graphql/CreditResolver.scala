package ie.deed.api.credits.graphql

import ie.deed.api.credits.CreditSource
import ie.deed.api.credits.stores.CreditStore
import ie.deed.api.purchases.stripe.StripeClient
import ie.deed.api.utils.authentication.Authed
import ie.deed.api.utils.graphql.{JsonCursor, Pagination}
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO

object CreditResolver {
  def credits(
      args: CreditsArgs
  ): ZIO[Authed with CreditStore, Nothing, CreditConnection] = {
    val limit = Pagination.limit(args)
    val createdAtBefore = Pagination.cursor(args)

    for {
      userIdentifier <- Authed.userIdentifier
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
  ): ZIO[Authed with CreditStore with StripeClient, Throwable, Credit] =
    for {
      userIdentifier <- Authed.userIdentifier
      purchase <- StripeClient.getPurchase(args.stripeCheckoutSessionId)
      existingCredits <- CreditStore.getPageByStripeCheckoutSessionId(
        purchase.checkoutSessionId,
        1,
        None
      )
      _ <- ZIO.cond(
        existingCredits.isEmpty,
        (),
        new Exception("Credit already purchased")
      )
      credit <- CreditStore.create(
        userIdentifier,
        purchase.creditAmount,
        CreditSource.StripePurchase(purchase.checkoutSessionId)
      )
      graphql = Credit.fromInternal(credit)
    } yield graphql
}
