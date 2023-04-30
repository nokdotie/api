package ie.deed.api.requests.graphql

import caliban.relay.PageInfo
import ie.deed.api.utils.graphql.Pagination
import ie.deed.api.requests.stores.RequestStore
import ie.deed.api.users.UserIdentifier
import ie.deed.api.utils.graphql.JsonCursor
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO

object RequestResolver {
  // TODO: value should be comming from the JWT in the Authorization header
  val userIdentifier = UserIdentifier("123")

  def requests(
      args: RequestsArgs
  ): ZIO[RequestStore, Nothing, RequestConnection] = {
    val limit = Pagination.limit(args)
    val createdAtBefore = Pagination.cursor(args)

    for {
      store <- RequestStore.getPage(userIdentifier, limit, createdAtBefore)
      graphql = store.map { Request.fromInternal }
      connection = Pagination.connection(
        RequestConnection.apply,
        _.createdAt.pipe(JsonCursor.apply),
        RequestEdge.apply,
        graphql
      )
    } yield connection
  }
}
