package ie.deed.api.purchases

case class Purchase(
    source: PurchaseSource.Stripe,
    creditAmount: Int
)
