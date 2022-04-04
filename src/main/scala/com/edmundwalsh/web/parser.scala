package com.edmundwalsh.web

import java.net.URLConnection
import java.net.URI
import java.io.InputStream
import scala.io.Source
import scala.util.{Try, Success, Failure}
import scala.collection.mutable.Queue

import org.jsoup.nodes.Document
import org.jsoup.Jsoup

import com.edmundwalsh.model.PageRank

object WebPageParser {

  /** function to check if page was already visited
    * @param url
    *   URLConnection of website to check
    * @param alreadyVisited
    *   a queue of website strings we have already visisted
    * @return
    *   A Boolean of true/false if the site has been visited
    */
  def checkIfVisited(
      url: URLConnection,
      alreadyVisited: Queue[String]
  ): Boolean = {
    alreadyVisited.length match {
      case 0 => false
      case _ =>
        alreadyVisited.contains(
          url.toString.replace(
            "sun.net.www.protocol.https.DelegateHttpsURLConnection:",
            ""
          )
        )
    }
  }

  /** simple boolean to int transform
    * @param in
    *   Boolean
    * @return
    *   either 0 or 1
    */
  def oneIfTrue(in: Boolean): Int = {
    in match {
      case true => 1
      case false => 0
    }
  }

  /** main function to calculate page rank and return list of hrefs found in a
    * tags from website
    * @param urlConn
    *   a URLConnection to a website to crawl and calc page rank from
    * @param depth
    *   the current depth of the algorithm to include in results
    * @return
    *   A tuple of PageRank for top level page and list of all href links
    */
  def calcPageRank(
      urlConn: URLConnection,
      depth: Int
  ): (PageRank, Array[String]) = {
    // get input stream and translate to string
    val stream: InputStream = urlConn.getInputStream
    val sourced = Source.fromInputStream(stream, "ISO-8859-1")
    val str = sourced.mkString
    // use Jsoup to parse and make elements easily selectable
    val doc = Jsoup.parse(str)
    val elems = doc.getElementsByTag("a")
    val baseURL = urlConn.getURL()
    val baseURLstr = baseURL.toString
    val allHrefs = elems.eachAttr("href").toArray.map(_.toString)
    val absHrefs = elems.eachAttr("abs:href").toArray.map(_.toString)
    val relHrefs = allHrefs.diff(absHrefs)
    // combine relative and absolute hrefs
    val outputHrefs = Array
      .concat(
        absHrefs,
        relHrefs.map(x => {
          baseURL + x
        })
      )
      .map(x => {
        Try {
          new URI(x) // trying to convert a string to a URI
        }
      })
      .filter(_.isFailure == false) // this can fail from special characters
      .map(_.get) // getting successful URIs
      .map(x => {
        val proto = x.toString.split(":")(0)
        val tryHost = Try { x.getHost.toString }
        // returning protocol and the host
        (proto, tryHost)
      })
      .filter(_._2.isSuccess)
      .map(x => {
        // reform with protocol
        x._1 + "://" + x._2.get
      })

    // calculated page rank ratio
    val ratio = outputHrefs
      .map(x => oneIfTrue(x == baseURLstr))
      .sum
      .toDouble / outputHrefs.length.toDouble

    // output
    (PageRank(baseURLstr, depth, ratio), outputHrefs.distinct)
  }
}
