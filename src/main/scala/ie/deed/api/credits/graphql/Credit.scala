package ie.deed.api.credits.graphql

import java.time.Instant

case class Credit(
    amount: Int,
    createdAt: Instant
)
