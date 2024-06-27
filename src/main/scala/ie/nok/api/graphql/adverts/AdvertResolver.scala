package ie.nok.api.graphql.adverts

import ie.nok.stores.filters.StringFilter
import ie.nok.adverts.stores
import ie.nok.api.utils.pagination.{Connection, PaginationArgs, JsonCursor}
import ie.nok.unit.Direction
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO

object AdvertResolver {
  def adverts(
      args: AdvertsArgs
  ): ZIO[stores.AdvertStore, Throwable, AdvertConnection] = {
    val filter =
      args.filter.fold(stores.AdvertFilter.Empty)(AdvertsFilter.toInternal)
    val sort  = args.sort.fold(stores.AdvertSort.default)(AdvertsSort.toInternal)
    val first = PaginationArgs.first(args)
    val after = PaginationArgs
      .after[AdvertsCursor](args)
      .fold(stores.AdvertCursor.Empty)(AdvertsCursor.toInternal)

    for {
      page <- stores.AdvertStore.getPage(filter, sort, first, after)
      connection = Connection[
        (ie.nok.adverts.Advert, stores.AdvertCursor),
        Advert,
        JsonCursor[AdvertsCursor],
        AdvertEdge,
        AdvertConnection
      ](
        AdvertConnection.apply,
        AdvertEdge.apply,
        (_, cursor) => JsonCursor(AdvertsCursor.fromInternal(cursor)),
        (advert, _) => Advert.fromInternal(advert),
        page
      )
    } yield connection
  }

  def advert(
      args: AdvertArgs
  ): ZIO[stores.AdvertStore, Throwable, Option[Advert]] = {
    val filter = args.identifier
      .pipe { StringFilter.Equals(_) }
      .pipe { stores.AdvertFilter.PropertyIdentifier(_) }
    val sort  = stores.AdvertSort.default
    val first = 1
    val after = stores.AdvertCursor.Empty

    stores.AdvertStore
      .getPage(filter, sort, first, after)
      .map {
        _.items.headOption
          .map { case (advert, _) => Advert.fromInternal(advert) }
      }
  }
}
