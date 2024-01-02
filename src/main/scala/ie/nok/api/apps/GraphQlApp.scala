package ie.nok.api.apps

import caliban.{GraphQL, RootResolver, ZHttpAdapter, graphQL}
import caliban.schema.{ArgBuilder, Schema}
import ie.nok.adverts.stores.AdvertStore
import ie.nok.api.graphql.adverts.{Advert, AdvertArgs, AdvertConnection, AdvertResolver, AdvertsArgs}
import sttp.tapir.json.zio.*
import zio.ZIO
import zio.http.*
import zio.http.model.Method

object GraphQlApp {

  case class Queries(
      adverts: AdvertsArgs => ZIO[
        AdvertStore,
        Throwable,
        AdvertConnection
      ],
      advert: AdvertArgs => ZIO[AdvertStore, Throwable, Option[Advert]]
  )
  object Queries {
    given Schema[AdvertStore, Queries] = Schema.gen
  }

  val api: GraphQL[AdvertStore] = graphQL[AdvertStore, Queries, Unit, Unit](
    RootResolver(Queries(AdvertResolver.adverts, AdvertResolver.advert))
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
