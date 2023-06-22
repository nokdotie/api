package ie.nok.api.graphql.adverts

import java.time.Instant

case class Advert(
    advertUrl: String,
    advertPriceInEur: Int,
    propertyAddress: String,
    propertyImageUrls: List[String],
    propertySizeInSqtMtr: BigDecimal,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int
)

object Advert {
  def fromInternal(internal: ie.nok.adverts.Advert): Advert =
    Advert(
      advertUrl = internal.advertUrl,
      advertPriceInEur = internal.advertPriceInEur,
      propertyAddress = internal.propertyAddress,
      propertyImageUrls = internal.propertyImageUrls,
      propertySizeInSqtMtr = internal.propertySizeInSqtMtr,
      propertyBedroomsCount = internal.propertyBedroomsCount,
      propertyBathroomsCount = internal.propertyBathroomsCount
    )
}
