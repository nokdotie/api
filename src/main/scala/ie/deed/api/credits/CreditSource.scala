package ie.deed.api.credits

enum CreditSource {
  case Trial
  case StripePurchase(checkoutSessionId: String)
}
