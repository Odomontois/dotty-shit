trait FunctionK[F[_], G[_]]{
  def apply[A](fa: F[A]): G[A]
}

trait TCtx{
  type T
}

object FunctionK{
  def apply[F[_], G[_]](f: (erased ctx: TCtx) => F[ctx.T] => G[ctx.T]): FunctionK[F, G] = 
    new FunctionK[F, G]{ 
        def apply[A](fa: F[A]): G[A] = {
          val tt: TCtx{type T = A} = new TCtx{ println("fuck"); type T = A }
          f(tt)(fa)
        }
    }
}

val headOption: FunctionK[List, Option] = FunctionK(_ => _.headOption)


class Foo 

def foo(f: (erased Int) => Int): Int = {
  val ctx = 1
  f(ctx)
}



object FK{
  def main(args: Array[String]) = println(headOption(List(1)))
}