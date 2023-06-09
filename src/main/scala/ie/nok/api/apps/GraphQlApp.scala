package ie.deed.api.apps

import caliban.schema.ArgBuilder.auto._
import caliban.schema.Schema.auto._
import caliban.{graphQL, RootResolver}
import caliban.ZHttpAdapter
import sttp.tapir.json.zio._
import ie.deed.api.apikeys.graphql._
import ie.deed.api.apikeys.stores.ApiKeyStore
import ie.deed.api.credits.graphql._
import ie.deed.api.credits.stores.CreditStore
import ie.deed.api.purchases.stores.PurchaseStore
import ie.deed.api.requests.graphql.{Request => _, _}
import ie.deed.api.requests.stores.RequestStore
import ie.deed.api.users.graphql._
import ie.deed.api.utils.authentication.Authed
import ie.deed.api.utils.authentication.JwtAuthMiddleware.jwtAuth
import zio.ZIO
import zio.http._
import zio.http.model.Method

object GraphQlApp {

  case class Queries(
      me: () => User,
      apiKeys: ApiKeysArgs => ZIO[
        Authed with ApiKeyStore,
        Nothing,
        ApiKeyConnection
      ],
      credits: CreditsArgs => ZIO[
        Authed with CreditStore,
        Nothing,
        CreditConnection
      ],
      requests: RequestsArgs => ZIO[
        Authed with RequestStore,
        Nothing,
        RequestConnection
      ]
  )

  case class Mutations(
      createApiKey: CreateApiKeyArgs => ZIO[
        Authed with ApiKeyStore,
        Nothing,
        ApiKey
      ],
      deleteApiKey: DeleteApiKeyArgs => ZIO[
        Authed with ApiKeyStore,
        Nothing,
        Unit
      ],
      purchaseCredit: PurchaseCreditArgs => ZIO[
        Authed with CreditStore with PurchaseStore,
        Throwable,
        Credit
      ]
  )

  val api = graphQL[
    Authed
      with ApiKeyStore
      with CreditStore
      with RequestStore
      with PurchaseStore,
    Queries,
    Mutations,
    Unit
  ](
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

  val http: Http[
    ApiKeyStore with CreditStore with RequestStore with PurchaseStore,
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
          @@ jwtAuth

    }
}
