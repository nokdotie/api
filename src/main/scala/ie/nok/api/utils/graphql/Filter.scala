package ie.nok.api.utils.graphql

import ie.nok.adverts.stores
import scala.util.chaining.scalaUtilChainingOps
import caliban.schema.{Schema, ArgBuilder}

case class StringFilter(
    or: Option[List[StringFilter]],
    contains: Option[String],
    startsWith: Option[String]
)

object StringFilter {
  given ArgBuilder[StringFilter] = ArgBuilder.gen
  given Schema[Any, StringFilter] = Schema.gen

  def toStoreFilter(filter: StringFilter): stores.StringFilter = {
    val or = filter.or
      .flatMap {
        case Nil         => Option.empty
        case head :: Nil => toStoreFilter(head).pipe(Option(_))
        case head :: tail =>
          val headFilter = toStoreFilter(head)
          val tailFilter = tail.map(toStoreFilter)

          stores.StringFilter.Or(headFilter, tailFilter: _*).pipe(Option(_))
      }

    val contains = filter.contains
      .map(_.trim)
      .map(stores.StringFilter.Contains(_))

    val startsWith = filter.startsWith
      .map(_.trim)
      .map(stores.StringFilter.StartsWith(_))

    List(or, contains, startsWith).flatten
      .pipe {
        case Nil          => stores.StringFilter.Empty
        case head :: tail => stores.StringFilter.And(head, tail: _*)
      }
  }
}
