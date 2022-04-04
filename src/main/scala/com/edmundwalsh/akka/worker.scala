// package com.edmundwalsh.akka
//
// import com.edmundwalsh.model.PageRank
// import com.edmundwalsh.web.WebPageParser.calcPageRank
// import com.edmundwalsh.akka.PageRankParser.AkkaQueryInput
// import com.edmundwalsh.connection.UrlConnectionChecker
//
// import akka.actor.{Actor, ActorLogging}
//
// import scala.collection.mutable.Queue
// import java.net.URLConnection
//
// class PageRankWorker(
//     id: Int,
//     linkQueue: Queue[String],
//     ansQueue: Queue[PageRank],
//     alreadyVisited: Queue[String]
// ) extends Actor
//     with ActorLogging {
//
//   def receive = {
//     // url connection needed new query object
//     case AkkaQueryInput(url, depth) => {
//       //
//       log.info(s"worker running on url = ${url}")
//       // check if valid url
//       val maybeLink: Option[URLConnection] = new UrlConnectionChecker(
//         url
//       ).conn
//       //
//       maybeLink match {
//         case Some(l) => {
//           val calcResults: (PageRank, Array[String]) = calcPageRank(l, depth)
//
//           log.info(s"worker with url = ${url} analyzed page rank")
//           sender() ! calcResults._1
//         }
//         case None => {
//           throw new Exception("a url wasn't valid")
//         }
//       }
//
//     }
//   }
// }
