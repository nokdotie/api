package ie.nok.api.graphql.adverts

import caliban.schema.Schema

case class AdvertFacet(
    url: String
)

object AdvertFacet {
  given Schema[Any, AdvertFacet] = Schema.gen

  def fromInternal(internal: ie.nok.adverts.AdvertFacet): AdvertFacet =
    AdvertFacet(url = internal.url)
}
