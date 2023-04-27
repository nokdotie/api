package ie.deed.api.requests.graphql

import caliban.relay.PageInfo

object RequestResolver {
  def requests(args: RequestsArgs): RequestConnection =
    RequestConnection(Nil, PageInfo(false, false, None, None))
}
