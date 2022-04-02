import com.edmundwalsh.utils.UtilFuncs.urlConnectionChecker

object SimpleAkkaWebCrawler extends App {
  override def main(args: Array[String]): Unit = {
    // parse website urls from first argument
    val output: String = urlConnectionChecker(args(0))
    println(output)
  }
}
