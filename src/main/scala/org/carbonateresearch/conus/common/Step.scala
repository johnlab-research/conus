package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.{Grid, GridFactory}

case class Step(stepNumber:Int, coordinates:Seq[Int], grid:Grid, stepErrors:String, stepOffset:Int=0){
  def - (offset:Int):Step = Step(this.stepNumber,this.coordinates,this.grid,this.stepErrors,offset)
  def i: Step = Step(this.stepNumber,this.coordinates,this.grid,this.stepErrors,this.stepNumber)
}

object Step {
  def apply[T](k:ModelVariable[T],v:T):Step = {
    val sr = StepResultsWithData(k,v)
    val mv = ModelVariable[Int]("Dummy",0)
    val varMap = Map(mv.asInstanceOf[CalculationParametersIOLabels]->0)
    val grid = GridFactory(Seq(1),1,varMap)
    Step(0,Seq(0),grid,"")
  }
}
