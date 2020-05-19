package org.carbonateresearch.conus.common

class SteppedModel (nbSteps:Int, modelName:String="no name",gridGeometry:Seq[Int]=Seq(1))  {
  val prepareSteps:  List[Int] = (0 until nbSteps+1).toList

  def setGrid(gridDimensions:Int*):SteppedModel = {
    new SteppedModel(nbSteps,modelName,gridDimensions)
  }

  def defineMathematicalModelPerCell(calculationList: Calculator*): SteppedModelWithCalculations = {
    val mathematicalModel:List[Calculator] = calculationList.toList
println("this far")
    SteppedModelWithCalculations(nbSteps:Int, modelName:String, gridGeometry:Seq[Int],mathematicalModel:List[Calculator])
  }


}

