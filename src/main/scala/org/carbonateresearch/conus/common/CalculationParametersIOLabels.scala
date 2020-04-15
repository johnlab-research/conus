package org.carbonateresearch.conus.common

trait CalculationParametersIOLabels{
  override def toString = ""
  val name:String
  def getValueAsString(value:Any):String
  def labelToValueFormattedString(v:Any):String ={
    val myString = getValueAsString(v)
    this.name + ": " + myString + " | "
  }
  def unit: String = ""
  def value = 0.0
  def precision:Int = 2
}


final case class Parameter(name: String, unitName:String = "", defaultValue:Option[Number] = None,
                           silent: Boolean = false,
                           override val precision:Int = 2) extends CalculationParametersIOLabels {
  override def toString: String = name
  override def getValueAsString(value:Any):String = "oh well"
  override def unit: String = unitName

}
