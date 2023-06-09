package ie.deed.api.apikeys.stores

import ie.deed.api.apikeys.ApiKey
import ie.deed.api.users.UserIdentifier
import java.time.Instant
import zio.ZIO

trait ApiKeyStore {
  def create(userIdentifier: UserIdentifier, description: String): ApiKey
  def delete(userIdentifier: UserIdentifier, key: String): Unit
  def getPage(
      userIdentifier: UserIdentifier,
      limit: Int,
      createdAtBefore: Option[Instant]
  ): List[ApiKey]
}

object ApiKeyStore {

  def create(
      userIdentifier: UserIdentifier,
      description: String
  ): ZIO[ApiKeyStore, Nothing, ApiKey] =
    ZIO.serviceWith[ApiKeyStore](_.create(userIdentifier, description))

  def delete(
      userIdentifier: UserIdentifier,
      key: String
  ): ZIO[ApiKeyStore, Nothing, Unit] =
    ZIO.serviceWith[ApiKeyStore](_.delete(userIdentifier, key))

  def getPage(
      userIdentifier: UserIdentifier,
      limit: Int,
      createdAtBefore: Option[Instant]
  ): ZIO[ApiKeyStore, Nothing, List[ApiKey]] =
    ZIO.serviceWith[ApiKeyStore](
      _.getPage(userIdentifier, limit, createdAtBefore)
    )

}
