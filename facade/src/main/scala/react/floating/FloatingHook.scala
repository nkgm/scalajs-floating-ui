package floating

import japgolly.scalajs.react._

object HooksApiExt {
// TODO: Replace
val hook = CustomHook[String]
  .useEffectOnMountBy(name => Callback.log(s"HELLO $name"))
  .buildReturning(name => name)
  sealed class Primary[Ctx, Step <: HooksApi.AbstractStep](api: HooksApi.Primary[Ctx, Step]) {

    // TODO: Change hook name, input args/type(s), and output type
    final def useFloating(name: String)(implicit step: Step): step.Next[String] =
      // TODO: Change hook name
      useFloatingBy(_ => name)

    // TODO: Change hook name, input args/type(s), and output type
    final def useFloatingBy(name: Ctx => String)(implicit step: Step): step.Next[String] =
      api.customBy(ctx => hook(name(ctx)))
  }

  final class Secondary[Ctx, CtxFn[_], Step <: HooksApi.SubsequentStep[Ctx, CtxFn]](api: HooksApi.Secondary[Ctx, CtxFn, Step]) extends Primary[Ctx, Step](api) {

    // TODO: Change hook name, input args/type(s), and output type
    def useFloatingBy(name: CtxFn[String])(implicit step: Step): step.Next[String] =
      // TODO: Change hook name, squash each parameter
      // useFloatingBy(step.squash(arg1)(_), step.squash(arg2)(_), ...)
      useFloatingBy(step.squash(name)(_))
  }
}

trait HooksApiExt {
  import HooksApiExt._

  // TODO: Change hook name so that it won't conflict with other custom hooks
  implicit def hooksExtFloating1[Ctx, Step <: HooksApi.AbstractStep](api: HooksApi.Primary[Ctx, Step]): Primary[Ctx, Step] =
    new Primary(api)

  // TODO: Change hook name so that it won't conflict with other custom hooks
  implicit def hooksExtFloating2[Ctx, CtxFn[_], Step <: HooksApi.SubsequentStep[Ctx, CtxFn]](api: HooksApi.Secondary[Ctx, CtxFn, Step]): Secondary[Ctx, CtxFn, Step] =
    new Secondary(api)
}

object Implicits extends HooksApiExt
