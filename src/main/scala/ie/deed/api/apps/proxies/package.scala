package ie.deed.api.apps.proxies

import zio.http.*
import zio.http.model.Headers

def get(url: String): zio.ZIO[Client, Throwable, Response] =
  Client.request(url, headers = Headers("x-api-key", "TODO"))
