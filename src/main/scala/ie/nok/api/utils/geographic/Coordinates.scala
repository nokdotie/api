package ie.nok.api.utils.geographic

import caliban.schema.{ArgBuilder, Schema}

case class Coordinates(latitude: BigDecimal, longitude: BigDecimal)

object Coordinates {
  given Schema[Any, Coordinates] = Schema.gen
  given ArgBuilder[Coordinates]  = ArgBuilder.gen

  def fromInternal(coordinates: ie.nok.geographic.Coordinates): Coordinates =
    Coordinates(coordinates.latitude, coordinates.longitude)

  def toInternal(coordinates: Coordinates): ie.nok.geographic.Coordinates =
    ie.nok.geographic.Coordinates(coordinates.latitude, coordinates.longitude)
}
