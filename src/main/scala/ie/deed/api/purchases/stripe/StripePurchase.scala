package ie.deed.api.purchases.stripe

import java.time.Instant

case class StripePurchase(
    checkoutSessionId: String,
    creditAmount: Int
)
