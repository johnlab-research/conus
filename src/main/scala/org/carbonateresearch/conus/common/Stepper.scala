package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.calculationparameters.{CalculationParameters, InitializeValues, Initializer}
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, NumberOfSteps}
import spire.math._
import spire.algebra._
import spire.implicits._

final case class Stepper (nbSteps:Int)  {

  val prepareSteps:  List[Number] = (0 to nbSteps).toList.map(x => Number(x))

  def +(parameter: CalculationParameters): ModelCalculationSpace = ModelCalculationSpace(List(ChainableCalculation(1,prepareSteps, List(parameter))))


  def +(initializer: InitializeValues): ModelCalculationSpace = {

    val initialValues = initializer.ModelCalculationSpace
    val modelsList:List[ChainableCalculation] = initialValues.map(x => ChainableCalculation(initialValues.indexOf(x)+1, prepareSteps, List(Initializer(x))))

    ModelCalculationSpace(modelsList)}

}
