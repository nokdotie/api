package ie.deed.api.apikeys.graphql

import caliban.relay.PageInfo
import java.time.Instant

object ApiKeyResolver {
  def apiKeys(args: ApiKeysArgs): ApiKeyConnection =
    ApiKeyConnection(Nil, PageInfo(false, false, None, None))
  def createApiKey(args: CreateApiKeyArgs): ApiKey =
    ApiKey("123", args.description, Instant.now)
  def deleteApiKey(args: DeleteApiKeyArgs): Unit = ()
}
