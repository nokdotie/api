package ie.deed.api.credits.stores

import ie.deed.api.users.UserIdentifier
import java.time.Instant
import zio.ZIO
import ie.deed.api.credits.{Credit, CreditSource}

trait CreditStore {
  def create(
      userIdentifier: UserIdentifier,
      amount: Int,
      source: CreditSource
  ): Credit
  def getPage(
      userIdentifier: UserIdentifier,
      limit: Int,
      createdAtBefore: Option[Instant]
  ): List[Credit]
}

object CreditStore {

  def create(
      userIdentifier: UserIdentifier,
      amount: Int,
      source: CreditSource
  ): ZIO[CreditStore, Nothing, Credit] =
    ZIO.serviceWith[CreditStore](_.create(userIdentifier, amount, source))

  def getPage(
      userIdentifier: UserIdentifier,
      limit: Int,
      createdAtBefore: Option[Instant]
  ): ZIO[CreditStore, Nothing, List[Credit]] =
    ZIO.serviceWith[CreditStore](
      _.getPage(userIdentifier, limit, createdAtBefore)
    )

}
