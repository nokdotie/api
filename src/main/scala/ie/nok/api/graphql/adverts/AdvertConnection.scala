package ie.nok.api.graphql.adverts

import caliban.relay.{Connection, PageInfo}
import caliban.schema.Schema
import ie.nok.api.utils.Caliban.given

case class AdvertConnection(
    pageInfo: PageInfo,
    edges: List[AdvertEdge]
) extends Connection[AdvertEdge]

object AdvertConnection {
  given Schema[Any, AdvertConnection] = Schema.gen
}
