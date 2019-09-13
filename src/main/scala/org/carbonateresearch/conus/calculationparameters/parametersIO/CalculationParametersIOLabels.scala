package org.carbonateresearch.conus.calculationparameters.parametersIO
import spire.math.{Number}

trait CalculationParametersIOLabels{
  override def toString: String = {
    val fullString = this.getClass.getSimpleName
  fullString.take(fullString.size-1)}
  def unit: String = ""
}
case object NumberOfSteps extends CalculationParametersIOLabels {override def toString: String = "Number of Steps"}
case object Depth extends CalculationParametersIOLabels {override def toString: String = "Depth"}
case object Age extends CalculationParametersIOLabels {override def toString: String = "Age"}
case object SurfaceTemperature extends CalculationParametersIOLabels {override def toString: String = "Surface Temperature"}
case object BurialTemperature extends CalculationParametersIOLabels {override def toString: String = "Burial Temperature"}
case object GeothermalGradient extends CalculationParametersIOLabels {override def toString: String = "Geothermal Gradient"}
case object Doubler extends CalculationParametersIOLabels {override def toString: String = "My Doubled"}

final case class Parameter(name: String, unitName:String = "", defaultValue:Option[Number] = None) extends CalculationParametersIOLabels {
  override def toString: String = name
  override def unit: String = unitName
}