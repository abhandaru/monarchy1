package monarchy.util.macros

package x1.util

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

trait Enum[E] {
  def values: Set[E] = macro Enum.values_impl[E]
}

object Enum {
  def values_impl[A: c.WeakTypeTag](c: Context) = {
    import c.universe._

    val symbol = weakTypeOf[A].typeSymbol

    if (!symbol.isClass) c.abort(
      c.enclosingPosition,
      "Can only enumerate values of a sealed trait or class.",
    )
    else if (!symbol.asClass.isSealed) c.abort(
      c.enclosingPosition,
      "Can only enumerate values of a sealed trait or class.",
    )
    else {
      val tpe = symbol.asClass
      // Needed because of scala core bug
      // See: https://github.com/scala/bug/issues/7588
      tpe.typeSignature
      val children = tpe.knownDirectSubclasses.toList

      if (!children.forall(_.isModuleClass)) c.abort(
        c.enclosingPosition,
        "All children must be objects.",
      )
      else c.Expr[Set[A]] {
        def sourceModuleRef(sym: Symbol) = Ident(
          sym.asInstanceOf[scala.reflect.internal.Symbols#Symbol]
            .sourceModule
            .asInstanceOf[Symbol]
        )
        Apply(
          Select(reify(Set).tree, TermName("apply")),
          children.map(sourceModuleRef(_)),
        )
      }
    }
  }
}
