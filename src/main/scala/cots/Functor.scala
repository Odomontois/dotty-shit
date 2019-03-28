package cots

trait Functor[F[_]]{
  def (fa: F[A]) map[A, B] (f: A => B): F[B]

  def (f: A => B) <@> [A, B](fa: F[A]): F[B] = fa.map(f)
}

trait Semigroupal[F[_]]{
  def (fa: F[A]) map2[A, B, C](fb: F[B])(f: (A, B) => C): F[C]

  def (fa: F[A]) zip2[A, B, C](fb: F[B]): F[(A, B)] = fa.map2(fb)((_, _))

  def (fab: F[A => B]) ap[A, B](fb: F[A]): F[B] = fab.map2(fb)(_ apply _)
}

trait Pointed[F[_]]{
  def (a: A) pure[A]: F[A]

  final val unit: F[Unit] = ().pure
}

trait Apply[F[_]] extends Semigroupal[F] with Functor[F]

trait Cartesian[F[_]] extends Apply[F] with Pointed[F]{
  override def (fa: F[A]) map[A, B](f: A => B) = fa.map2(unit)((a, _) => f(a))  
}


object Cots{
  val t3 = (1, "lol", true)
  // val (x, y) = t3
  // val (u, v) = y

  def main(args: Array[String]): Unit = {
    // List(t3, x, y, u, v).foreach(println)
  }
}