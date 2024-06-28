package ie.nok.api.utils.filters

import caliban.schema.{Schema, ArgBuilder}
import ie.nok.stores.filters.IntFilter as Filter
import scala.util.chaining.scalaUtilChainingOps

case class IntFilter(
    lessThanOrEqual: Option[Int],
    greaterThanOrEqual: Option[Int]
)

object IntFilter {
  given ArgBuilder[IntFilter]  = ArgBuilder.gen
  given Schema[Any, IntFilter] = Schema.gen

  def toInternal(external: IntFilter): Filter =
    List(
      external.lessThanOrEqual.map(Filter.LessThanOrEqual),
      external.greaterThanOrEqual.map(Filter.GreaterThanOrEqual)
    ).flatten
      .pipe {
        case Nil          => Filter.Empty
        case head :: Nil  => head
        case head :: tail => Filter.And(head, tail: _*)
      }

}
