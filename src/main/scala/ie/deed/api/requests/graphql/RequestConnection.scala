package ie.deed.api.requests.graphql

import caliban.relay.{Connection, PageInfo}

case class RequestConnection(
    edges: List[RequestEdge],
    pageInfo: PageInfo
) extends Connection[RequestEdge]
