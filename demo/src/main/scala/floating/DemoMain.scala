package floating.demo

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom
import floating.HooksApiExt
import floating.implicits._
import floatingui.floatingUiReactDom.anon.OmitPartialComputePositio
import floatingui.floatingUiCore.typesMod.Placement

import scala.scalajs.js
import scala.scalajs.js.|
import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom.html

import js.annotation._

@JSExportTopLevel("DemoMain")
object DemoMain {
  val component =
    ScalaFnComponent
      .withHooks[Unit]
      // .useMemo(())(_ => Ref.toVdom[html.Div])
      // .customBy(pos => HooksApiExt.jsHook(pos))
      .useFloating(OmitPartialComputePositio().setPlacement(Placement.top))
      .render { (p, h) =>
        // println($.hook1.update())
        // println($.hook1.floating())
        // println($.hook1.strategy)
        println(h.reference())
        println(h.refs.reference)
        val refRef = Ref.fromJs(h.refs.reference.asInstanceOf[facade.React.RefHandle[html.Div | Null]])
        val floatRef = Ref.fromJs(h.refs.floating.asInstanceOf[facade.React.RefHandle[html.Div | Null]])
        // val m: Int = (h.refs.reference)
        // println($.hook1.x)
        val r = <.div(^.untypedRef := refRef, "Example")
        val f = <.div("Float")
        // val f = <.div(^.untypedRef := floatRef, "Float")
        println(s"Plac ${h.placement} ${h.x}")
        r
        // <.div("Example")
        <.div(r, f)
      }

  @JSExport
  def main(): Unit = {

    val container = Option(dom.document.getElementById("root")).getOrElse {
      val elem = dom.document.createElement("div")
      elem.id = "root"
      dom.document.body.appendChild(elem)
      elem
    }

    <.div(
    component()
  ).renderIntoDOM(container)
    ()
  }
}
