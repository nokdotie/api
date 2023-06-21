package ie.nok.api.graphql.adverts

import java.time.Instant

case class Advert(
    advertUrl: String,
    advertPrice: Int,
    advertPriceInEur: Int,
    propertyAddress: String,
    propertyImageUrls: List[String],
    propertySizeInSqtMtr: BigDecimal,
    propertySizeinSqtMtr: BigDecimal,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int
)

object Advert {
  def fromInternal(internal: ie.nok.adverts.Advert): Advert =
    Advert(
      advertUrl = internal.advertUrl,
      advertPrice = internal.advertPriceInEur,
      advertPriceInEur = internal.advertPriceInEur,
      propertyAddress = internal.propertyAddress,
      propertyImageUrls = internal.propertyImageUrls,
      propertySizeinSqtMtr = internal.propertySizeInSqtMtr,
      propertySizeInSqtMtr = internal.propertySizeInSqtMtr,
      propertyBedroomsCount = internal.propertyBedroomsCount,
      propertyBathroomsCount = internal.propertyBathroomsCount
    )
}
