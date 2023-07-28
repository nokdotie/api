package ie.nok.api.utils.filters

import caliban.schema.{Schema, ArgBuilder}
import ie.nok.adverts.stores

case class StringFilter(
    contains: Option[String]
)

object StringFilter {
  given ArgBuilder[StringFilter] = ArgBuilder.gen
  given Schema[Any, StringFilter] = Schema.gen

  def toStoreFilter(filter: StringFilter): stores.StringFilter =
    filter.contains
      .map(_.trim)
      .fold(stores.StringFilter.Empty)(stores.StringFilter.Contains(_))

}
