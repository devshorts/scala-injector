import Modules.{SourceModule}
import com.google.inject.Guice
import play.api.GlobalSettings

object Global extends GlobalSettings {

  val kernel = Guice.createInjector(new SourceModule())

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    kernel.getInstance(controllerClass)
  }
}
