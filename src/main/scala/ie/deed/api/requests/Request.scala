package ie.deed.api.requests

import java.time.Instant
import ie.deed.api.users.UserIdentifier
import ie.deed.api.utils.Base64Uuid

case class Request(
    id: Base64Uuid,
    url: String,
    status: RequestStatusCode,
    createdAt: Instant,
    userIdentifier: UserIdentifier
)
