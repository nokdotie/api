package ie.deed.api.purchases.stores

import ie.deed.api.purchases.Purchase
import ie.deed.api.purchases.utils.StripeWrapper
import scala.util.chaining.scalaUtilChainingOps
import zio.{ZIO, ZLayer}

trait PurchaseStore {
  def getStripePurchase(
      stripeCheckoutSessionId: String
  ): ZIO[Any, Throwable, Purchase]
}

object PurchaseStore {
  val default: ZLayer[StripeWrapper, SecurityException, PurchaseStore] =
    ZIO
      .service[StripeWrapper]
      .map { PurchaseStoreImpl(_) }
      .pipe { ZLayer.fromZIO(_) }

  def getStripePurchase(
      stripeCheckoutSessionId: String
  ): ZIO[PurchaseStore, Throwable, Purchase] =
    ZIO.serviceWithZIO(_.getStripePurchase(stripeCheckoutSessionId))
}
