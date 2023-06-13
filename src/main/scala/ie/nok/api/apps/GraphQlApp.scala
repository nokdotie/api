package ie.nok.api.apps

import caliban.schema.ArgBuilder.auto._
import caliban.schema.Schema.auto._
import caliban.{graphQL, RootResolver}
import caliban.ZHttpAdapter
import ie.nok.adverts.stores.AdvertStore
import ie.nok.api.graphql.adverts.{
  AdvertsArgs,
  AdvertResolver,
  AdvertConnection
}
import sttp.tapir.json.zio._
import zio.ZIO
import zio.http._
import zio.http.model.Method

object GraphQlApp {

  case class Queries(
      adverts: AdvertsArgs => ZIO[
        AdvertStore,
        Throwable,
        AdvertConnection
      ]
  )

  val api = graphQL[AdvertStore, Queries, Unit, Unit](
    RootResolver(Queries(AdvertResolver.adverts))
  )

  val http: Http[
    AdvertStore,
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
