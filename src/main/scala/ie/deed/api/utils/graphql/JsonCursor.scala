package ie.nok.api.utils.graphql

import caliban.relay.Cursor
import java.util.Base64
import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps
import zio.json._
import caliban.schema.Schema

case class JsonCursor[A: JsonCodec](value: A)
object JsonCursor {
  lazy val encoder = Base64.getUrlEncoder.withoutPadding()
  lazy val decoder = Base64.getUrlDecoder

  given [A: JsonCodec]: Cursor[JsonCursor[A]] = new Cursor[JsonCursor[A]] {
    type T = A

    def encode(a: JsonCursor[A]): String =
      a.value.toJson.getBytes("UTF-8").pipe(encoder.encodeToString)

    def decode(s: String): Either[String, JsonCursor[A]] =
      Try { decoder.decode(s) }
        .map { new String(_, "UTF-8") }
        .flatMap { json =>
          json
            .fromJson[A]
            .left
            .map { err => Throwable(s"$err: $json") }
            .toTry
        }
        .map { JsonCursor.apply }
        .toEither
        .left
        .map(_.getMessage())

    def value(cursor: JsonCursor[A]): A = cursor.value
  }

  given [A: JsonCodec]: Schema[Any, JsonCursor[A]] =
    Schema.stringSchema.contramap(Cursor[JsonCursor[A]].encode)

}
