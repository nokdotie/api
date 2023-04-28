package ie.deed.api.requests.graphql

import caliban.relay.{Connection, PageInfo}

case class RequestConnection(
    pageInfo: PageInfo,
    edges: List[RequestEdge]
) extends Connection[RequestEdge]
