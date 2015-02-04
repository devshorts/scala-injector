package controllers

import javax.inject.{Inject, Singleton}

import annotations.TestAnnotation
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.{Injector, Key}
import dispatchers.Actionable
import dispatchers.CorrIdDispatcher.Implicits._
import play.api.data.validation.ValidationError
import play.api.libs.json.{JsPath, Json}
import play.api.mvc._
import utils.logging.Log

import scala.concurrent.Future


trait Sourceable {
  val kernelSource: Injector

  def WithSource[T](clazz: Class[T])(f: ((T, Request[AnyContent]) => Result)): Action[AnyContent] = {
    Actionable { request => {

      val binder =
        request.getQueryString(sourceableQueryParamToggle) match {
          case Some(_) => kernelSource.getInstance(Key.get(clazz, classOf[TestAnnotation]))
          case None => kernelSource.getInstance(clazz)
        }

      f(binder, request)
    }
    }
  }

  def sourceableQueryParamToggle = "test"
}

trait AppUtils {
  def errResponse(errs: Seq[(JsPath, Seq[ValidationError])]) = {
    Json.obj("foo" -> "bar") //Json.toJson(errs.map(i => Json.obj(i._1.path.toString() -> Json.arr(i._2.map(j => j.message)))))
  }

  def pojoJson[T](item: T) = new ObjectMapper().writeValueAsString(item)
}

trait DataSource {
  def get: String
}

class ProdSource extends DataSource {
  override def get: String = "prod"
}

class TestSource extends DataSource {
  override def get: String = "test"
}

class Db {
  val logger = Log("DataSource")

  val f: Future[String] = {
    Future({
      logger.info("async future string")

      "async future string"
    })
  }
}

@Singleton
class Application @Inject()(kernel: Injector) extends Controller with AppUtils with Sourceable {

  override val kernelSource: Injector = kernel

  val logger = Log("bar")

  override def sourceableQueryParamToggle = "bar"

  def index = Actionable.async { request =>

    logger info "index hit"

    new Db().f.map(Ok(_))
  }

  def getName(name: String) = WithSource(classOf[DataSource]) { (provider, request) => {
    val result = name + ": " + provider.get

    logger.info("processed request")

    Ok(result)
  }
  }
}
