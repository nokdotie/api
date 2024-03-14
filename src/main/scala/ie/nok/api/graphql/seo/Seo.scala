package ie.nok.api.graphql.seo

import caliban.schema.Schema

case class Seo(
    indexNow: IndexNow
)

object Seo {
  given Schema[Any, Seo] = Schema.gen

  val default = Seo(IndexNow.default)
}
