package org.carbonateresearch.domainespecific.Geology
import org.carbonateresearch.conus.util.CommonFunctions.{interpolatedValue, scaleFromMinToMaxByStep}
import org.carbonateresearch.conus.common.{ModelVariable, Step}
import org.carbonateresearch.conus.util.StepFunctionUtils.StepFunction

object GeneralGeology {
  val depth = ModelVariable[Double]("Burial depth",0," m", precision = 1)
  val age  = ModelVariable[Double]("Age",0," Ma", precision = 3)
  val surfaceTemperature  = ModelVariable[Double]("Surface Temperature",0,"˚C", precision =1)
  val burialTemperature = ModelVariable[Double]("Burial Temperature",0,"˚C", precision =1)
  val geothermalGradient = ModelVariable[Double]("Geothermal gradient",25,"˚C/km", precision =1)

  def ageOfStep[T](oldestAge: T, youngestAge:T)(implicit num:Fractional[T]):StepFunction[T] = {
    scaleFromMinToMaxByStep(youngestAge,oldestAge)}

  def ageOfStep[T](oldestAge: ModelVariable[T], youngestAge:ModelVariable[T])(implicit num:Fractional[T]):StepFunction[T] = {
    (s:Step) => (scaleFromMinToMaxByStep(youngestAge(s.i),oldestAge(s.i))).apply(s)}

  def burialDepthFromAgeModel[T](age:ModelVariable[T], ageModel:List[(T,T)])(implicit num:Fractional[T]):StepFunction[T] =  interpolatedValue(age,ageModel)

  def burialTemperatureFromGeothermalGradient[T](surfaceTemperature:ModelVariable[T],
                                                 depth:ModelVariable[T],
                                                 geothermalGradient:ModelVariable[T])
                                                (implicit num:Fractional[T]):StepFunction[T] =  {
    (s:Step) => num.plus(num.times(depth(s),num.div(geothermalGradient(s),1000.0.asInstanceOf[T])),surfaceTemperature(s))
  }

  def geothermalGradientAtAge[T](age:ModelVariable[T],
                                       geothermalGradientsAgeMap:List[(T, T)])
                                      (implicit num:Fractional[T]):StepFunction[T] = {
  interpolatedValue(age, xyList = geothermalGradientsAgeMap)}

  def surfaceTemperaturesAtAge[T](age:ModelVariable[T],
                                  surfaceTemperatureAgeMap:List[(T, T)])
                                 (implicit num:Fractional[T]):StepFunction[T] = {
        interpolatedValue(age, surfaceTemperatureAgeMap)
      }
}
