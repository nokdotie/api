package ie.nok.api.graphql.adverts

import caliban.relay.ForwardPaginationArgs
import ie.nok.api.utils.graphql.{IntFilter, JsonCursor, StringFilter}

case class AdvertsArgs(
    filter: Option[AdvertsFilter],
    first: Option[Int],
    after: Option[String]
) extends ForwardPaginationArgs[JsonCursor[Int]]

case class AdvertsFilter(
    address: Option[StringFilter],
    price: Option[IntFilter],
    sizeinSqtMtr: Option[IntFilter],
    bedroomsCount: Option[IntFilter],
    bathroomsCount: Option[IntFilter]
)
