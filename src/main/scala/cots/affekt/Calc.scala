package manatki.data.eval
import cats.{MonadError, StackSafeMonad}
import cats.kernel.Monoid
import cats.syntax.either._
import scala.language.implicitConversions

enum Calc[R, S, E, A] {
  final def run(r: R, init: S): (S, Either[E, A])               = Calc.run(this, r, init)
  final def runEmpty(r: R)(implicit S: Monoid[S])               = run(r, Monoid.empty[S])
  final def runEmptyUnit(implicit ev: Unit <:< R, S: Monoid[S]) = runEmpty(())
  final def runUnit(init: S)(implicit ev: Unit <:< R)           = run((), init)

  case Pure(a: A)                            
  case Read[R, S, E]()                                 extends Calc[R, S, E, R]
  case Get[R, S, E]()                                  extends Calc[R, S, E, S]
  case Set[R, S, E](s: S)                              extends Calc[R, S, E, Unit]
  case Raise(e: E)                          
  case Defer(e: () => Calc[R, S, E, A])
  case Cont[R, S, E, A, B](
      src: Calc[R, S, E, A],
      ksuc: A => Calc[R, S, E, B],
      kerr: E => Calc[R, S, E, B]
  ) extends Calc[R, S, E, B]
}

object Calc {
  def update[R, S, E](f: S => S): Calc[R, S, E, Unit] = Get().flatMap(s => Set(f(s)))
  def defer[R, S, E, A](x: => Calc[R, S, E, A])         = Defer(() => x)
  def delay[S, A](x: => A): Calc[Any, S, Nothing, A]    = defer(Pure(x))

  def write[R, S, E](s: S)(implicit S: Monoid[S]): Calc[R, S, E, Unit] = update(S.combine(_, s))


  implicit class invariantOps[R, S, E, A](val calc: Calc[R, S, E, A]) extends AnyVal {
    def cont[B](f: A => Calc[R, S, E, B], h: E => Calc[R, S, E, B]): Calc[R, S, E, B] = Cont(calc, f, h)
    def flatMap[B](f: A => Calc[R, S, E, B]): Calc[R, S, E, B]                        = cont(f, Raise(_))
    def handleWith(f: E => Calc[R, S, E, A]): Calc[R, S, E, A]                        = cont(Pure(_), f)
    def handle(f: E => A): Calc[R, S, E, A]                                           = handleWith(e => Pure(f(e)))
    def map[B](f: A => B): Calc[R, S, E, B]                                           = flatMap(a => Pure(f(a)))
  }

  def run[R, S, E, A](calc: Calc[R, S, E, A], r: R, init: S): (S, Either[E, A]) =
    calc match {
      case Pure(a)     => (init, Right(a))
      case Read()      => (init, Right(r))
      case Get()       => (init, Right(init))
      case Set(s)      => (s, Right(()))
      case Raise(e)    => (init, Left(e))
      case Defer(f)    => run(f(), r, init)
      case Cont(src, ks, ke) =>
        src match {
          case Pure(a)     => run(ks(a), r, init)
          case Read()      => run(ks(r), r, init)
          case Get()       => run(ks(init), r, init)
          case Set(s)      => run(ks(()), r, s)
          case Raise(e)    => run(ke(e), r, init)
          case Defer(f)    => run(f().cont(ks, ke), r, init)
          case Cont(src1, ks1, ke1) =>
            run(src1.cont(a => ks1(a).cont(ks, ke), e => ke1(e).cont(ks, ke)), r, init)
        }
    }

  implicit def calcInstance[R, S, E]: CalcFunctorInstance[R, S, E] = new CalcFunctorInstance[R, S, E]

  class CalcFunctorInstance[R, S, E]
      extends MonadError[[A] => Calc[R, S, E, A], E] with cats.Defer[[A] => Calc[R, S, E, A]] with StackSafeMonad[[A] => Calc[R, S, E, A]] {
    def defer[A](fa: => Calc[R, S, E, A]): Calc[R, S, E, A]                                  = Calc.defer(fa)
    def raiseError[A](e: E): Calc[R, S, E, A]                                                = Calc.Raise(e)
    def handleErrorWith[A](fa: Calc[R, S, E, A])(f: E => Calc[R, S, E, A]): Calc[R, S, E, A] = fa.handleWith(f)
    def flatMap[A, B](fa: Calc[R, S, E, A])(f: A => Calc[R, S, E, B]): Calc[R, S, E, B]      = fa.flatMap(f)
    def pure[A](x: A): Calc[R, S, E, A]                                                      = Calc.Pure(x)
  }
}
