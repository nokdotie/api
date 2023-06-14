package ie.nok.api.utils.graphql

import zio.json.JsonCodec
import caliban.relay._

object Pagination {
  val MinValue = 1
  val MaxValue = 25

  def first(pagination: ForwardPaginationArgs[_]): Int =
    pagination.first.getOrElse(MaxValue).min(MaxValue).max(MinValue)

  def after[A: JsonCodec](
      pagination: ForwardPaginationArgs[JsonCursor[A]]
  ): Option[A] =
    pagination.after
      .map { Cursor[JsonCursor[A]].decode(_).toOption }
      .flatten
      .map { _.value }

  private def pageInfoFromEdges[Cu: Cursor, E <: Edge[Cu, _]](
      edges: List[E]
  ): PageInfo =
    PageInfo(
      hasNextPage = edges.nonEmpty,
      hasPreviousPage = false,
      startCursor = edges.headOption.map { _.encodeCursor },
      endCursor = edges.lastOption.map { _.encodeCursor }
    )

  private def connectionFromEdges[Cu: Cursor, E <: Edge[
    Cu,
    _
  ], Co <: Connection[E]](
      connection: (PageInfo, List[E]) => Co,
      edges: List[E]
  ): Co =
    connection(pageInfoFromEdges(edges), edges)

  private def edges[Ctx, N, Cu: Cursor, E <: Edge[Cu, N]](
      edge: (Cu, N) => E,
      cursor: Ctx => Cu,
      node: Ctx => N,
      ctx: List[Ctx]
  ): List[E] =
    ctx.map { ctx => edge(cursor(ctx), node(ctx)) }

  def connection[Ctx, N, Cu: Cursor, E <: Edge[Cu, N], Co <: Connection[E]](
      connection: (PageInfo, List[E]) => Co,
      edge: (Cu, N) => E,
      cursor: Ctx => Cu,
      node: Ctx => N,
      ctx: List[Ctx]
  ): Co =
    connectionFromEdges(connection, edges(edge, cursor, node, ctx))

}
