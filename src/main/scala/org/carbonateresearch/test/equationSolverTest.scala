package org.carbonateresearch.test
import org.carbonateresearch.conus.common.BiMapIS
import org.carbonateresearch.conus.common.ModelVariable
import org.carbonateresearch.conus.util.StepFunctionUtils._
import org.carbonateresearch.conus.common.Step
import shapeless.HMap
import shapeless._

case object equationSolverTest {
  def apply[T](eq: StepFunction[T], myMap: Map[Step, HMap[BiMapIS]]): String = {
    //implicit val thisNewWeirdMap = myMap
    //implicit def whatABunchOfBakwas[T](v:ModelVariable[T]):T = v.apply(0, myMap)

   "None"
  }
}
