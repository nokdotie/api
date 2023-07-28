package ie.nok.api.utils.geographic

import caliban.schema.{ArgBuilder, Schema}

case class BoundsRectangle(northEast: Coordinates, southWest: Coordinates)

object BoundsRectangle {
  given Schema[Any, BoundsRectangle] = Schema.gen
  given ArgBuilder[BoundsRectangle] = ArgBuilder.gen
}
