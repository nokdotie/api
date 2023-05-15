package ie.deed.api.purchases

enum PurchaseSource {
  case Stripe(checkoutSessionId: String)
}
