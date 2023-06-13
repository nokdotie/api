package ie.nok.api

import zio._
import zio.http._
import zio.http.ServerConfig.default.address.getPort
import ie.nok.adverts.stores.{AdvertStore, AdvertStoreImpl}
import ie.nok.api.apps._
import ie.nok.api.apps.proxies._
import ie.nok.gcp.storage.Storage
import scala.util.chaining.scalaUtilChainingOps

object Main extends ZIOAppDefault {

  private val app: App[AdvertStore with Client] = (
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
        Server.default,
        Client.default,
        AdvertStoreImpl.live,
        Storage.live,
        Scope.default
      )
  } yield ()

}
