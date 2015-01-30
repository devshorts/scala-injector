package Modules

import annotations.TestAnnotation
import com.google.inject.AbstractModule
import controllers.{DataSource, ProdSource, TestSource}

class SourceModule extends AbstractModule {

  override def configure(): Unit = {
    testable(classOf[DataSource], classOf[ProdSource], classOf[TestSource])
  }

  def testable[TInterface, TMain <: TInterface, TDev <: TInterface](
               interface: Class[TInterface],
               main:      Class[TMain],
               test:      Class[TDev]) = {
    val markerClass = classOf[TestAnnotation]

    bind(interface).to(main)

    bind(interface) annotatedWith markerClass to test
  }
}
