package ie.nok.api.graphql.adverts

import ie.nok.adverts.stores.{AdvertStore, AdvertStoreCursor}
import ie.nok.api.utils.graphql.{Pagination, JsonCursor}
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO

object AdvertResolver {
  def adverts(
      args: AdvertsArgs
  ): ZIO[AdvertStore, Throwable, AdvertConnection] = {
    val first = Pagination.first(args)
    val after =
      Pagination.after(args).fold(AdvertStoreCursor(0))(AdvertStoreCursor.apply)

    for {
      page <- AdvertStore.getPage(first, after)
      advertWithIndex = page.zipWithIndex.map {
        (advert, index) =>
          (Advert.fromInternal(advert), index + after.index + 1)
      }
      connection = Pagination.connection[(Advert, Int), Advert, JsonCursor[
        Int
      ], AdvertEdge, AdvertConnection](
        AdvertConnection.apply,
        AdvertEdge.apply,
        (_, index) => JsonCursor(index),
        (advert, _) => advert,
        advertWithIndex
      )
    } yield connection
  }
}
