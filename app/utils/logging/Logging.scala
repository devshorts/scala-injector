package utils.logging

import play.api.mvc.{RequestHeader, Request, AnyContent}
import play.api.{Logger, LoggerLike}

class Log(logger: LoggerLike) {

  def getMessage(s: String, data: LogData): String = {
    if (data != null) {
      return s + " " + data.asLog
    }

    s
  }

  def info(s: String, m: LogData = null, t: Throwable = null) = logger.info(getMessage(s, m), t)

  def debug(s: String, m: LogData = null, t: Throwable = null) = logger.debug(getMessage(s, m), t)

  def warn(s: String, m: LogData = null, t: Throwable = null) = logger.warn(getMessage(s, m), t)

  def error(s: String, m: LogData = null, t: Throwable = null) = logger.error(getMessage(s, m))

}

object Log {
  def apply(src : Class[_]) = {
    new Log(Logger(src))
  }

  def apply(name : String) = new Log(Logger(name))
}

class LogData(data: Map[String, String]) {

  protected val logMap = data

  def asLog = logMap.map({ case(k, v) => s"$k=$v" }).mkString("; ")

  def and(tup: (String, Any)) = {
    val (x, y) = tup
    new LogData(logMap.updated(x, y.toString))
  }

  def and(other: LogData) = {
    new LogData(logMap ++ other.logMap)
  }
}

object With {
  def apply(tup: (String, Any)*) = {
    new LogData(tup.map(i => (i._1, i._2.toString)).toMap)
  }
}