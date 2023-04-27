package ie.deed.api.credits.graphql

import caliban.relay.{Connection, PageInfo}

case class CreditConnection(
    edges: List[CreditEdge],
    pageInfo: PageInfo
) extends Connection[CreditEdge]
