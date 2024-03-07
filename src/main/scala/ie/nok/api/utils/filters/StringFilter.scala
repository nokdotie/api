package ie.nok.api.utils.filters

import caliban.schema.{Schema, ArgBuilder}
import ie.nok.stores.filters.StringFilter as Filter

case class StringFilter(
    contains: Option[String]
)

object StringFilter {
  given ArgBuilder[StringFilter]  = ArgBuilder.gen
  given Schema[Any, StringFilter] = Schema.gen

  def toStoreFilter(filter: StringFilter): Filter =
    filter.contains
      .map(_.trim)
      .fold(Filter.Empty)(
        Filter.ContainsCaseInsensitive(_)
      )

}
