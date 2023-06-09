package ie.deed.api.utils.authentication

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.{FirebaseApp, FirebaseOptions}
import ie.deed.api.users.UserIdentifier
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Try
import zio.http._
import zio.http.middleware.RequestHandlerMiddlewares
import zio.http.HttpAppMiddleware.customAuthProviding

object JwtAuthMiddleware {
  private val firebaseAuth = FirebaseOptions
    .builder()
    .setProjectId("deed-ie")
    .setCredentials(GoogleCredentials.getApplicationDefault())
    .build()
    .pipe { FirebaseApp.initializeApp }
    .pipe { FirebaseAuth.getInstance }

  def jwtAuth[R0] = customAuthProviding[R0, Authed] { headers =>
    for {
      bearerToken <- headers.bearerToken
      firebaseToken <- Try { firebaseAuth.verifyIdToken(bearerToken) }.toOption
      email <- Option.when(firebaseToken.isEmailVerified)(
        firebaseToken.getEmail
      )
    } yield Authed(UserIdentifier(email))
  }
}
