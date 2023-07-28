package ie.nok.api.utils.filters

import caliban.schema.{Schema, ArgBuilder}
import ie.nok.adverts.stores
import ie.nok.api.utils.geographic.{Coordinates, BoundsRectangle}

case class CoordinatesFilter(
    withinRectangle: Option[BoundsRectangle]
)

object CoordinatesFilter {
  given ArgBuilder[CoordinatesFilter] = ArgBuilder.gen
  given Schema[Any, CoordinatesFilter] = Schema.gen

  def toStoreFilter(filter: CoordinatesFilter): stores.CoordinatesFilter =
    filter.withinRectangle
      .fold(stores.CoordinatesFilter.Empty) { bounds =>
        val northEast = Coordinates.toInternal(bounds.northEast)
        val southWest = Coordinates.toInternal(bounds.southWest)

        stores.CoordinatesFilter.WithinRectangle(northEast, southWest)
      }

}
