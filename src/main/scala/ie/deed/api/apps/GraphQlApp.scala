package ie.deed.api.apps

import caliban.schema.ArgBuilder.auto._
import caliban.schema.Schema.auto._
import caliban.{graphQL, RootResolver}
import caliban.ZHttpAdapter
import sttp.tapir.json.zio._
import zio.ZIO
import zio.http._
import zio.http.model.Method

object GraphQlApp {

  case class Queries()

  val api = graphQL(
    RootResolver(Queries())
  )

  val http: Http[
    Any,
    Throwable,
    Request,
    Response
  ] =
    Http.collectHandler[Request] {
      case Method.GET -> !! / "graphiql" =>
        Http.fromResource("graphiql.html").toHandler(Handler.notFound)
      case Method.POST -> !! / "graphql" =>
        Handler
          .fromZIO(api.interpreter)
          .map(ZHttpAdapter.makeHttpService(_))
          .flatMap(_.toHandler(Handler.notFound))

    }
}
