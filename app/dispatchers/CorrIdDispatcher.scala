package dispatchers

import org.slf4j.MDC
import play.api.mvc.{Result, Request, ActionBuilder}

import scala.concurrent.{ExecutionContextExecutor, ExecutionContext, Future}

class CorrIdDispatcher (origCtx : java.util.Map[_,_], executor : ExecutionContext) extends ExecutionContextExecutor{
  override def execute(runnable: Runnable): Unit = {
    executor.execute(new Runnable {
      override def run(): Unit = {
        val prev = MDC.getCopyOfContextMap

        setMdc(origCtx)

        try {
          runnable.run()
        }
        finally {
          setMdc(prev)
        }
      }
    })
  }

  private def setMdc(m : java.util.Map[_,_]) = {
    if(m == null){
      MDC.clear()
    }
    else{
      MDC.setContextMap(m)
    }
  }

  override def reportFailure(cause: Throwable) = executor.reportFailure(cause)
}

object CorrIdDispatcher {
  object Implicits {
    implicit def defaultContext: ExecutionContext = {
      CorrIdDispatcher.executionContext
    }
  }

  def executionContext : ExecutionContext =
    new CorrIdDispatcher(MDC.getCopyOfContextMap, play.api.libs.concurrent.Execution.defaultContext)

}

object Actionable extends ActionBuilder[Request] {
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    block(request)
  }

  override protected def executionContext: ExecutionContext = {
    CorrIdDispatcher.executionContext
  }
}

