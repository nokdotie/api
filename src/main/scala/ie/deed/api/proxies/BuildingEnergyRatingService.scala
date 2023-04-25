package ie.deed.api.proxies

import zio.http.*
import zio.http.model.{Headers, Method}

object BuildingEnergyRatingProxy {
  def get(url: String): zio.ZIO[Client, Throwable, Response] =
    Client.request(
      s"https://building-energy-rating-3uvvqmwyoq-ew.a.run.app$url",
      headers = Headers("x-api-key", "yBF1ntnYoyk9BPJtS0rc")
    )

  val http: Http[Client, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "v1" / "ber" / int(certificateNumber) =>
        get(s"/v1/ber/$certificateNumber")
      case Method.GET -> !! / "v1" / "eircode" / eircode / "ber" =>
        get(s"/v1/eircode/$eircode/ber")
    }

}
