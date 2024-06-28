package ie.nok.api.utils.geographic

import caliban.schema.{ArgBuilder, Schema}

case class Coordinates(latitude: BigDecimal, longitude: BigDecimal)

object Coordinates {
  given Schema[Any, Coordinates] = Schema.gen
  given ArgBuilder[Coordinates]  = ArgBuilder.gen

  def fromInternal(internal: ie.nok.geographic.Coordinates): Coordinates =
    Coordinates(internal.latitude, internal.longitude)

  def toInternal(external: Coordinates): ie.nok.geographic.Coordinates =
    ie.nok.geographic.Coordinates(external.latitude, external.longitude)
}
