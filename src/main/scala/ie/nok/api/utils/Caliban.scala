package ie.nok.api.utils

import caliban.relay.PageInfo
import caliban.schema.Schema

object Caliban {
  given Schema[Any, PageInfo] = Schema.gen
}
