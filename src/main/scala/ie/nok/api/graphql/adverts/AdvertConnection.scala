package ie.nok.api.graphql.adverts

import caliban.relay.{Connection, PageInfo}

case class AdvertConnection(
    pageInfo: PageInfo,
    edges: List[AdvertEdge]
) extends Connection[AdvertEdge]
