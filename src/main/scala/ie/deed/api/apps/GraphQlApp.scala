package ie.deed.api.apps

import caliban.schema.ArgBuilder.auto._
import caliban.schema.Schema.auto._
import caliban.{graphQL, RootResolver}
import caliban.ZHttpAdapter
import sttp.tapir.json.zio._
import ie.deed.api.apikeys.graphql._
import ie.deed.api.credits.graphql._
import ie.deed.api.requests.graphql.{Request => _, _}
import ie.deed.api.users.graphql._
import ie.deed.api.apikeys.stores.ApiKeyStore
import ie.deed.api.credits.stores.CreditStore
import zio.ZIO
import zio.http._
import zio.http.model.Method

object GraphQlApp {

  case class Queries(
      me: () => User,
      apiKeys: ApiKeysArgs => ZIO[ApiKeyStore, Nothing, ApiKeyConnection],
      credits: CreditsArgs => ZIO[CreditStore, Nothing, CreditConnection],
      requests: RequestsArgs => RequestConnection
  )

  case class Mutations(
      createApiKey: CreateApiKeyArgs => ZIO[ApiKeyStore, Nothing, ApiKey],
      deleteApiKey: DeleteApiKeyArgs => ZIO[ApiKeyStore, Nothing, Unit],
      buyCredit: PurchaseCreditArgs => ZIO[CreditStore, Nothing, Credit]
  )

  val api = graphQL[ApiKeyStore with CreditStore, Queries, Mutations, Unit](
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
        CreditResolver.purchaseCredit
      )
    )
  )

  val http: Http[ApiKeyStore with CreditStore, Throwable, Request, Response] =
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
