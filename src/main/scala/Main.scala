import zio._
import zio.http._

object HelloWorld extends ZIOAppDefault {

  val app: App[Any] =
    Http.collect[Request] {
      case Method.GET -> !! / "text" => Response.text("Hello World!")
    }

  override val run =
    Server.serve(app).provide(Server.default)
}
