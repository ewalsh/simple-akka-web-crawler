package com.edmundwalsh.connection

import java.net.URL
import java.net.URLConnection
import sun.net.www.protocol.https.HttpsURLConnectionImpl

import scala.util.{Try, Success, Failure}

class UrlConnectionChecker(inputUrlString: String) {

  /** function checks the validity of a url and also tests and appends the
    * approprite http or https if missing
    * @param inputUrlString
    *   String input url
    * @return
    *   String of updated url of URL type with correct protocol
    */
  def urlConnectionChecker(inputUrlString: String): Option[URLConnection] = {

    /** */
    def getResponseCode(
        url: URL,
        tryProtocolSwap: Boolean = false
    ): (URLConnection, String) = {
      //
      /** internal function to swap url protocols
        * @param url
        *   java.net.URL input url
        * @return
        *   java.net.URL with swapped protocol
        */
      def swapProtocol(url: URL): URL = {
        val urlArr: Array[String] = url.toString.split("://")
        val proto: String = urlArr(0)
        val updatedUrl: String = proto match {
          case "http" => s"https://" + urlArr(1)
          case "https" => s"http://" + urlArr(1)
          case _ => {
            throw new Exception("unknown protocol")
          }
        }
        new URL(updatedUrl)
      }
      // connect
      val conn: java.net.URLConnection = tryProtocolSwap match {
        case false => url.openConnection()
        case true => swapProtocol(url).openConnection()
      }
      // get response from header
      val header: java.util.Map[String, java.util.List[String]] =
        conn.getHeaderFields()
      val responseCode: String = header.size match {
        case 0 => "404"
        case _ => header.get(null).get(0)
      }
      (conn, responseCode)
    }

    // try initial url
    val maybeUrl: Try[URL] = Try {
      new URL(inputUrlString)
    }
    // if success return url, otherwise add https as guessed protocol
    val inputUrl: URL = maybeUrl match {
      case Success(u) => u
      case Failure(_) => {
        new URL(s"https://" + inputUrlString)
      }
    }

    val responseCode = getResponseCode(inputUrl)

    //
    val outputConn: Option[URLConnection] =
      responseCode._2.contains("200") match {
        case true => Some(responseCode._1)
        case false => {
          responseCode._2.contains("301") match {
            case true => {
              val swappedResponseCode = getResponseCode(inputUrl, true)
              swappedResponseCode._2.contains("200") match {
                case true => Some(swappedResponseCode._1)
                case false => None
              }
            }
            case false => None
          }
        }
      }
    outputConn
  }

  val conn: Option[URLConnection] = urlConnectionChecker(inputUrlString)
}
