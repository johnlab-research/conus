package org.carbonateresearch.conus.calculationparameters.parametersIO
import spire.math.{Number}

trait CalculationParametersIOLabels{
  override def toString: String = {
    val fullString = this.getClass.getSimpleName
  fullString.take(fullString.size-1)}
  def unit: String = ""
  def value = 0.0
}
case object NumberOfSteps extends CalculationParametersIOLabels {override def toString: String = "Number of Steps"}
case object Depth extends CalculationParametersIOLabels {override def toString: String = "Depth"}

final case class Parameter(name: String, unitName:String = "", defaultValue:Option[Number] = None,
                           silent: Boolean = false,
                           precision:Int=2) extends CalculationParametersIOLabels {
  override def toString: String = name
  override def unit: String = unitName
}
