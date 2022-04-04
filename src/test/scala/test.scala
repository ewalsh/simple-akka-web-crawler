package com.adthena.test

import org.scalatest.funsuite.AnyFunSuite
import com.edmundwalsh.web.WebPageParser.{calcPageRank, checkIfVisited}
import com.edmundwalsh.connection.UrlConnectionChecker
import java.net.URLConnection
import java.net.URL
import scala.collection.mutable.Queue

/** this set of tests is not exhaustive, for example, I did not include the main
  * calcPageRank as this would be variable over time Realizing this, I would
  * likely break out the ratio calc, etc to test those separately.
  *
  * A few illustrative tests
  */

class TestSuite extends AnyFunSuite {

  test("That URL Connections are identified if already visited or not") {
    val url0: URL = new URL("https://google.com")
    val urlConn0: URLConnection = url0.openConnection()
    val alreadyVisited: Queue[String] = Queue("https://google.com")
    val test0 = checkIfVisited(urlConn0, alreadyVisited)

    val url1: URL = new URL("https://microsoft.com")
    val urlConn1: URLConnection = url1.openConnection()
    val test1 = checkIfVisited(urlConn1, alreadyVisited)

    assert(test0 && !test1)
  }

  test(
    "That given a URL without a protocol correct final url with protocol is found"
  ) {

    val test = new UrlConnectionChecker("www.microsoft.com").conn.get.toString

    assert(
      test == "sun.net.www.protocol.https.DelegateHttpsURLConnection:https://www.microsoft.com"
    )
  }

}
