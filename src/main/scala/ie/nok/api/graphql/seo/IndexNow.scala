package ie.nok.api.graphql.seo

import caliban.schema.Schema
import ie.nok.seo.indexnow.IndexNow.key

case class IndexNow(
    isKeyValid: (key: String) => Boolean
)

object IndexNow {
  given Schema[Any, IndexNow] = Schema.gen

  val default = IndexNow(_ == key)
}
