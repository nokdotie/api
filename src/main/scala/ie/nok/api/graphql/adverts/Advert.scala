package ie.nok.api.graphql.adverts

import java.time.Instant

case class Advert(
    advertUrl: String,
    advertPrice: Int,
    propertyAddress: String,
    propertyImageUrls: List[String],
    propertySizeinSqtMtr: BigDecimal,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int
)

object Advert {
  def fromInternal(internal: ie.nok.adverts.Advert): Advert =
    Advert(
      advertUrl = internal.advertUrl,
      advertPrice = internal.advertPrice,
      propertyAddress = internal.propertyAddress,
      propertyImageUrls = internal.propertyImageUrls,
      propertySizeinSqtMtr = internal.propertySizeinSqtMtr,
      propertyBedroomsCount = internal.propertyBedroomsCount,
      propertyBathroomsCount = internal.propertyBathroomsCount
    )
}
