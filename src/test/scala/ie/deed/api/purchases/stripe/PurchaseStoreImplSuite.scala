package ie.deed.api.purchases.stores

import com.stripe.model.{LineItem, LineItemCollection}
import com.stripe.model.checkout.Session
import ie.deed.api.purchases.{Purchase, PurchaseSource}
import ie.deed.api.purchases.utils.StripeWrapper
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.util.chaining.scalaUtilChainingOps
import zio._
import zio.test.{test, _}
import zio.test.Assertion._

object PurchaseStoreImplSuite extends ZIOSpecDefault {
  def stripeWrapper(result: ZIO[Any, Throwable, Session]) = new StripeWrapper {
    override def checkoutSessionRetrieve(
        checkoutSessionId: String
    ): ZIO[Any, Throwable, Session] = {
      result
    }
  }

  def spec = suite("PurchaseStoreImplSuite")(
    suite("getStripePurchase")(
      test("fails if Stripe fails") {
        val result = ZIO
          .fail(new Throwable("Failed"))
          .pipe { stripeWrapper(_) }
          .pipe { PurchaseStoreImpl(_) }
          .getStripePurchase("ABC")

        assertZIO(result)(nothing)
      } @@ TestAspect.failing[Throwable] {
        case TestFailure.Runtime(Cause.Fail(e, _), _) =>
          e.getMessage == "Stripe failure: Failed"
        case _ => false
      },
      test("fails if Stripe checkout not complete") {
        val result = new Session()
          .pipe { ZIO.succeed(_) }
          .pipe { stripeWrapper(_) }
          .pipe { PurchaseStoreImpl(_) }
          .getStripePurchase("ABC")

        assertZIO(result)(nothing)
      } @@ TestAspect.failing[Throwable] {
        case TestFailure.Runtime(Cause.Fail(e, _), _) =>
          println(e.getMessage)
          e.getMessage == "Stripe checkout not complete"
        case _ => false
      },
      test("fails if Stripe checkout not paid") {
        val result = new Session()
          .tap { _.setStatus("complete") }
          .pipe { ZIO.succeed(_) }
          .pipe { stripeWrapper(_) }
          .pipe { PurchaseStoreImpl(_) }
          .getStripePurchase("ABC")

        assertZIO(result)(nothing)
      } @@ TestAspect.failing[Throwable] {
        case TestFailure.Runtime(Cause.Fail(e, _), _) =>
          println(e.getMessage)
          e.getMessage == "Stripe checkout payment not paid"
        case _ => false
      },
      test("succeed with empty cart") {
        val result = new Session()
          .tap { _.setStatus("complete") }
          .tap { _.setPaymentStatus("paid") }
          .pipe { ZIO.succeed(_) }
          .pipe { stripeWrapper(_) }
          .pipe { PurchaseStoreImpl(_) }
          .getStripePurchase("ABC")

        assertZIO(result)(equalTo(Purchase(PurchaseSource.Stripe("ABC"), 0)))
      },
      test("succeed with summed quantities") {
        val result = new Session()
          .tap { _.setStatus("complete") }
          .tap { _.setPaymentStatus("paid") }
          .tap { session =>
            List(
              LineItem().tap { _.setQuantity(1) },
              LineItem().tap { _.setQuantity(2) }
            )
              .pipe { _.asJava }
              .pipe { data => LineItemCollection().tap { _.setData(data) } }
              .tap { session.setLineItems(_) }
          }
          .pipe { ZIO.succeed(_) }
          .pipe { stripeWrapper(_) }
          .pipe { PurchaseStoreImpl(_) }
          .getStripePurchase("ABC")

        assertZIO(result)(equalTo(Purchase(PurchaseSource.Stripe("ABC"), 3)))
      }
    )
  )
}
