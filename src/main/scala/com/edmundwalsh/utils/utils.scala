package com.edmundwalsh.utils

import java.net.URL
import java.net.URLConnection
import sun.net.www.protocol.https.HttpsURLConnectionImpl

import scala.util.{Try, Success, Failure}

object UtilFuncs {

  /** function checks the validity of a url and also tests and appends the
    * approprite http or https if missing
    * @param inputUrlString
    *   String input url
    * @return
    *   String of updated url of URL type with correct protocol
    */
  def urlConnectionChecker(inputUrlString: String): String = {
    val inputUrl: URL = new URL(inputUrlString)
    val conn: java.net.URLConnection = inputUrl.openConnection()
    // set get method
    val header = conn.getHeaderFields()
    header.get(null).get(0)
  }
}
