package ie.nok.api.utils.filters

import caliban.schema.{ArgBuilder, Schema}
import ie.nok.stores.filters.StringFilter as Filter

import scala.util.chaining.scalaUtilChainingOps

case class StringFilter(
    contains: Option[String],
    equals: Option[String],
    in: Option[List[String]]
)

object StringFilter {
  given ArgBuilder[StringFilter]  = ArgBuilder.gen
  given Schema[Any, StringFilter] = Schema.gen

  private def toFilter(list: List[Filter], f: (Filter, Seq[Filter]) => Filter): Filter =
    list
      .pipe {
        case Nil          => Filter.Empty
        case head :: Nil  => head
        case head :: tail => f(head, tail)
      }

  def toStoreFilter(filter: StringFilter): Filter =
    List(
      filter.contains.map(Filter.ContainsCaseInsensitive(_)),
      filter.equals.map(Filter.EqualsCaseInsensitive(_)),
      filter.in.map(_.map(Filter.EqualsCaseInsensitive(_))).map(toFilter(_, Filter.Or.apply))
    ).flatten
      .pipe { toFilter(_, Filter.And.apply) }
}
