package ie.nok.api

import zio._
import zio.http._
import zio.http.ServerConfig.default.address.getPort
import ie.nok.adverts.stores.{AdvertStore, AdvertStoreImpl}
import ie.nok.api.apps._
import ie.nok.api.apps.proxies._
import ie.nok.file.ZFileServiceImpl
import ie.nok.gcp.storage.ZStorageServiceImpl
import scala.util.chaining.scalaUtilChainingOps
import ie.nok.adverts.Advert

object Main extends ZIOAppDefault {

  private val app: App[AdvertStore with Client] = (
    BuildingEnergyRatingProxy.http ++
      PropertyPriceRegisterProxy.http ++
      GraphQlApp.http ++
      HealthApp.http ++
      SwaggerApp.http
  ).pipe {
    _
      @@ HttpAppMiddleware.debug
      @@ HttpAppMiddleware.cors()
  }.withDefaultErrorResponse

  override val run: ZIO[Any with ZIOAppArgs with Scope, Throwable, Unit] = for {
    _ <- Console.printLine(s"Starting server on http://localhost:$getPort")
    server <- Server
      .serve(app)
      .provide(
        Server.default,
        Client.default,
        AdvertStoreImpl.live,
        ZFileServiceImpl.layer[Advert],
        ZStorageServiceImpl.layer
      )
  } yield ()

}
