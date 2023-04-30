package ie.deed.api.requests.stores

import java.time.Instant
import ie.deed.api.requests.Request
import ie.deed.api.users.UserIdentifier
import zio.ZIO

trait RequestStore {
  def getPage(
      userIdentifier: UserIdentifier,
      limit: Int,
      createdAtBefore: Option[Instant]
  ): List[Request]
}

object RequestStore {

  def getPage(
      userIdentifier: UserIdentifier,
      limit: Int,
      createdAtBefore: Option[Instant]
  ): ZIO[RequestStore, Nothing, List[Request]] =
    ZIO.serviceWith[RequestStore](
      _.getPage(userIdentifier, limit, createdAtBefore)
    )

}
