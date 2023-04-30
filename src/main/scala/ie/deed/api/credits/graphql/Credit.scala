package ie.deed.api.credits.graphql

import java.time.Instant

case class Credit(
    amount: Int,
    source: CreditSource,
    createdAt: Instant
)

object Credit {
  def fromInternal(source: ie.deed.api.credits.Credit): Credit = Credit(
    source.amount,
    CreditSource.fromInternal(source.source),
    source.createdAt
  )
}
