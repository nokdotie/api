package ie.deed.api.apikeys.graphql

import java.time.Instant

case class ApiKey(
    key: String,
    description: String,
    createdAt: Instant
)
