package ie.nok.api.utils.filters

import caliban.schema.{Schema, ArgBuilder}
import ie.nok.adverts.stores
import scala.util.chaining.scalaUtilChainingOps

case class IntFilter(
    lessThanOrEqual: Option[Int],
    greaterThanOrEqual: Option[Int]
)

object IntFilter {
  given ArgBuilder[IntFilter]  = ArgBuilder.gen
  given Schema[Any, IntFilter] = Schema.gen

  def toStoreFilter(filter: IntFilter): stores.IntFilter =
    List(
      filter.lessThanOrEqual.map(stores.IntFilter.LessThanOrEqual),
      filter.greaterThanOrEqual.map(stores.IntFilter.GreaterThanOrEqual)
    ).flatten
      .pipe {
        case Nil          => stores.IntFilter.Empty
        case head :: Nil  => head
        case head :: tail => stores.IntFilter.And(head, tail: _*)
      }

}
