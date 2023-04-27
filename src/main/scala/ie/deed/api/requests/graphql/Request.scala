package ie.deed.api.requests.graphql

import java.time.Instant

case class Request(
    url: String,
    status: RequestStatusCode,
    createdAt: Instant
)
