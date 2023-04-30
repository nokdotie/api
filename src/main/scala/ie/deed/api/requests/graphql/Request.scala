package ie.deed.api.requests.graphql

import java.time.Instant

case class Request(
    url: String,
    status: RequestStatusCode,
    createdAt: Instant
)

object Request {
  def fromInternal(internal: ie.deed.api.requests.Request): Request =
    Request(
      internal.url,
      RequestStatusCode.fromInternal(internal.status),
      internal.createdAt
    )
}
