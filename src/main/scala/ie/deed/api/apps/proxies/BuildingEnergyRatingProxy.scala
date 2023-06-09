package ie.nok.api.apps.proxies

import zio.http.*
import zio.http.model.{Headers, Method}

object BuildingEnergyRatingProxy {
  val origin = s"https://building-energy-rating-3uvvqmwyoq-ew.a.run.app"

  val http: Http[Client, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "v1" / "ber" / int(certificateNumber) =>
        get(s"$origin/v1/ber/$certificateNumber")
      case Method.GET -> !! / "v1" / "eircode" / eircode / "ber" =>
        get(s"$origin/v1/eircode/$eircode/ber")
    }

}
