package ie.nok.api.graphql.adverts

import caliban.relay.ForwardPaginationArgs
import caliban.schema.{ArgBuilder, Schema}
import ie.nok.adverts.stores
import ie.nok.api.utils.graphql.{JsonCursor, StringFilter}
import scala.util.chaining.scalaUtilChainingOps

case class AdvertsArgs(
    filter: Option[AdvertsFilter],
    first: Option[Int],
    after: Option[String]
) extends ForwardPaginationArgs[JsonCursor[Int]]

object AdvertsArgs {
  given ArgBuilder[AdvertsArgs] = ArgBuilder.gen
  given Schema[Any, AdvertsArgs] = Schema.gen
}

case class AdvertsFilter(
    address: Option[StringFilter],
    priceInEur: Option[IntFilter],
    sizeInSqtMtr: Option[IntFilter],
    bedroomsCount: Option[IntFilter],
    bathroomsCount: Option[IntFilter]
)
