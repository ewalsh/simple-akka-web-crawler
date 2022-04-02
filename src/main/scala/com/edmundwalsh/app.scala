import com.edmundwalsh.connection.UrlConnectionChecker
import java.net.URLConnection

object SimpleAkkaWebCrawler extends App {
  override def main(args: Array[String]): Unit = {
    // parse website urls from first argument
    val link: Option[URLConnection] = new UrlConnectionChecker(args(0)).conn
    // val urlConn = link.get
    // val stream: InputStream = urlConn.getInputStream
    // val sourced = Source.fromInputStream(stream)
  }
}
