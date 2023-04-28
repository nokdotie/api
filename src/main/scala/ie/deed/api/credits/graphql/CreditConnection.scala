package ie.deed.api.credits.graphql

import caliban.relay.{Connection, PageInfo}

case class CreditConnection(
    pageInfo: PageInfo,
    edges: List[CreditEdge]
) extends Connection[CreditEdge]
