val x = (1, "lol", 3)
val y = (2, "cat")

val z  =  x ++ y

x.getClass.getDeclaredFields



// inline def tuplexxx(inline n: Int) <: Tuple = {
//   if (n == 0) ()
//   else n *: tuplexxx(n - 1)
// }

// tuplexxx(100)

// import reflect._

// def gen[A] given (g: Generic[A]): g.type = g

// val x = (1, "lol", 2)

// val gggg = gen[(Int, String, Int)]

// type yyy = gggg.Shape

// val u = gggg.reflect(x)

// u.adtClass

// u.elementLabel(2)