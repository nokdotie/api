package ie.deed.api.requests.graphql

import caliban.relay.{Base64Cursor, Edge}

case class RequestEdge(
    node: Request,
    cursor: Base64Cursor
) extends Edge[Base64Cursor, Request]
