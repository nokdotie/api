package ie.deed.api.apikeys.graphql

import caliban.relay.{Connection, PageInfo}

case class ApiKeyConnection(
    pageInfo: PageInfo,
    edges: List[ApiKeyEdge]
) extends Connection[ApiKeyEdge]
