import java.util.UUID

import Modules.SourceModule
import com.google.inject.Guice
import org.slf4j.MDC
import play.api.mvc._
import utils.logging.{Log, With}
import dispatchers.CorrIdDispatcher.Implicits._
import scala.concurrent.Future

object LoggingFilter extends Filter {
  val logger = Log(getClass)

  override def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    val start = System.currentTimeMillis()

    MDC.put("corrId", UUID.randomUUID().toString)

    val logInfo = With(
      "request-uri" -> rh.uri
    )

    logger.info("starting", logInfo)

    val result = f(rh)

    result.map(r => {
      logger.info("completing", logInfo and With(
        "request-time-ms" -> (System.currentTimeMillis() - start)
      ))

      MDC.remove("corrId")

      r
    })
  }
}

object Global extends WithFilters(LoggingFilter) {

  val logger = Log("global")

  val kernel = Guice.createInjector(new SourceModule())

  override def onRequestReceived(request: RequestHeader): (RequestHeader, Handler) = {
    super.onRequestReceived(request)
  }

  override def onRequestCompletion(request: RequestHeader): Unit = {
    super.onRequestCompletion(request)
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    kernel.getInstance(controllerClass)
  }
}
