// package com.edmundwalsh.akka
//
// import akka.actor.{ActorSystem, Props, Actor}
// import akka.actor.{Props, ActorRef, ActorLogging}
// import com.edmundwalsh.model.PageRank
//
// import scala.collection.mutable.Queue
//
// object PageRankParser {
//   case class AkkaQueryInput(url: String, depth: Int)
// }
//
// class PageRankParser(
//     linkQueue: Queue[String],
//     ansQueue: Queue[PageRank],
//     alreadyVisited: Queue[String]
// ) extends Actor
//     with ActorLogging {
//   //
//   import PageRankParser.AkkaQueryInput
//   // create actors to handle each partition of link in queue
//   val numPartitions = linkQueue.length
//   var outputArr: Option[PageRank] = None
//   val partitionActors: Array[ActorRef] = new Array[ActorRef](numPartitions)
//
//   //
//   (0 to (numPartitions - 1)).foreach({ case (id) =>
//     partitionActors(id) = context.actorOf(
//       Props(
//         new PageRankWorker(
//           id,
//           linkQueue,
//           ansQueue,
//           alreadyVisited
//         )
//       )
//     )
//   })
//
//   //
//   log.info("Worker actors created")
//
//   var workersNotFinished = numPartitions
//
//   //
//   def receive = {
//     case AkkaQueryInput(url, depth) => {
//       partitionActors.foreach(_ ! AkkaQueryInput(url, depth))
//       context.become(waitForWorkers)
//     }
//     case _ => throw new Exception("Error in query input")
//   }
//
//   //
//   def waitForWorkers: Receive = {
//     case PageRank(url, depth, ratio) => {
//       log.info(s"worker for ${url} search results received by master")
//       workersNotFinished -= 1
//       //
//       log.info(s"${workersNotFinished} workers still working...")
//       ansQueue += PageRank(url, depth, ratio)
//       if (workersNotFinished == 0) {
//         log.info("All results computed")
//
//         val numberOfSites = ansQueue.length
//         val avgRatio = ansQueue.map(_.ratio).sum / ansQueue.length.toDouble
//
//         log.info("The crawlers stats are:")
//         log.info(s"the number of links the app has crawled = ${numberOfSites}")
//         log.info(s"at a depth = ${depth}")
//         log.info(s"with an average page rank = ${avgRatio}")
//
//         val outputQueue = Some(ansQueue)
//
//         context.parent ! outputQueue
//         context.unbecome()
//       }
//     }
//     case _ => throw new Exception("worker sent something other than PageRank ")
//   }
//
//   def results: Queue[PageRank] = {
//     outputArr match {
//       case None => throw new Exception("No results exist")
//       case Some(out) => {
//         ansQueue += out
//       }
//     }
//   }
//
// }
