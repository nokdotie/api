package ie.deed.api.credits.graphql

import caliban.relay.{Base64Cursor, PaginationArgs}

case class CreditsArgs(
    first: Option[Int],
    after: Option[String]
) extends PaginationArgs[Base64Cursor] {
  override val before: Option[String] = None
  override val last: Option[Int] = None
}
