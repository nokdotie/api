package ie.deed.api.apps

import zio.ZLayer
import zio.http._
import zio.http.model.Method
import caliban.schema.Schema
import caliban.schema.ArgBuilder.auto._
import caliban.schema.Schema.auto._
import caliban.{graphQL, GraphQL, RootResolver}
import caliban.ZHttpAdapter
import sttp.tapir.json.zio._
import ie.deed.api.apikeys.graphql._
import ie.deed.api.credits.graphql._
import ie.deed.api.requests.graphql.{Request => _, _}
import ie.deed.api.users.graphql._
import caliban.relay.PageInfo

object GraphQlApp {
  case class Queries(
      me: () => User,
      apiKeys: ApiKeysArgs => ApiKeyConnection,
      credits: CreditsArgs => CreditConnection,
      requests: RequestsArgs => RequestConnection
  )

  case class Mutations(
      createApiKey: CreateApiKeyArgs => ApiKey,
      deleteApiKey: DeleteApiKeyArgs => Unit,
      buyCredit: BuyCreditArgs => Credit
  )

  val api: GraphQL[Any] = graphQL(
    RootResolver(
      Queries(
        UserResolver.me,
        ApiKeyResolver.apiKeys,
        CreditResolver.credits,
        RequestResolver.requests
      ),
      Mutations(
        ApiKeyResolver.createApiKey,
        ApiKeyResolver.deleteApiKey,
        CreditResolver.buyCredit
      )
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
