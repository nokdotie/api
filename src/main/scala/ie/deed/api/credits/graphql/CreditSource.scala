package ie.deed.api.credits.graphql

enum CreditSource {
  case Trial
  case Purchase
}

object CreditSource {
  def fromInternal(source: ie.deed.api.credits.CreditSource): CreditSource =
    source match {
      case ie.deed.api.credits.CreditSource.Trial    => CreditSource.Trial
      case ie.deed.api.credits.CreditSource.Purchase => CreditSource.Purchase
    }
}
