package ie.nok.api.graphql.adverts

import caliban.relay.Edge
import ie.nok.api.utils.graphql.JsonCursor

case class AdvertEdge(
    cursor: JsonCursor[Int],
    node: Advert
) extends Edge[JsonCursor[Int], Advert]
