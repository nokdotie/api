package ie.deed.api.apikeys.graphql

import java.time.Instant

case class ApiKey(
    key: String,
    description: String,
    createdAt: Instant
)

object ApiKey {
  def fromInternal(internal: ie.deed.api.apikeys.ApiKey): ApiKey =
    ApiKey(
      key = internal.id.value,
      description = internal.description,
      createdAt = internal.createdAt
    )
}
