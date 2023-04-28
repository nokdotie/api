package ie.deed.api.apikeys

import ie.deed.api.users.UserIdentifier
import java.time.Instant

case class ApiKey(
    key: String,
    description: String,
    createdAt: Instant,
    userIdentifier: UserIdentifier
)
