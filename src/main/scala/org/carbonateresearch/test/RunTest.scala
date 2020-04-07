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

object RunTest extends App {

  val a = ModelVariable[Double](name = "To be calculated",defaultValue = 0.0)
  val b = ModelVariable[Int](name = "Double1",defaultValue = 0)
  val c = ModelVariable[Int](name = "Double2",defaultValue = 0)
  val sampleDepth = ModelVariable[Double](name = "Double3", defaultValue=0.0, ifNoValue = ReturnSpecificValue(0.0))
  val me: Int = 3
  val e = ModelVariable[Int](name = "String",defaultValue = 0)
  val s = ModelVariable[String](name = "String", defaultValue = "name")

  //implicit val data:Map[Step,HMap[BiMapIS]] = Map(0 -> HMap[BiMapIS](a -> 3.3), 1 -> HMap[BiMapIS](a -> 4.3))

  implicit def whatABunchOfBakwas[T](v:ModelVariable[T]):T = v.defaultValue

  val expression:StepFunction[Double] = (s:Step) => 2 + a(s) + a(s) + sampleDepth(s)
  val expr2:StepFunction[Double] = (s:Step) => 2+a(s) + e(s)
  val expr5:StepFunction[List[Double]] = (s:Step) => List(2+a(s), e(s))

  val expr3:StepFunction[Double] = (s:Step) => a(s)
  val expr4:StepFunction[Double]=expression
  val depthOfSample:StepFunction[Double] = (s:Step)=> 2 + a(s) + e(s)




  val bof: Step => Double = 2+a(_)-3

  val test = ApplyStepFunction((s:Step)=> (2 + a(s) + a(s)).toDouble) storeResultAs sampleDepth

  val hmapa = HMap[BiMapIS](a -> 3.3, e -> 4)
  val hmapb =  HMap[BiMapIS](b -> 3)
  val hmapd: HMap[BiMapIS] = hmapa + (b -> 3)
  val fakeResults = NumericalResults(Map(0 -> StepResults(hmapa)))
  val fakeStep = Step(1,fakeResults,"")

  println(depthOfSample(fakeStep-2))

  val stupidMap: Map[CalculationParametersIOLabels,Any] = Map(a->3.2,b->2,c->3,s->"String")

  def spitMe[T](mv: ModelVariable[T]):T = {
    stupidMap(mv).asInstanceOf[T]
  }

  def spitMe2[T](mv: ModelVariable[T]):T = {

    stupidMap(mv) match{
      case v:T => v
      case _ => mv.defaultValue
    }
  }

 val testV:Int = spitMe2(a).toInt
 println(testV)
}
