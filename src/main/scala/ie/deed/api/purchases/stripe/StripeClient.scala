package ie.deed.api.purchases.stripe

import zio.{System, ZLayer, ZIO}
import com.stripe.net.RequestOptions
import com.stripe.model.checkout.Session
import scala.collection.JavaConverters.mapAsScalaMapConverter
import java.time.Instant

trait StripeClient {
  def getPurchase(
      checkoutSessionId: String
  ): ZIO[Any, Throwable, StripePurchase]
}

object StripeClient {
  val layer: ZLayer[Any, SecurityException, StripeClient] =
    ZLayer {
      for {
        secretKey <- System
          .env("ENV")
          .map {
            case Some("production") =>
              "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"
            case _ =>
              "sk_test_51N5RTeJwjcbVxo7fGKESoSWKZThzctFhYKbpeEk8K455JYbjX9AovN5yQfsClyS133H9trdn8Qr53e1wVIUpFpsA00Cp5CitWD"
          }
      } yield StripeClientImpl(secretKey)
    }

  def getPurchase(
      checkoutSessionId: String
  ): ZIO[StripeClient, Throwable, StripePurchase] =
    ZIO.serviceWithZIO[StripeClient](_.getPurchase(checkoutSessionId))
}

class StripeClientImpl(secretKey: String) extends StripeClient {
  private val requestOptions = RequestOptions
    .builder()
    .setApiKey(secretKey)
    .build()

  def getPurchase(
      checkoutSessionId: String
  ): ZIO[Any, Throwable, StripePurchase] = for {
    session <- ZIO.attempt {
      Session.retrieve(checkoutSessionId, requestOptions)
    }
    _ <- ZIO.cond(
      session.getPaymentStatus == "paid",
      (),
      new Exception("Purchase not paid")
    )
    creditAmount = session
      .getMetadata()
      .asScala
      .get("credit amount")
      .flatMap(_.toIntOption)
      .getOrElse(0)
  } yield StripePurchase(
    checkoutSessionId,
    creditAmount
  )

}
