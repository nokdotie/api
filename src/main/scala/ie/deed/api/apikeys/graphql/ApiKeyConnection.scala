package ie.deed.api.apikeys.graphql

import caliban.relay.{Connection, PageInfo}

case class ApiKeyConnection(
    edges: List[ApiKeyEdge],
    pageInfo: PageInfo
) extends Connection[ApiKeyEdge]
