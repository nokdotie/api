package ie.deed.api.users.graphql

object UserResolver {
  def me(): User = User("john.doe@gmail.com")
}
