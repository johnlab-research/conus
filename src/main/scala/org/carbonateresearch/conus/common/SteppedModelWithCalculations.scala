/*
 * Copyright © 2020 by Cédric John.
 *
 * This file is part of CoNuS.
 *
 * CoNuS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * CoNuS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CoNuS. If not, see <http://www.gnu.org/licenses/>.
 */

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
    val sizeOfModels = nestedList.map(ic => ic.size).foldLeft(1)(_ * _)
    println(s"A total of $sizeOfModels unique models were defined, attempting to create a list now.")
   val initConditions:List[List[InitialCondition]] = combineListOfLists(nestedList)
    println("Models list successfully created.")
    val fullInitialValues = initConditions.map(ic => List(InitialCondition(NumberOfSteps,List((nbSteps,Seq())))) ++ ic)
    val singleModels:List[SingleModel] = createListOfModels(fullInitialValues)

    ModelCalculationSpace(models = singleModels,modelName)
  }

  private def createListOfModels(initialValues:List[List[InitialCondition]]):List[SingleModel] = {

    val modelIDs:Seq[Int] = initialValues.indices

    modelIDs.map(i => {
      SingleModel(ID = i+1,nbSteps=nbSteps,gridGeometry = gridGeometry,calculations=mathematicalModel,initialConditions=initialValues(i))
    }).toList
  }
}
