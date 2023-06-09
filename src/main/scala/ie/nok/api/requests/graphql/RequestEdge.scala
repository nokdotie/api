package ie.deed.api.requests.graphql

import caliban.relay.Edge
import ie.deed.api.utils.graphql.JsonCursor
import java.time.Instant

case class RequestEdge(
    cursor: JsonCursor[Instant],
    node: Request
) extends Edge[JsonCursor[Instant], Request]
