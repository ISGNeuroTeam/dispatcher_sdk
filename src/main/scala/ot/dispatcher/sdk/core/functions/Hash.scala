package ot.dispatcher.sdk.core.functions

import java.math.BigInteger
import java.security.MessageDigest

object Hash {
  val md5: String => String = (s: String) => new BigInteger(1, MessageDigest.getInstance("MD5").digest(s.getBytes)).toString(16)
  def sha1: String => String = (s: String) => new BigInteger(1, MessageDigest.getInstance("SHA1").digest(s.getBytes)).toString(16)
}