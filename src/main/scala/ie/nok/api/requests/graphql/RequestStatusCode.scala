package ie.deed.api.requests.graphql

enum RequestStatusCode {
  case Ok
}

object RequestStatusCode {
  def fromInternal(
      internal: ie.deed.api.requests.RequestStatusCode
  ): RequestStatusCode =
    internal match {
      case ie.deed.api.requests.RequestStatusCode.Ok => RequestStatusCode.Ok
    }
}
