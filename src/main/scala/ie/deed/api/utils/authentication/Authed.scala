package ie.deed.api.utils.authentication

import ie.deed.api.users.UserIdentifier
import zio.ZIO

case class Authed(
    userIdentifier: UserIdentifier
)

object Authed {
  def userIdentifier: ZIO[Authed, Nothing, UserIdentifier] =
    ZIO.serviceWith[Authed](_.userIdentifier)
}
