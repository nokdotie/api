package ie.nok.api.graphql.adverts

import ie.nok.filter.StringFilter
import ie.nok.adverts.stores.{AdvertFilter, AdvertStore, AdvertStoreCursor}
import ie.nok.api.utils.pagination.{Connection, PaginationArgs, JsonCursor}
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO

object AdvertResolver {
  def adverts(
      args: AdvertsArgs
  ): ZIO[AdvertStore, Throwable, AdvertConnection] = {
    val filter =
      args.filter.fold(AdvertFilter.Empty)(AdvertsFilter.toStoreFilter)
    val first = PaginationArgs.first(args)
    val after = PaginationArgs
      .after(args)
      .fold(AdvertStoreCursor(0))(AdvertStoreCursor.apply)

    for {
      page <- AdvertStore.getPage(filter, first, after)
      advertWithIndex = page.zipWithIndex.map { (advert, index) =>
        (Advert.fromInternal(advert), index + after.index + 1)
      }
      connection = Connection[(Advert, Int), Advert, JsonCursor[
        Int
      ], AdvertEdge, AdvertConnection](
        AdvertConnection.apply,
        AdvertEdge.apply,
        (_, index) => JsonCursor(index),
        (advert, _) => advert,
        advertWithIndex,
        first
      )
    } yield connection
  }

  def advert(
      args: AdvertArgs
  ): ZIO[AdvertStore, Throwable, Option[Advert]] = {
    val filter = args.identifier
      .pipe { StringFilter.Equals(_) }
      .pipe { AdvertFilter.PropertyIdentifier(_) }
    val first = 1
    val after = AdvertStoreCursor(0)

    AdvertStore
      .getPage(filter, first, after)
      .map { _.headOption }
      .map { _.map(Advert.fromInternal) }
  }
}
