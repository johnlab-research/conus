package org.carbonateresearch.test

import shapeless.syntax.std.function._
import shapeless.ops.function._
import shapeless.HMap
import org.carbonateresearch.conus.Implicits.BiMapIS
import org.carbonateresearch.test.implicits._
import org.carbonateresearch.conus.util.StepFunctionUtils._

object RunTest extends App {

  val a = ModelVariable[Double](name = "To be calculated")
  val b = ModelVariable[Int](name = "Double1",data=Map(0->1,1->2))
  val c = ModelVariable[String](name = "Double2")
  val sampleDepth = ModelVariable[Double](name = "Double3",data=Map(0->1.0,1->2.0))
  val me: Int = 3
  val e = ModelVariable[Int](name = "String",data=Map(0->1,1->2))
  val s = ModelVariable[String](name = "String")

  type leftSideOfEquation[T] = T

  val expression:StepFunction[Double] = (s:Step) => 2 + b(s) + e(s) + sampleDepth(s)
  val expr2 = 2+b(_:Int) + e(_:Int)
  val expr3:StepFunction[Int] = b(_)
  val expr4:StepFunction[Double]=expression
  val depthOfSample:StepFunction[Double] = (s:Step)=> 2 + b(s) + e(s)

  println(expression(0))

  val bof: Step => Int = 2+b(_)-3

  val test = ApplyStepFunction((s:Step)=> (2 + b(s) + e(s)).toDouble) storeResultAs sampleDepth


  implicit val intToString = new BiMapIS[Int, String]
  implicit val stringToInt = new BiMapIS[String, Int]
 implicit def ModifyMyImplicit[T](myVar:ModelVariable[T]):BiMapIS[ModelVariable[T],T] = myVar.implicitBiMapIS


  val rando = HMap[BiMapIS](1->2,2->"foo", a -> 3.3)

 println(rando.get(1) ++ rando.get(a))

}
