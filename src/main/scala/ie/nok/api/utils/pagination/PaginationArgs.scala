package ie.nok.api.utils.pagination

import caliban.relay._
import zio.json.JsonCodec

object PaginationArgs {
  val MinValue = 1
  val MaxValue = 100

  def first(pagination: ForwardPaginationArgs[_]): Int =
    pagination.first.getOrElse(MaxValue).min(MaxValue).max(MinValue)

  def after[A: JsonCodec](
      pagination: ForwardPaginationArgs[JsonCursor[A]]
  ): Option[A] =
    pagination.after
      .flatMap(Cursor[JsonCursor[A]].decode(_).toOption)
      .map { _.value }
}
