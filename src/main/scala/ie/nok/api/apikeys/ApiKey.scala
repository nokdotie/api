package ie.deed.api.apikeys

import ie.deed.api.utils.Base64Uuid
import ie.deed.api.users.UserIdentifier
import java.time.Instant

case class ApiKey(
    id: Base64Uuid,
    description: String,
    createdAt: Instant,
    userIdentifier: UserIdentifier
)
