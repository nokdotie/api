package ie.deed.api.requests.graphql

import caliban.relay.ForwardPaginationArgs
import ie.deed.api.utils.graphql.JsonCursor
import java.time.Instant

case class RequestsArgs(
    first: Option[Int],
    after: Option[String]
) extends ForwardPaginationArgs[JsonCursor[Instant]]
