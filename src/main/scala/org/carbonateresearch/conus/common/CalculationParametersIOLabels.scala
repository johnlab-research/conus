package org.carbonateresearch.conus.common

trait CalculationParametersIOLabels{
  override def toString: String = {
    val fullString = this.getClass.getSimpleName
  fullString.take(fullString.size-1)}
  def unit: String = ""
  def value = 0.0
  def precision:Int = 2
}
case object NumberOfSteps extends CalculationParametersIOLabels {
  override def toString: String = "Number of Steps"
override def precision = 0}

case object Depth extends CalculationParametersIOLabels {override def toString: String = "Depth"}

final case class Parameter(name: String, unitName:String = "", defaultValue:Option[Number] = None,
                           silent: Boolean = false,
                           override val precision:Int = 2) extends CalculationParametersIOLabels {
  override def toString: String = name
  override def unit: String = unitName

}
