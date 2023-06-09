package ie.nok.api.apps

import zio.http._
import zio.http.model.Method

object SwaggerHttp {

  val http: Http[Any, Throwable, Request, Response] =
    Http.collectHandler[Request] { case Method.GET -> !! / "swagger.yaml" =>
      Http.fromResource("swagger.yaml").toHandler(Handler.notFound)
    }

}
