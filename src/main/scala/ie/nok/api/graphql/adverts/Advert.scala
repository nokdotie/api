package ie.nok.api.graphql.adverts

import caliban.schema.Schema
import java.time.Instant

case class Advert(
    advertUrl: String,
    advertPriceInEur: Int,
    propertyAddress: String,
    propertyCoordinates: AdvertCoordinates,
    propertyImageUrls: List[String],
    propertySizeInSqtMtr: BigDecimal,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int
)

object Advert {
  given Schema[Any, Advert] = Schema.gen

  def fromInternal(internal: ie.nok.adverts.Advert): Advert =
    Advert(
      advertUrl = internal.advertUrl,
      advertPriceInEur = internal.advertPriceInEur,
      propertyAddress = internal.propertyAddress,
      propertyCoordinates =
        AdvertCoordinates.fromInternal(internal.propertyCoordinates),
      propertyImageUrls = internal.propertyImageUrls,
      propertySizeInSqtMtr = internal.propertySizeInSqtMtr,
      propertyBedroomsCount = internal.propertyBedroomsCount,
      propertyBathroomsCount = internal.propertyBathroomsCount
    )
}
