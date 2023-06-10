package ie.nok.api.utils.graphql

import zio.json.JsonCodec
import caliban.relay._

object Pagination {
  val MinValue = 1
  val MaxValue = 25

  def limit(pagination: ForwardPaginationArgs[_]): Int =
    pagination.first.getOrElse(MaxValue).min(MaxValue).max(MinValue)

  def cursor[A: JsonCodec](
      pagination: ForwardPaginationArgs[JsonCursor[A]]
  ): Option[A] =
    pagination.after
      .map { Cursor[JsonCursor[A]].decode(_).toOption }
      .flatten
      .map { _.value }

  def pageInfoFromEdges[Cu: Cursor, E <: Edge[Cu, _]](
      edges: List[E]
  ): PageInfo =
    PageInfo(
      hasNextPage = edges.nonEmpty,
      hasPreviousPage = false,
      startCursor = edges.headOption.map { _.encodeCursor },
      endCursor = edges.lastOption.map { _.encodeCursor }
    )

  def connectionFromEdges[Cu: Cursor, E <: Edge[Cu, _], Co <: Connection[E]](
      connection: (PageInfo, List[E]) => Co,
      edges: List[E]
  ): Co =
    connection(pageInfoFromEdges(edges), edges)

  def edges[A, Cu: Cursor, E <: Edge[Cu, A]](
      cursor: A => Cu,
      edge: (Cu, A) => E,
      nodes: List[A]
  ): List[E] =
    nodes.map { node => edge(cursor(node), node) }

  def connection[A, Cu: Cursor, E <: Edge[Cu, A], Co <: Connection[E]](
      connection: (PageInfo, List[E]) => Co,
      cursor: A => Cu,
      edge: (Cu, A) => E,
      nodes: List[A]
  ): Co =
    connectionFromEdges(connection, edges(cursor, edge, nodes))

}
