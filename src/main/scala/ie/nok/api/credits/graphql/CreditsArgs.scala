package ie.deed.api.credits.graphql

import caliban.relay.ForwardPaginationArgs
import ie.deed.api.utils.graphql.JsonCursor
import java.time.Instant

case class CreditsArgs(
    first: Option[Int],
    after: Option[String]
) extends ForwardPaginationArgs[JsonCursor[Instant]]
