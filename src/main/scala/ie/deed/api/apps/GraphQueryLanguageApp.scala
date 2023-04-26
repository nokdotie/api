package ie.deed.api.apps

import zio.ZLayer
import zio.http._
import zio.http.model.Method
import caliban.schema.Schema
import caliban.schema.ArgBuilder.auto._
import caliban.{graphQL, GraphQL, RootResolver}
import caliban.ZHttpAdapter
import sttp.tapir.json.zio._

object GraphQueryLanguageApp {

  case class HelloArgs(name: Option[String])

  object Service {
    def hello(args: HelloArgs) =
      s"Hello, ${args.name.getOrElse("World")}!"
  }

  case class Queries(
      hello: HelloArgs => String
  )

  given Schema[Any, HelloArgs] = Schema.gen
  given Schema[Any, Queries] = Schema.gen

  val api: GraphQL[Any] = graphQL(
    RootResolver(
      Queries(args => Service.hello(args))
    )
  )

  val http: Http[Any, Throwable, Request, Response] =
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
