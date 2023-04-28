package ie.deed.api.credits.graphql

import caliban.relay.PageInfo
import java.time.Instant

object CreditResolver {
  def credits(args: CreditsArgs): CreditConnection =
    CreditConnection(PageInfo(false, false, None, None), Nil)
  def buyCredit(args: BuyCreditArgs): Credit = Credit(args.amount, Instant.now)
}
