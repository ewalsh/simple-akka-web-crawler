import com.edmundwalsh.connection.UrlConnectionChecker
import com.edmundwalsh.web.WebPageParser.{calcPageRank, checkIfVisited}
import com.edmundwalsh.model.PageRank
import java.net.URLConnection

import scala.collection.mutable.Queue
// import akka.actor.ActorSystem

object SimpleAkkaWebCrawler extends App {

  // preallocate
  val ansQueue = Queue[PageRank]()
  var linkQueue = Queue[URLConnection]()
  val alreadyVisited = Queue[String]()

  // parse website urls from first argument
  // can be comma separated to start with multiple sites
  val inputLinkArr = args(0).split(",")
  val maybeLinks: Array[Option[URLConnection]] = inputLinkArr.map(x => {
    new UrlConnectionChecker(x).conn
  })

  // add these initial urls to a queue if valid
  linkQueue ++= maybeLinks.filter(_ != None).map(_.get).distinct

  // get max depth
  val maxDepth: Int = args(1).toInt

  /** recursive depth function that continues to build page rank until max depth
    * is reached
    * @param currentDepth
    *   a integer to indicate current depth
    * @param maxDepth
    *   a integer to indicate the maximum depth we should go
    * @param currentLinkQueue
    *   A queue of links to evaluate
    * @param currentAnsQueue
    *   Our queue of page rank answer we are building up over each recursive
    *   loop
    * @param pagesVisited
    *   A queue of running pages that have been visited
    * @return
    *   A Queue of our PageRank data model and final answer
    */
  def depthRecursion(
      currentDepth: Int,
      maxDepth: Int,
      currentLinkQueue: Queue[URLConnection],
      currentAnsQueue: Queue[PageRank],
      pagesVisited: Queue[String]
  ): Queue[PageRank] = {

    // continue if we haven't hit max depth or return current answers
    (currentDepth > maxDepth) match {
      case true => currentAnsQueue
      case false => {
        // mapping over current queue of links
        val maybeResult: Queue[Option[(PageRank, Array[String])]] =
          currentLinkQueue.map(lnk => {
            // checking if they have been visited
            checkIfVisited(lnk, pagesVisited) match {
              case true => None
              case false => Some(calcPageRank(lnk, currentDepth))
            }
          })
        // only look/evaluate results we have not looked at before
        val results: Queue[(com.edmundwalsh.model.PageRank, Array[String])] =
          maybeResult.filter(_ != None).map(_.get)
        // adding to queues
        currentAnsQueue ++= results.map(_._1)
        pagesVisited ++= results.map(_._1.url.toString)
        // reset link queue
        var nextLinkQueue = Queue[URLConnection]()
        nextLinkQueue = results
          .flatMap(_._2)
          .distinct
          .map(x => {
            new UrlConnectionChecker(x).conn
          })
          .filter(_ != None)
          .map(_.get)
          .distinct

        // for testing -- dumping urls into console
        // nextLinkQueue.map(x => {
        //   println(x.toString)
        // })

        // start next recursive loop
        depthRecursion(
          currentDepth + 1,
          maxDepth,
          nextLinkQueue,
          currentAnsQueue,
          pagesVisited
        )
      }
    }

  }

  // handle optional connection if original link was faulty
  linkQueue.length match {
    case 0 =>
      println("This url doesn't look right, please check and try again")
    case _ => {

      // val system = ActorSystem("Akka Web Crawler")
      // val ac = system.actorOf(Props[])

      val ans =
        depthRecursion(1, maxDepth, linkQueue, ansQueue, alreadyVisited)
      println("url\tdepth\tratio")
      ans.map(x => println(x.toTSV))
    }
  }
}
