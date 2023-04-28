package ie.deed.api.utils

import java.util.{Base64, UUID}
import scala.util.chaining.scalaUtilChainingOps

sealed abstract case class Base64Uuid private (value: String)
object Base64Uuid {
  private val encoder = Base64.getUrlEncoder.withoutPadding

  def random(): Base64Uuid =
    UUID.randomUUID.toString.getBytes
      .pipe { encoder.encodeToString }
      .pipe { new Base64Uuid(_) {} }
}
