package ie.nok.api.graphql.adverts

import caliban.schema.Schema
import ie.nok.api.utils.geographic.Coordinates

case class Advert(
    advertUrl: String,
    advertPriceInEur: Int,
    propertyIdentifier: String,
    propertyDescription: Option[String],
    propertyAddress: String,
    propertyCoordinates: Coordinates,
    propertyImageUrls: List[String],
    propertySizeInSqtMtr: BigDecimal,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int,
    propertyBuildingEnergyRating: Option[String],
    propertyBuildingEnergyRatingCertificateNumber: Option[Int],
    propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear: Option[BigDecimal],
    sources: List[InformationSource],
    advertiser: Option[Advertiser]
)

object Advert {
  given Schema[Any, Advert] = Schema.gen

  def fromInternal(internal: ie.nok.adverts.Advert): Advert =
    Advert(
      advertUrl = internal.advertUrl,
      advertPriceInEur = internal.advertPriceInEur,
      propertyIdentifier = internal.propertyIdentifier,
      propertyDescription = internal.propertyDescription,
      propertyAddress = internal.propertyAddress,
      propertyCoordinates = Coordinates.fromInternal(internal.propertyCoordinates),
      propertyImageUrls = internal.propertyImageUrls,
      propertySizeInSqtMtr = internal.propertySizeInSqtMtr,
      propertyBedroomsCount = internal.propertyBedroomsCount,
      propertyBathroomsCount = internal.propertyBathroomsCount,
      propertyBuildingEnergyRating = internal.propertyBuildingEnergyRating.map { _.toString },
      propertyBuildingEnergyRatingCertificateNumber = internal.propertyBuildingEnergyRatingCertificateNumber,
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = internal.propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
      sources = internal.sources.map { InformationSource.fromInternal },
      advertiser = internal.advertiser.map { Advertiser.fromInternal }
    )
}
