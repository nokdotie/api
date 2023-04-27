package ie.deed.api.credits.graphql

import caliban.relay.{Base64Cursor, Edge}

case class CreditEdge(
    node: Credit,
    cursor: Base64Cursor
) extends Edge[Base64Cursor, Credit]
