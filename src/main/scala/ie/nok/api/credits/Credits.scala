package ie.deed.api.credits

import java.time.Instant
import ie.deed.api.users.UserIdentifier
import ie.deed.api.utils.Base64Uuid

case class Credit(
    id: Base64Uuid,
    amount: Int,
    source: CreditSource,
    createdAt: Instant,
    userIdentifier: UserIdentifier
)
