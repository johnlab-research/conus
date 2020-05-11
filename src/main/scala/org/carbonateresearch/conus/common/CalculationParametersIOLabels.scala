package org.carbonateresearch.conus.common

trait CalculationParametersIOLabels{
  override def toString = ""
  val silent: Boolean
  val name:String
  def formatValueAsString(value:Any):String
  def labelToValueFormattedString(v:Any):String ={
    if(!silent){
    val myString = formatValueAsString(v)
    this.name + ": " + myString + unitName + " | " }
    else {""}
  }
  def unitName: String = ""
  def value = 0.0
  def precision:Int = 2
  def defaultValue:Any
}

