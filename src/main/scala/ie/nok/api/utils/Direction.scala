package ie.nok.api.utils

import caliban.schema.{ArgBuilder, Schema}

sealed trait Direction
object Direction {
  case object Ascending  extends Direction
  case object Descending extends Direction

  given Schema[Any, Direction.Ascending.type]  = Schema.gen
  given Schema[Any, Direction.Descending.type] = Schema.gen
  given Schema[Any, Direction]                 = Schema.gen

  given ArgBuilder[Direction.Ascending.type]  = ArgBuilder.gen
  given ArgBuilder[Direction.Descending.type] = ArgBuilder.gen
  given ArgBuilder[Direction]                 = ArgBuilder.gen

  def toInternal(external: Direction): ie.nok.unit.Direction =
    external match {
      case Direction.Ascending  => ie.nok.unit.Direction.Ascending
      case Direction.Descending => ie.nok.unit.Direction.Descending
    }
}
