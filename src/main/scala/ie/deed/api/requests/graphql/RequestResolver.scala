package ie.deed.api.requests.graphql

import caliban.relay.PageInfo

object RequestResolver {
  def requests(args: RequestsArgs): RequestConnection =
    RequestConnection(PageInfo(false, false, None, None), Nil)
}
