package ie.deed.api

import zio._
import zio.http._
import zio.http.ServerConfig.default.address.getPort
import ie.deed.api.proxies.BuildingEnergyRatingProxy
import ie.deed.api.apps.SwaggerHttp
import scala.util.chaining.scalaUtilChainingOps

object Main extends ZIOAppDefault {

  private val app: App[Client] = (
    BuildingEnergyRatingProxy.http ++
      SwaggerHttp.http
  ).pipe {
    _
      @@ HttpAppMiddleware.debug
      @@ HttpAppMiddleware.cors()
  }.withDefaultErrorResponse

  override val run = for {
    _ <- Console.printLine(s"Starting server on http://localhost:$getPort")
    server <- Server.serve(app).provide(Server.default, Client.default)
  } yield ()

}
