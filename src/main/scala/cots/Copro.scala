package cots

enum Copro[T <: Tuple]{
  case InjL[H, T <: Tuple](h : H) extends Copro[H *: T]
  case InjR[H, T <: Tuple](t : Copro[T]) extends Copro[H *: T] 
}

