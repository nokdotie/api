package ie.deed.api.apps

import zio.http._
import zio.http.model.Method

object GraphQueryLanguageApp {

  val http: Http[Any, Throwable, Request, Response] =
    Http.collectHandler[Request] {
      case Method.GET -> !! / "graphiql" =>  Http.fromResource("graphiql.html").toHandler(Handler.notFound)
    }
}
