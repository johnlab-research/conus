package org.carbonateresearch.conus.common

case class Step(stepNumber:Int, totalSteps: Int, currentResults:SingleModelResults, stepErrors:String,stepOffset:Int=0){
  def - (offset:Int):Step = Step(this.stepNumber,this.totalSteps,this.currentResults,this.stepErrors,offset * -1)
  def + (offset:Int):Step = Step(this.stepNumber,this.totalSteps,this.currentResults,this.stepErrors,offset)
}

object Step {
  def apply[T](k:ModelVariable[T],v:T):Step = {
    val sr = StepResultsWithData(k,v)
    val modelResults = SingleModelResults(Map(0->sr))
    Step(0,1,modelResults,"")
  }
}
