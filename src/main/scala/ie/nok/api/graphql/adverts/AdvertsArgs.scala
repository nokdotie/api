package ie.nok.api.graphql.adverts

import caliban.relay.ForwardPaginationArgs
import ie.nok.api.utils.graphql.JsonCursor
import java.time.Instant

case class AdvertsArgs(
    first: Option[Int],
    after: Option[String]
) extends ForwardPaginationArgs[JsonCursor[Int]]
