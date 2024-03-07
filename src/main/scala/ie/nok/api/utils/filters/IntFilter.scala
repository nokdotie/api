package ie.nok.api.utils.filters

import caliban.schema.{Schema, ArgBuilder}
import ie.nok.filter.IntFilter as Filter
import scala.util.chaining.scalaUtilChainingOps

case class IntFilter(
    lessThanOrEqual: Option[Int],
    greaterThanOrEqual: Option[Int]
)

object IntFilter {
  given ArgBuilder[IntFilter]  = ArgBuilder.gen
  given Schema[Any, IntFilter] = Schema.gen

  def toStoreFilter(filter: IntFilter): Filter =
    List(
      filter.lessThanOrEqual.map(Filter.LessThanOrEqual),
      filter.greaterThanOrEqual.map(Filter.GreaterThanOrEqual)
    ).flatten
      .pipe {
        case Nil          => Filter.Empty
        case head :: Nil  => head
        case head :: tail => Filter.And(head, tail: _*)
      }

}
