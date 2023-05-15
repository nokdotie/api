package ie.deed.api.purchases.utils

import com.stripe.model.checkout.Session
import com.stripe.net.RequestOptions
import zio.{System, ZIO, ZLayer}

final case class StripeSecretKey(value: String) extends AnyVal

trait StripeWrapper {
  def checkoutSessionRetrieve(
      checkoutSessionId: String
  ): ZIO[Any, Throwable, Session]
}

object StripeWrapper {
  val default: ZLayer[Any, Throwable, StripeWrapper] =
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
          .map { StripeSecretKey(_) }
      } yield StripeWrapperImpl(secretKey)
    }

  def checkoutSessionRetrieve(
      checkoutSessionId: String
  ): ZIO[StripeWrapper, Throwable, Session] =
    ZIO.serviceWithZIO[StripeWrapper](
      _.checkoutSessionRetrieve(checkoutSessionId)
    )
}

class StripeWrapperImpl(secretKey: StripeSecretKey) extends StripeWrapper {
  private val requestOptions = RequestOptions
    .builder()
    .setApiKey(secretKey.value)
    .build()

  override def checkoutSessionRetrieve(
      checkoutSessionId: String
  ): ZIO[Any, Throwable, Session] = {
    ZIO.attemptBlocking { Session.retrieve(checkoutSessionId, requestOptions) }
  }
}
