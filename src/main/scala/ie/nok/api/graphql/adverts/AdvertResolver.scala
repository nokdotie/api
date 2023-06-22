package ie.nok.api.graphql.adverts

import ie.nok.adverts.stores.{
  AdvertFilter,
  StringFilter,
  NumericFilter,
  AdvertStore,
  AdvertStoreCursor
}
import ie.nok.api.utils.graphql.{Pagination, JsonCursor}
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO

object AdvertResolver {
  private def toAdvertFilter(from: Option[AdvertsFilter]): AdvertFilter =
    List(
      from
        .flatMap(_.address)
        .flatMap(_.contains)
        .map { contains =>
          AdvertFilter.Address(StringFilter.Contains(contains))
        },
      from
        .flatMap(_.priceInEur)
        .flatMap(_.greaterThanOrEqual)
        .map { gte =>
          AdvertFilter.PriceInEur(NumericFilter.GreaterThanOrEqual(gte))
        },
      from
        .flatMap(_.priceInEur)
        .flatMap(_.lessThanOrEqual)
        .map { lte =>
          AdvertFilter.PriceInEur(NumericFilter.LessThanOrEqual(lte))
        },
      from
        .flatMap(_.sizeInSqtMtr)
        .flatMap(_.greaterThanOrEqual)
        .map { gte =>
          AdvertFilter.SizeInSqtMtr(NumericFilter.GreaterThanOrEqual(gte))
        },
      from
        .flatMap(_.sizeInSqtMtr)
        .flatMap(_.lessThanOrEqual)
        .map { lte =>
          AdvertFilter.SizeInSqtMtr(NumericFilter.LessThanOrEqual(lte))
        },
      from
        .flatMap(_.bedroomsCount)
        .flatMap(_.greaterThanOrEqual)
        .map { gte =>
          AdvertFilter.BedroomsCount(NumericFilter.GreaterThanOrEqual(gte))
        },
      from
        .flatMap(_.bedroomsCount)
        .flatMap(_.lessThanOrEqual)
        .map { lte =>
          AdvertFilter.BedroomsCount(NumericFilter.LessThanOrEqual(lte))
        },
      from
        .flatMap(_.bathroomsCount)
        .flatMap(_.greaterThanOrEqual)
        .map { gte =>
          AdvertFilter.BathroomsCount(NumericFilter.GreaterThanOrEqual(gte))
        },
      from
        .flatMap(_.bathroomsCount)
        .flatMap(_.lessThanOrEqual)
        .map { lte =>
          AdvertFilter.BathroomsCount(NumericFilter.LessThanOrEqual(lte))
        }
    ).flatten
      .pipe {
        case Nil          => AdvertFilter.Empty
        case head :: tail => AdvertFilter.And(head, tail: _*)
      }

  def adverts(
      args: AdvertsArgs
  ): ZIO[AdvertStore, Throwable, AdvertConnection] = {
    val filter = toAdvertFilter(args.filter)
    println(filter)
    val first = Pagination.first(args)
    val after =
      Pagination.after(args).fold(AdvertStoreCursor(0))(AdvertStoreCursor.apply)

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
        advertWithIndex
      )
    } yield connection
  }
}
