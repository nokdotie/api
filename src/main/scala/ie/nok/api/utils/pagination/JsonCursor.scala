package ie.nok.api.utils.pagination

import caliban.relay.Cursor
import caliban.schema.Schema
import ie.nok.codec.base64.Base64
import ie.nok.codec.json.Json
import zio.json.*

import scala.util.chaining.scalaUtilChainingOps

case class JsonCursor[A: JsonCodec](value: A)
object JsonCursor {
  given [A: JsonCodec]: Cursor[JsonCursor[A]] = new Cursor[JsonCursor[A]] {
    type T = A

    def encode(a: JsonCursor[A]): String = a.value.toJson.pipe {
      Base64.encode
    }
    def decode(s: String): Either[String, JsonCursor[A]] =
      Base64
        .decode(s)
        .flatMap { Json.decode[A] }
        .map { JsonCursor.apply }
        .toEither
        .left
        .map { _.getMessage }

    def value(cursor: JsonCursor[A]): A = cursor.value
  }

  given [A: JsonCodec]: Schema[Any, JsonCursor[A]] =
    Schema.stringSchema.contramap(Cursor[JsonCursor[A]].encode)

}
