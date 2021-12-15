package floating

import japgolly.scalajs.react._
import floatingui.floatingUiReactDom.mod
import floatingui.floatingUiReactDom.anon.OmitPartialComputePositio
import floatingui.floatingUiReactDom.srcMod.UseFloatingReturn

object HooksApiExt {
  val hook1  =
    CustomHook.unchecked[OmitPartialComputePositio, UseFloatingReturn] { pos =>
      println(pos.placement); mod.useFloating()
    }
  val jsHook = CustomHook.unchecked[OmitPartialComputePositio, UseFloatingReturn] { pos =>
    val res = mod.useFloating(pos)
    // res.update()
    res
  }
  val hook =
    // CustomHook.unsafe
    CustomHook[OmitPartialComputePositio]
      // .withHooks[Unit]
      // .useCallback(())
      // .useCallback((pos: OmitPartialComputePositio) => mod.useFloating(pos))
      // .useCallbackWithDeps[OmitPartialComputePositio, UseFloatingReturn](pos)(pos =>
      //   mod.useFloating(pos)
      // )
      // println(pos.placement); mod.useFloating(pos)
      .buildReturning { pos =>
        println(pos.placement);
        val res = mod.useFloating(pos)
        println("======")
        println(res.placement)
        // res.update()
        println(res.placement)
        println("---")
        res
      }
  // .buildReturning((p, v) => v.value)

  sealed class Primary[Ctx, Step <: HooksApi.AbstractStep](api: HooksApi.Primary[Ctx, Step]) {

    final def useFloating(pos: OmitPartialComputePositio)(implicit
      step:                    Step
    ): step.Next[UseFloatingReturn] =
      useFloatingBy(_ => pos)

    final def useFloatingBy(pos: Ctx => OmitPartialComputePositio)(implicit
      step:                      Step
    ): step.Next[UseFloatingReturn] =
      // api.customBy(ctx => mod.useFloating(pos(ctx)))
      api.customBy(ctx => jsHook(pos(ctx)))
  }

  final class Secondary[Ctx, CtxFn[_], Step <: HooksApi.SubsequentStep[Ctx, CtxFn]](
    api: HooksApi.Secondary[Ctx, CtxFn, Step]
  ) extends Primary[Ctx, Step](api) {

    def useFloatingBy(pos: CtxFn[OmitPartialComputePositio])(implicit
      step:                Step
    ): step.Next[UseFloatingReturn] =
      useFloatingBy(step.squash(pos)(_))
  }
}

trait HooksApiExt {
  import HooksApiExt._

  implicit def hooksExtFloating1[Ctx, Step <: HooksApi.AbstractStep](
    api: HooksApi.Primary[Ctx, Step]
  ): Primary[Ctx, Step] =
    new Primary(api)

  implicit def hooksExtFloating2[Ctx, CtxFn[_], Step <: HooksApi.SubsequentStep[Ctx, CtxFn]](
    api: HooksApi.Secondary[Ctx, CtxFn, Step]
  ): Secondary[Ctx, CtxFn, Step] =
    new Secondary(api)
}

object implicits extends HooksApiExt
