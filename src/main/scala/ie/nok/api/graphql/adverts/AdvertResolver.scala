package ie.nok.api.graphql.adverts

import ie.nok.adverts.stores
import ie.nok.adverts.stores.{AdvertStore, AdvertStoreCursor}
import ie.nok.api.utils.graphql.{Pagination, JsonCursor}
import zio.ZIO

object AdvertResolver {
  def adverts(
      args: AdvertsArgs
  ): ZIO[AdvertStore, Throwable, AdvertConnection] = {
    val filter =
      args.filter.fold(stores.AdvertFilter.Empty)(AdvertsFilter.toStoreFilter)
    val first = Pagination.first(args)
    val after = Pagination
      .after(args)
      .fold(AdvertStoreCursor(0))(AdvertStoreCursor.apply)

    for {
      page <- AdvertStore.getPage(filter, first, after)
      advertWithIndex = page.zipWithIndex.map { (advert, index) =>
        (Advert.fromInternal(advert), index + after.index + 1)
      }
      connection = Pagination.connection[(Advert, Int), Advert, JsonCursor[
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
}
