package ie.nok.api.utils.filters

import caliban.schema.{ArgBuilder, Schema}
import ie.nok.stores.filters.StringFilter as Filter

import scala.util.chaining.scalaUtilChainingOps

case class StringFilter(
    contains: Option[String],
    equals: Option[String]
)

object StringFilter {
  given ArgBuilder[StringFilter]  = ArgBuilder.gen
  given Schema[Any, StringFilter] = Schema.gen

  def toStoreFilter(filter: StringFilter): Filter =
    List(
      filter.contains.map(Filter.ContainsCaseInsensitive(_)),
      filter.equals.map(Filter.EqualsCaseInsensitive(_))
    ).flatten
      .pipe {
        case Nil          => Filter.Empty
        case head :: Nil  => head
        case head :: tail => Filter.And(head, tail: _*)
      }
}
