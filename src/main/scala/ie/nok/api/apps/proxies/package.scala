package ie.deed.api.apps.proxies

import zio.http.*
import zio.http.model.Headers

def get(url: String): zio.ZIO[Client, Throwable, Response] =
  Client.request(
    s"https://building-energy-rating-3uvvqmwyoq-ew.a.run.app$url",
    headers = Headers("x-api-key", "yBF1ntnYoyk9BPJtS0rc")
  )
