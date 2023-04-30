package ie.deed.api.apikeys.graphql

import ie.deed.api.apikeys.graphql.ApiKey
import ie.deed.api.apikeys.stores.ApiKeyStore
import ie.deed.api.utils.authentication.Authed
import ie.deed.api.utils.graphql.{Pagination, JsonCursor}
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO

object ApiKeyResolver {
  def apiKeys(
      args: ApiKeysArgs
  ): ZIO[Authed with ApiKeyStore, Nothing, ApiKeyConnection] = {
    val limit = Pagination.limit(args)
    val createdAtBefore = Pagination.cursor(args)

    for {
      userIdentifier <- Authed.userIdentifier
      store <- ApiKeyStore.getPage(userIdentifier, limit, createdAtBefore)
      graphql = store.map { ApiKey.fromInternal }
      connection = Pagination.connection(
        ApiKeyConnection.apply,
        _.createdAt.pipe(JsonCursor.apply),
        ApiKeyEdge.apply,
        graphql
      )
    } yield connection
  }

  def createApiKey(
      args: CreateApiKeyArgs
  ): ZIO[Authed with ApiKeyStore, Nothing, ApiKey] =
    for {
      userIdentifier <- Authed.userIdentifier
      apiKey <- ApiKeyStore.create(userIdentifier, args.description)
      graphql = ApiKey.fromInternal(apiKey)
    } yield graphql

  def deleteApiKey(
      args: DeleteApiKeyArgs
  ): ZIO[Authed with ApiKeyStore, Nothing, Unit] =
    for {
      userIdentifier <- Authed.userIdentifier
      _ <- ApiKeyStore.delete(userIdentifier, args.key)
    } yield ()

}
