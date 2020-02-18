package org.carbonateresearch.test

import org.carbonateresearch.test.implicits._

object RunTest extends App {

  val a = Parameter[Double](name = "To be calculated")
  val b = Parameter[Int](name = "Double1",data=Map(0->1,1->2))
  val c = Parameter[String](name = "Double2")
  val d = Parameter[Double](name = "Double3",data=Map(0->1.0,1->2.0))
  val me: Int = 3
  val e = Parameter[Int](name = "String",data=Map(0->1,1->2))
  val s = Parameter[String](name = "String")

  type Step=Int
  val expression = (s:Step) => 2 + b(s) + e(s) + d(s)
  val expr2 = 2+b(_:Int) + e(_:Int)
  val expr3: Step => Int = b(_)

  println(expression(0))

  val bof: Step => Int = 2+b(_)-3

  val test = ApplyEquation(bof).storeResultAs(b)

}
