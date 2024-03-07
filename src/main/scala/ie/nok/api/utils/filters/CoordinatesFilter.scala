package ie.nok.api.utils.filters

import caliban.schema.{Schema, ArgBuilder}
import ie.nok.api.utils.geographic.{Coordinates, BoundsRectangle}
import ie.nok.geographic.CoordinatesFilter as Filter

case class CoordinatesFilter(
    withinRectangle: Option[BoundsRectangle]
)

object CoordinatesFilter {
  given ArgBuilder[CoordinatesFilter]  = ArgBuilder.gen
  given Schema[Any, CoordinatesFilter] = Schema.gen

  def toStoreFilter(filter: CoordinatesFilter): Filter =
    filter.withinRectangle
      .fold(Filter.Empty) { bounds =>
        val northEast = Coordinates.toInternal(bounds.northEast)
        val southWest = Coordinates.toInternal(bounds.southWest)

        Filter.WithinRectangle(northEast, southWest)
      }

}
