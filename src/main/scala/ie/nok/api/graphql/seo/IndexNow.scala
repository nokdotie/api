package ie.nok.api.graphql.seo

import caliban.schema.Schema

case class IndexNow(
    isKeyValid: (key: String) => Boolean
)

object IndexNow {
  given Schema[Any, IndexNow] = Schema.gen

  val default = IndexNow((key: String) => key == "indexnow-123")
}
