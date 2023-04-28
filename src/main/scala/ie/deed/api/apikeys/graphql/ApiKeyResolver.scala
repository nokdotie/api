package ie.deed.api.apikeys.graphql

import zio.ZIO
import ie.deed.api.apikeys.graphql.ApiKey
import ie.deed.api.apikeys.stores.ApiKeyStore
import ie.deed.api.users.UserIdentifier
import ie.deed.api.utils.graphql.{Pagination, JsonCursor}
import scala.util.chaining.scalaUtilChainingOps

object ApiKeyResolver {
  val userIdentifier = UserIdentifier("123")

  def apiKeys(
      args: ApiKeysArgs
  ): ZIO[ApiKeyStore, Nothing, ApiKeyConnection] = {
    val limit = Pagination.limit(args)
    val createdAtBefore = Pagination.cursor(args)

    for {
      store <- ApiKeyStore.getPage(userIdentifier, limit, createdAtBefore)
      graphql = store.map { apiKey =>
        ApiKey(apiKey.key, apiKey.description, apiKey.createdAt)
      }
      connection = Pagination.connection(
        ApiKeyConnection.apply,
        _.createdAt.pipe(JsonCursor.apply),
        ApiKeyEdge.apply,
        graphql
      )
    } yield connection
  }

  def createApiKey(args: CreateApiKeyArgs): ZIO[ApiKeyStore, Nothing, ApiKey] =
    ApiKeyStore
      .create(userIdentifier, args.description)
      .map { apiKey =>
        ApiKey(apiKey.key, apiKey.description, apiKey.createdAt)
      }

  def deleteApiKey(args: DeleteApiKeyArgs): ZIO[ApiKeyStore, Nothing, Unit] =
    ApiKeyStore.delete(userIdentifier, args.key)

}
