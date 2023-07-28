package ie.nok.api.graphql.adverts

import caliban.relay.Edge
import caliban.schema.Schema
import ie.nok.api.utils.pagination.JsonCursor

case class AdvertEdge(
    cursor: JsonCursor[Int],
    node: Advert
) extends Edge[JsonCursor[Int], Advert]

object AdvertEdge {
  given Schema[Any, AdvertEdge] = Schema.gen
}
