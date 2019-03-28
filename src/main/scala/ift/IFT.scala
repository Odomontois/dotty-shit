// package ift

// trait X[F[_]]{ def x(): F[Int] }
// trait Y[F[_]]{ def y(): F[String] }
// trait Z[F[_]]{ def z(): F[Double] }

// type XX[F[_], A] = given (X[F]) => A
// type YY[F[_], A] = given (Y[F]) => A
// type ZZ[F[_], A] = given (Z[F]) => A


// type &~&[U <: [_[_], _] => Any, V <:[_[_], _] => Any] = [F[_], A] => U[F, V[F, A]] 

// type XY = XX &~& YY
// type YX = YY &~& XX
// type XXY = XX &~& XY
// type XYX = XX &~& YX
// type XZY = XX &~& ZZ &~& YY
// type YZX  = YY &~& ZZ &~& XX 

// type YXP[F[_], A] = given (X[F], Y[F]) => A
// type XYZP[F[_], A] = given (X[F], Y[F], Z[F]) => A
// type XYXP[F[_], A] = given (X[F], Y[F], X[F]) => A

// def weak1[F[_], A](xy: XX[F, A]): XY[F, A] = xy
// def weak2[F[_], A](xy: XX[F, A]): YX[F, A] = xy

// def exchange1[F[_], A](xy: XY[F, A]): YX[F, A] = xy 
// def exchange2[F[_], A](xzy: XZY[F, A]): YZX[F, A] = xzy  

// def contract1[F[_], A](xxy: XXY[F, A]): XY[F, A] = xxy
// def contract2[F[_], A](xyx: XYX[F, A]): XY[F, A] = xyx 

// def unabiguit[F[_], A](xy: XY[F, A]): XXY[F, A] = xy

// def curry1[F[_], A](xy: XY[F, A]): YXP[F, A] = xy
// def curry2[F[_], A](xzy: XZY[F, A]): XYZP[F, A] = xzy

// def uncurry1[F[_], A](xy: YXP[F, A]): XY[F, A] = xy
// def uncurry2[F[_], A](xyz: XYZP[F, A]): XZY[F, A] = xyz


// def x[F[_]] given(xf: X[F]): F[Int] = xf.x()
// def y[F[_]] given(yf: Y[F]): F[String] = yf.y()
// def z[F[_]] given(zf: Z[F]): F[Double] = zf.z()

// object IFT{
//   def main(args: Array[String]) = {
//       val xy1: XY[List, List[(String, Int)]] = y[List] zip x[List]
//       val xy2: XXY[List, List[(String, Int)]] = y[List] zip x[List]

//       assert(xy1 given (() => List(1)) given (() => List("one")) == List(("one", 1)))
//       assert(xy2 given (() => List(1)) given (() => List(2)) given (() => List("one")) == List(("one", 2)))
//   }
// }