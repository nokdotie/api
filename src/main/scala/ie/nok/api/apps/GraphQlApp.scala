package ie.nok.api.apps

import caliban.{graphQL, RootResolver, ZHttpAdapter}
import caliban.schema.{Schema, ArgBuilder}
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
  object Queries {
    given Schema[AdvertStore, Queries] = Schema.gen
  }

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
