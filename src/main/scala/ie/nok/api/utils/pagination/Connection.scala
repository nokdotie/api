package ie.nok.api.utils.pagination

import caliban.relay._
import zio.json.JsonCodec

object Connection {
  private def pageInfoFromEdges[Cu: Cursor, E <: Edge[Cu, _]](
      edges: List[E],
      edgesExpectedLength: Int
  ): PageInfo =
    PageInfo(
      hasNextPage = edges.lengthIs == edgesExpectedLength,
      hasPreviousPage = false,
      startCursor = edges.headOption.map { _.encodeCursor },
      endCursor = edges.lastOption.map { _.encodeCursor }
    )

  private def connectionFromEdges[Cu: Cursor, E <: Edge[
    Cu,
    _
  ], Co <: Connection[E]](
      connection: (PageInfo, List[E]) => Co,
      edges: List[E],
      edgesExpectedLength: Int
  ): Co =
    connection(pageInfoFromEdges(edges, edgesExpectedLength), edges)

  private def edges[Ctx, N, Cu: Cursor, E <: Edge[Cu, N]](
      edge: (Cu, N) => E,
      cursor: Ctx => Cu,
      node: Ctx => N,
      ctx: List[Ctx]
  ): List[E] =
    ctx.map { ctx => edge(cursor(ctx), node(ctx)) }

  def apply[Ctx, N, Cu: Cursor, E <: Edge[Cu, N], Co <: Connection[E]](
      connection: (PageInfo, List[E]) => Co,
      edge: (Cu, N) => E,
      cursor: Ctx => Cu,
      node: Ctx => N,
      ctx: List[Ctx],
      edgesExpectedLength: Int
  ): Co =
    connectionFromEdges(
      connection,
      edges(edge, cursor, node, ctx),
      edgesExpectedLength
    )
}
