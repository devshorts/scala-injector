package actionExtensions

import play.api.mvc._
import play.api.mvc.BodyParsers._

object Actions {

  def parser = parse.using (request => parse.anyContent)

  def Authenticated(f: (Integer, Request[AnyContent]) => Result) = {
    Action(parser) { request =>  f(1, request)  }
  }
}
