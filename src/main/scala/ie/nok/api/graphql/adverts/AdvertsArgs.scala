package ie.nok.api.graphql.adverts

import caliban.relay.ForwardPaginationArgs
import caliban.schema.{ArgBuilder, Schema}
import ie.nok.adverts.stores
import ie.nok.api.utils.filters.CoordinatesFilter
import ie.nok.api.utils.pagination.JsonCursor
import ie.nok.api.utils.filters.StringFilter
import scala.util.chaining.scalaUtilChainingOps

case class AdvertsArgs(
    filter: Option[AdvertsFilter],
    first: Option[Int],
    after: Option[String]
) extends ForwardPaginationArgs[JsonCursor[Int]]

object AdvertsArgs {
  given ArgBuilder[AdvertsArgs]  = ArgBuilder.gen
  given Schema[Any, AdvertsArgs] = Schema.gen
}

case class AdvertsFilter(
    address: Option[StringFilter],
    coordinates: Option[CoordinatesFilter]
)

object AdvertsFilter {
  given ArgBuilder[AdvertsFilter]  = ArgBuilder.gen
  given Schema[Any, AdvertsFilter] = Schema.gen

  def toStoreFilter(filter: AdvertsFilter): stores.AdvertFilter =
    List(
      filter.address
        .map(StringFilter.toStoreFilter)
        .map(stores.AdvertFilter.PropertyAddress(_)),
      filter.coordinates
        .map(CoordinatesFilter.toStoreFilter)
        .map(stores.AdvertFilter.PropertyCoordinates(_))
    ).flatten
      .pipe {
        case Nil          => stores.AdvertFilter.Empty
        case head :: tail => stores.AdvertFilter.And(head, tail: _*)
      }
}
