package ie.nok.api.utils.pagination

import caliban.relay._
import ie.nok.stores.pagination.Page
import zio.json.JsonCodec

object Connection {
  def apply[Ctx, N, Cu: Cursor, E <: Edge[Cu, N], Co <: Connection[E]](
      toConnection: (PageInfo, List[E]) => Co,
      toEdge: (Cu, N) => E,
      toCursor: Ctx => Cu,
      toNode: Ctx => N,
      ctx: Page[Ctx]
  ): Co = {
    val edges = ctx.items.map { ctx => toEdge(toCursor(ctx), toNode(ctx)) }
    val pageInfo = PageInfo(
      hasNextPage = ctx.pageInfo.hasNextPage,
      hasPreviousPage = ctx.pageInfo.hasPreviousPage,
      startCursor = edges.headOption.map { _.encodeCursor },
      endCursor = edges.lastOption.map { _.encodeCursor }
    )

    toConnection(pageInfo, edges)
  }
}
