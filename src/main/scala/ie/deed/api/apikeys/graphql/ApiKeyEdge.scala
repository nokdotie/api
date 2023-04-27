package ie.deed.api.apikeys.graphql

import caliban.relay.{Base64Cursor, Edge}

case class ApiKeyEdge(
    node: ApiKey,
    cursor: Base64Cursor
) extends Edge[Base64Cursor, ApiKey]
