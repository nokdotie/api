package ie.nok.api.graphql.adverts

import caliban.schema.{ArgBuilder, Schema}

case class AdvertArgs(
    identifier: String
)

object AdvertArgs {
  given ArgBuilder[AdvertArgs] = ArgBuilder.gen
  given Schema[Any, AdvertArgs] = Schema.gen
}
