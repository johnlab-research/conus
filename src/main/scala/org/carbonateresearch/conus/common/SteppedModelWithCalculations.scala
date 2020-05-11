package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.{Grid,GridFactory}
import org.carbonateresearch.conus.util.CommonModelVariables.NumberOfSteps

case class SteppedModelWithCalculations(nbSteps:Int,
                                        gridGeometry:Seq[Int],
                                        mathematicalModel:List[Calculator]) extends Combinatorial {

  def defineInitialModelConditions(optionsPerVariables: InitialConditions*): ModelCalculationSpace = {
   val initConditions:List[List[InitialCondition]] = combineListOfLists(optionsPerVariables.map(x => x.conditions).toList)
    val fullInitialValues = initConditions.map(ic => List(InitialCondition(NumberOfSteps,nbSteps,Seq())) ++ ic)
    val singleModels:List[SingleModel] = createInitialGriddedModels(fullInitialValues)

    ModelCalculationSpace(models = singleModels)
  }

  private def createInitialGriddedModels(initialValues:List[List[InitialCondition]]):List[SingleModel] = {

    val variableMap:Map[CalculationParametersIOLabels,Int] = defineVariableMap(initialValues)
    val grids:List[Grid] = initialValues.map(iv => {
      val theGrid = GridFactory(gridGeometry, nbSteps, variableMap)
      val defaultValues = variableMap.map(v => (v._1,v._1.defaultValue)).unzip
      theGrid.initializeGrid(defaultValues._1.toSeq,defaultValues._2.toSeq)
      iv.foreach(ic => theGrid.setAtCell(ic.variable,ic.value,ic.coordinates)(0))
     theGrid
    })
    grids.map(g => SingleModel(ID = grids.indexOf(g),nbSteps=nbSteps,grid = g,calculations=mathematicalModel,initialConditions=initialValues(grids.indexOf(g))))
  }

  private def defineVariableMap(initialValues:List[List[InitialCondition]]):Map[CalculationParametersIOLabels,Int] = {
    val labelsForInitialization:List[CalculationParametersIOLabels] = initialValues.head.map(x => x.variable)
    val labelsForCalculations:List[CalculationParametersIOLabels] = mathematicalModel.map(x => x.outputs)
    val allParameters:List[CalculationParametersIOLabels] = (labelsForInitialization ++ labelsForCalculations).distinct

  (allParameters.indices).map(index => (allParameters(index),index)).toMap

  }
}
