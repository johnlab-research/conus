package org.carbonateresearch.conus.common

case class Step(stepNumber:Int, currentResults:NumericalResults,stepErrors:String){
  def - (offset:Int):Step = Step(this.stepNumber-offset,this.currentResults,this.stepErrors)
  def + (offset:Int):Step = Step(this.stepNumber-offset,this.currentResults,this.stepErrors)
}
