package ie.deed.api

import zio._
import zio.http._
import zio.http.ServerConfig.default.address.getPort
import ie.deed.api.apikeys.stores._
import ie.deed.api.apps._
import ie.deed.api.apps.proxies._
import ie.deed.api.credits.stores._
import ie.deed.api.purchases.stripe._
import ie.deed.api.requests.stores._
import scala.util.chaining.scalaUtilChainingOps

object Main extends ZIOAppDefault {

  private val app: App[
    ApiKeyStore with CreditStore with RequestStore with StripeClient with Client
  ] = (
    BuildingEnergyRatingProxy.http ++
      PropertyPriceRegisterProxy.http ++
      GraphQlApp.http ++
      HealthApp.http ++
      SwaggerHttp.http
  ).pipe {
    _
      @@ HttpAppMiddleware.debug
      @@ HttpAppMiddleware.cors()
  }.withDefaultErrorResponse

  override val run = for {
    _ <- Console.printLine(s"Starting server on http://localhost:$getPort")
    server <- Server
      .serve(app)
      .provide(
        InMemoryApiKeyStore.live,
        InMemoryCreditStore.live,
        InMemoryRequestStore.live,
        StripeClient.layer,
        Server.default,
        Client.default
      )
  } yield ()

}
