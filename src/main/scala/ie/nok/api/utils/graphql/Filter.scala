package ie.nok.api.utils.graphql

case class StringFilter(
    contains: Option[String]
)

case class IntFilter(
    greaterThanOrEqual: Option[Int],
    lessThanOrEqual: Option[Int]
)
