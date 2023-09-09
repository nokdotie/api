package ie.nok.api.graphql.adverts

import caliban.schema.Schema
import java.time.Instant
import ie.nok.api.utils.geographic.Coordinates

case class Advert(
    advertUrl: String,
    advertPriceInEur: Int,
    propertyAddress: String,
    propertyCoordinates: Coordinates,
    propertyImageUrls: List[String],
    propertySizeInSqtMtr: BigDecimal,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int,
    propertyBuildingEnergyRating: Option[String],
    sources: List[InformationSource]
)

object Advert {
  given Schema[Any, Advert] = Schema.gen

  def fromInternal(internal: ie.nok.adverts.Advert): Advert =
    Advert(
      advertUrl = internal.advertUrl,
      advertPriceInEur = internal.advertPriceInEur,
      propertyAddress = internal.propertyAddress,
      propertyCoordinates =
        Coordinates.fromInternal(internal.propertyCoordinates),
      propertyImageUrls = internal.propertyImageUrls,
      propertySizeInSqtMtr = internal.propertySizeInSqtMtr,
      propertyBedroomsCount = internal.propertyBedroomsCount,
      propertyBathroomsCount = internal.propertyBathroomsCount,
      propertyBuildingEnergyRating = internal.propertyBuildingEnergyRating.map {
        _.toString
      },
      sources = internal.sources.map { InformationSource.fromInternal }
    )
}
