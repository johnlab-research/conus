package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels

case class SingleStepResults(valuesPerLabel:Map[CalculationParametersIOLabels,Double]){
  def retrieveValue(label:CalculationParametersIOLabels):Double = valuesPerLabel(label)
  def addValue(label:CalculationParametersIOLabels, newValue:Double):SingleStepResults = SingleStepResults(valuesPerLabel++Map((label,newValue)))
}

object SingleStepResults {
  def apply(nonDoubleValuesPerLabel:Map[CalculationParametersIOLabels,AnyVal]){
    new SingleStepResults(nonDoubleValuesPerLabel.map(x => (x._1,(x._2).asInstanceOf[Double])))
  }
}