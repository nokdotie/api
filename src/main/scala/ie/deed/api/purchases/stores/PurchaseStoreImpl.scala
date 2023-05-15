package ie.deed.api.purchases.stores

import com.stripe.model.{LineItem, LineItemCollection}
import ie.deed.api.purchases.{Purchase, PurchaseSource}
import ie.deed.api.purchases.utils.StripeWrapper
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO

class PurchaseStoreImpl(
    stripeWrapper: StripeWrapper
) extends PurchaseStore {

  override def getStripePurchase(
      checkoutSessionId: String
  ): ZIO[Any, Throwable, Purchase] = for {
    session <- stripeWrapper
      .checkoutSessionRetrieve(checkoutSessionId)
      .mapError { e => new Exception(s"Stripe failure: ${e.getMessage()}") }
    _ <- ZIO.cond(
      session.getStatus == "complete",
      (),
      new Exception("Stripe checkout not complete")
    )
    _ <- ZIO.cond(
      session.getPaymentStatus == "paid",
      (),
      new Exception("Stripe checkout payment not paid")
    )
    creditAmount = Option(session.getLineItems)
      .flatMap { lineItems => Option(lineItems.getData) }
      .fold(List.empty[LineItem]) { _.asScala }
      .map { _.getQuantity.toInt }
      .sum
  } yield Purchase(
    PurchaseSource.Stripe(checkoutSessionId),
    creditAmount
  )

}
