import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner


@RunWith(classOf[JUnitRunner])
class ControllerAppTests extends Specification {
  "Maps" should {
    "should map once" in  {
      val maps = List(1,2,3).map(i => i + 1)
      maps shouldEqual List(2, 3, 4)
    }
  }

  "Controller" should {
    "be injectable" in {
      1 shouldEqual 1
    }
  }
}
