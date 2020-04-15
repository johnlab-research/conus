package org.carbonateresearch.test

import shapeless.syntax.std.function._
import shapeless.ops.function._
import shapeless.HMap
import shapeless._
import syntax.singleton._
import record._
import org.carbonateresearch.conus.common._
import shapeless.labelled.FieldType
import shapeless.syntax.SingletonOps
//import org.carbonateresearch.conus.Implicits.GenericModelVariableTtoTImplicit
//import org.carbonateresearch.conus.Implicits.GenericStepToBiMapTImplicit
import org.carbonateresearch.conus.util.Implicits._
import org.carbonateresearch.conus.util.StepFunctionUtils._
import org.carbonateresearch.conus.common.{ModelVariable, Step, ApplyStepFunction}
import org.carbonateresearch.conus.util.CommonFunctions.interpolatedValue

object RunTest extends App {

  val a = ModelVariable[Double](name = "To be calculated",defaultValue = 0.0)
  val b = ModelVariable[Int](name = "Double1",defaultValue = 0)
  val c = ModelVariable[Int](name = "Double2",defaultValue = 0)

  val value:Double = -2
  val valueList = List((1.0,3.0),(2.0,4.0))
  val step = Step(a,value)
  val myEquation:StepFunction[Double] =interpolatedValue(a,valueList)
  println(myEquation(step))

}
