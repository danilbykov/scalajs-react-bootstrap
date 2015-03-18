package com.acework.js.components.bootstrap

import com.acework.js.components.bootstrap.Utils._
import japgolly.scalajs.react.Addons.ReactCloneWithProps
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.js
import scala.scalajs.js._

/**
 * Created by weiyin on 10/03/15.
 */
object ProgressBar {

  case class Props(
                    min: UndefOr[Double] = 0,
                    now: UndefOr[Double] = undefined,
                    max: UndefOr[Double] = 100,
                    label: UndefOr[ReactNode] = undefined,
                    srOnly: UndefOr[Boolean] = undefined,
                    striped: UndefOr[Boolean] = undefined,
                    active: UndefOr[Boolean] = undefined,
                    isChild: UndefOr[Boolean] = undefined,
                    interpolateClass: UndefOr[ReactNode] = undefined,
                    bsClass: UndefOr[Classes.Value] = Classes.`progress-bar`,
                    bsStyle: UndefOr[Styles.Value] = Styles.default,
                    bsSize: UndefOr[Sizes.Value] = undefined,
                    addClasses: String = "") extends BsProps

  val Progressbar = ReactComponentB[Props]("Progressbar")
    .render((P, C) => {

    def getPercentage(now: Double, min: Double, max: Double): Double =
      Math.ceil((now - min) / (max - min) * 100)

    def renderLabel(percentage: Double): ReactNode = {
      if (P.interpolateClass.isDefined) {
        //val interpolateClass = P.interpolateClass.get.reactTag
        // TODO P.interpolateClass
        ""
      }
      else
        Interpolate(Interpolate.Props(now = P.now, min = P.min, max = P.max, percent = percentage,
          bsStyle = P.bsStyle), P.label.get)
    }

    def renderScreenReaderOnlyLabel(label: ReactNode) = {
      <.span(^.className := "sr-only", label)
    }

    def renderChildBar(child: ReactNode, index: Int) = {
      val keyAndRef = getChildKeyAndRef(child, index)
      ReactCloneWithProps(child, keyAndRef ++ Map[String, js.Any]("isChild" -> true))
    }

    def renderProgressBar() = {
      val percentage = getPercentage(P.now.getOrElse(0), P.min.getOrElse(0), P.max.getOrElse(100))
      var label: ReactNode = if (React.isValidElement(P.label))
        renderLabel(percentage)
      else
        P.label.getOrElse("")

      if (P.srOnly.getOrElse(false))
        label = renderScreenReaderOnlyLabel(label)

      // FIXME spread props
      <.div(^.classSet1M(P.addClasses, P.bsClassSet), ^.role := "progpressbar",
        ^.width := s"$percentage%", ^.aria.valuenow := P.now, ^.aria.valuemin := P.min, ^.aria.valuemax := P.max,
        label
      )
    }

    var classes = Map("progress" -> true)

    if (P.active.getOrElse(false)) {
      classes += ("progress-striped" -> true)
      classes += ("active" -> true)
    } else if (P.striped.getOrElse(false))
      classes += ("progress-striped" -> true)

    if (!ValidComponentChildren.hasValidComponents(C)) {
      if (!P.isChild.getOrElse(false)) {
        // FIXME spread props
        <.div(^.classSet1M(P.addClasses, classes),
          renderProgressBar()
        )
      }
      else
        renderProgressBar()
    }
    else
      <.div(^.classSet1M(P.addClasses, classes),
        ValidComponentChildren.map(C, renderChildBar)
      )

  }).build

  def withKey(key: js.Any) = Progressbar.withKey(key)

  def withRef(ref: String) = Progressbar.withRef(ref)

  def apply(props: Props, children: ReactNode*) = Progressbar(props, children)

  def apply(children: ReactNode*) = Progressbar(Props(), children)
}