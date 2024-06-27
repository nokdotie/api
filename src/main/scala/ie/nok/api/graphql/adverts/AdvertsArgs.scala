package ie.nok.api.graphql.adverts

import caliban.relay.ForwardPaginationArgs
import caliban.schema.{ArgBuilder, Schema}
import ie.nok.adverts.stores
import ie.nok.api.utils.Direction
import ie.nok.api.utils.filters.*
import ie.nok.api.utils.pagination.JsonCursor
import java.time.Instant
import zio.json.{JsonCodec, DeriveJsonCodec}

import scala.util.chaining.scalaUtilChainingOps

case class AdvertsArgs(
    filter: Option[AdvertsFilter],
    sort: Option[AdvertsSort],
    first: Option[Int],
    after: Option[String]
) extends ForwardPaginationArgs[JsonCursor[AdvertsCursor]]

object AdvertsArgs {
  given ArgBuilder[AdvertsArgs]  = ArgBuilder.gen
  given Schema[Any, AdvertsArgs] = Schema.gen
}

case class AdvertsFilter(
    address: Option[StringFilter],
    coordinates: Option[CoordinatesFilter],
    priceInEur: Option[IntFilter],
    bedroomsCount: Option[IntFilter],
    bathroomsCount: Option[IntFilter],
    sizeInSqtMtr: Option[IntFilter],
    propertyType: Option[StringFilter]
)

object AdvertsFilter {
  given ArgBuilder[AdvertsFilter]  = ArgBuilder.gen
  given Schema[Any, AdvertsFilter] = Schema.gen

  def toInternal(external: AdvertsFilter): stores.AdvertFilter =
    List(
      external.address
        .map(StringFilter.toInternal)
        .map(stores.AdvertFilter.PropertyAddress(_)),
      external.coordinates
        .map(CoordinatesFilter.toInternal)
        .map(stores.AdvertFilter.PropertyCoordinates(_)),
      external.priceInEur
        .map(IntFilter.toInternal)
        .map(stores.AdvertFilter.AdvertPriceInEur(_)),
      external.bedroomsCount
        .map(IntFilter.toInternal)
        .map(stores.AdvertFilter.PropertyBedroomsCount(_)),
      external.bathroomsCount
        .map(IntFilter.toInternal)
        .map(stores.AdvertFilter.PropertyBathroomsCount(_)),
      external.sizeInSqtMtr
        .map(IntFilter.toInternal)
        .map(stores.AdvertFilter.PropertySizeInSqtMtr(_)),
      external.propertyType
        .map(StringFilter.toInternal)
        .map(stores.AdvertFilter.PropertyType(_))
    ).flatten
      .pipe {
        case Nil          => stores.AdvertFilter.Empty
        case head :: Nil  => head
        case head :: tail => stores.AdvertFilter.And(head, tail: _*)
      }
}

sealed trait AdvertsSortableField
object AdvertsSortableField {
  case object CreatedAt extends AdvertsSortableField

  given ArgBuilder[CreatedAt.type]       = ArgBuilder.gen
  given ArgBuilder[AdvertsSortableField] = ArgBuilder.gen

  given Schema[Any, CreatedAt.type]       = Schema.gen
  given Schema[Any, AdvertsSortableField] = Schema.gen

  def toInternal(external: AdvertsSortableField): stores.AdvertSortableField =
    external match {
      case AdvertsSortableField.CreatedAt => stores.AdvertSortableField.CreatedAt
    }
}

case class AdvertsSort(
    field: AdvertsSortableField,
    direction: Direction
)

object AdvertsSort {
  given ArgBuilder[AdvertsSort]  = ArgBuilder.gen
  given Schema[Any, AdvertsSort] = Schema.gen

  def toInternal(external: AdvertsSort): stores.AdvertSort =
    stores.AdvertSort(
      AdvertsSortableField.toInternal(external.field),
      Direction.toInternal(external.direction)
    )
}

enum AdvertsCursor {
  case Empty
  case CreatedAt(createdAt: Instant, identifier: String)
}

object AdvertsCursor {
  given JsonCodec[AdvertsCursor] = DeriveJsonCodec.gen[AdvertsCursor]

  def fromInternal(internal: stores.AdvertCursor): AdvertsCursor =
    internal match {
      case stores.AdvertCursor.Empty => AdvertsCursor.Empty
      case stores.AdvertCursor.CreatedAt(createdAt, identifier) =>
        AdvertsCursor.CreatedAt(createdAt, identifier)
    }

  def toInternal(external: AdvertsCursor): stores.AdvertCursor =
    external match {
      case AdvertsCursor.Empty => stores.AdvertCursor.Empty
      case AdvertsCursor.CreatedAt(createdAt, identifier) =>
        stores.AdvertCursor.CreatedAt(createdAt, identifier)
    }
}
