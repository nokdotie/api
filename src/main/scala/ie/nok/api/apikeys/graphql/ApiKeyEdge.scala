package ie.deed.api.apikeys.graphql

import caliban.relay.Edge
import ie.deed.api.utils.graphql.JsonCursor
import java.time.Instant

case class ApiKeyEdge(
    cursor: JsonCursor[Instant],
    node: ApiKey
) extends Edge[JsonCursor[Instant], ApiKey]
