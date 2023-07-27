package ie.nok.api.graphql.adverts

import caliban.schema.Schema

case class AdvertCoordinates(
    latitude: BigDecimal,
    longitude: BigDecimal
)

object AdvertCoordinates {
  given Schema[Any, AdvertCoordinates] = Schema.gen

  def fromInternal(internal: ie.nok.geographic.Coordinates): AdvertCoordinates =
    AdvertCoordinates(
      latitude = internal.latitude,
      longitude = internal.longitude
    )
}
