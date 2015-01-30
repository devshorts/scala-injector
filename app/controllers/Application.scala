package controllers

import javax.inject.{Singleton, Inject}

import annotations.TestAnnotation
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.{Key, Injector}
import play.api.data.validation.{ValidationError, ValidationResult}
import play.api.libs.json.{JsPath, JsError, Json}
import actionExtensions.Actions._
import play.api.mvc._

trait Sourceable{
  val kernelSource : Injector

  def WithSource[T] (clazz : Class[T]) (f: ((T, Request[AnyContent]) => Result)) : Action[AnyContent] = {
    Action { request => {
      val binder =
        request.getQueryString(sourceableQueryParamToggle) match {
          case Some(_) => kernelSource.getInstance(Key.get(clazz, classOf[TestAnnotation]))
          case None => kernelSource.getInstance(clazz)
        }

      f(binder, request)
    }}
  }

  def sourceableQueryParamToggle = "test"
}

trait AppUtils{
  def errResponse(errs: Seq[(JsPath, Seq[ValidationError])]) = {
    Json.obj("foo" -> "bar") //Json.toJson(errs.map(i => Json.obj(i._1.path.toString() -> Json.arr(i._2.map(j => j.message)))))
  }

  def pojoJson[T](item : T) = new ObjectMapper().writeValueAsString(item)
}

trait DataSource{
  def get : String
}

class ProdSource extends DataSource{
  override def get: String = "prod"
}

class TestSource extends DataSource {
  override def get : String = "test"
}

@Singleton
class Application @Inject() (kernel : Injector) extends Controller with AppUtils with Sourceable {

  override val kernelSource: Injector = kernel

  override def sourceableQueryParamToggle = "bar"

  def index = Action { request =>
    Ok(views.html.index("Your new application is ready."))
  }

  def binding(name : String) = WithSource(classOf[DataSource]){ (provider, request) =>
  {
    val result = name + ": " + provider.get

    Ok(result)
  }}
}