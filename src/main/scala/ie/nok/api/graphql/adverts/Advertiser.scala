package ie.nok.api.graphql.adverts

import caliban.schema.Schema
import java.time.Instant

case class Advertiser(
    identifier: String,
    name: String,
    pictureUrl: String,
    emailAddresses: List[String],
    phoneNumbers: List[String],
    physicalAddresses: List[String],
    propertyServicesRegulatoryAuthorityLicenceNumber: String
)

object Advertiser {
  given Schema[Any, Advertiser] = Schema.gen

  def fromInternal(internal: ie.nok.advertisers.Advertiser): Advertiser =
    Advertiser(
      identifier = internal.id,
      name = internal.name,
      pictureUrl = internal.pictureUrl,
      emailAddresses = internal.emailAddresses,
      phoneNumbers = internal.phoneNumbers,
      physicalAddresses = internal.physicalAddresses,
      propertyServicesRegulatoryAuthorityLicenceNumber =
        internal.propertyServicesRegulatoryAuthorityLicenceNumber
    )
}
