package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.{Grid,GridFactory,GridValueDescriptor}
import org.carbonateresearch.conus.util.CommonModelVariables.NumberOfSteps

case class SteppedModelWithCalculations(nbSteps:Int,
                                        modelName:String,
                                        gridGeometry:Seq[Int],
                                        mathematicalModel:List[Calculator]) extends Combinatorial {

  def defineInitialModelConditions(optionsPerVariables: GridValueDescriptor*): ModelCalculationSpace = {
    val nestedList:List[List[InitialCondition]] = optionsPerVariables.map(op => {
       op.setOfValues.map(s => InitialCondition(op.variableName,s))}).toList

   val initConditions:List[List[InitialCondition]] = combineListOfLists(nestedList)
    val fullInitialValues = initConditions.map(ic => List(InitialCondition(NumberOfSteps,List((nbSteps,Seq())))) ++ ic)
    val singleModels:List[SingleModel] = createInitialGriddedModels(fullInitialValues)

    ModelCalculationSpace(models = singleModels,modelName)
  }

  private def createInitialGriddedModels(initialValues:List[List[InitialCondition]]):List[SingleModel] = {

    val variableMap:Map[CalculationParametersIOLabels,Int] = defineVariableMap(initialValues)
    val grids:List[Grid] = initialValues.map(iv => {
      val theGrid = GridFactory(gridGeometry, nbSteps, variableMap)
      val defaultValues = variableMap.map(v => (v._1,v._1.defaultValue)).unzip
      theGrid.initializeGrid(defaultValues._1.toSeq,defaultValues._2.toSeq)
      iv.foreach(ic => ic.values.foreach(icv =>
        theGrid.setAtCell(ic.variable,icv._1,icv._2)(0)))
     theGrid
    })
    grids.indices.map(i => {
      val g =grids(i)
      SingleModel(ID = i+1,nbSteps=nbSteps,grid = g,calculations=mathematicalModel,initialConditions=initialValues(i))
    }).toList
  }

  private def defineVariableMap(initialValues:List[List[InitialCondition]]):Map[CalculationParametersIOLabels,Int] = {
    val labelsForInitialization:List[CalculationParametersIOLabels] = initialValues.head.map(x => x.variable)
    val labelsForCalculations:List[CalculationParametersIOLabels] = mathematicalModel.map(x => x.outputs)
    val allParameters:List[CalculationParametersIOLabels] = (labelsForInitialization ++ labelsForCalculations).distinct

  (allParameters.indices).map(index => (allParameters(index),index)).toMap

  }
}
