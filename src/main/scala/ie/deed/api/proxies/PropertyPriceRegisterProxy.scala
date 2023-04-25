package ie.deed.api.proxies

import zio.http.*

object PropertyPriceRegisterProxy {
  val origin = s"https://property-price-register-3uvvqmwyoq-ew.a.run.app"

  val http: Http[Client, Throwable, Request, Response] = Http.empty
}
