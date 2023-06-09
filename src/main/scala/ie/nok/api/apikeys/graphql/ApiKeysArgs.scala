package ie.deed.api.apikeys.graphql

import caliban.relay.ForwardPaginationArgs
import ie.deed.api.utils.graphql.JsonCursor
import java.time.Instant

case class ApiKeysArgs(
    first: Option[Int],
    after: Option[String]
) extends ForwardPaginationArgs[JsonCursor[Instant]]
