package ie.nok.api.utils.graphql

import caliban.relay.PageInfo
import caliban.schema.Schema

object Caliban {
  given Schema[Any, PageInfo] = Schema.gen
}
