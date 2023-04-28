package ie.deed.api.credits.graphql

import ie.deed.api.utils.graphql.JsonCursor
import caliban.relay.Edge
import java.time.Instant

case class CreditEdge(cursor: JsonCursor[Instant], node: Credit)
    extends Edge[JsonCursor[Instant], Credit]
