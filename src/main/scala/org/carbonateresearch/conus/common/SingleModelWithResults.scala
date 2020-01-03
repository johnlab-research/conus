package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.calculationparameters.CalculationStepValue
import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels
import scala.compat.Platform.EOL

final case class SingleModelWithResults(ID:Int, steps: List[Int], parameters: List[CalculationStepValue], results: ModelResults){

   def valuesForAllSteps(label:CalculationParametersIOLabels): Map[Int,Double] = steps.map(s => (s,results.resultsForStep(s).valueForLabel(label))).toMap

   def valueForStep(label: CalculationParametersIOLabels, step:Int): Double = results.resultsForStep(step).valueForLabel(label)

   def finalResult: Map[CalculationParametersIOLabels, Double] =  results.resultsForStep(steps.size-1).valuesForAllLabels

   def resultsPerLabel: Map[CalculationParametersIOLabels,Map[Int, Double]] = {
      val IOLabels:List[CalculationParametersIOLabels] = results.theseResults.last._2.valuesForAllLabels.map(x => x._1).toList
        IOLabels.map(l => (l,steps.map(s => (s,results.resultsForStep(s).valuesForAllLabels(l))).toMap)).toMap
   }

   def formatNumbers(precision:Int, value:Double, culprit:String): String = {

      val pValue:Double =  1+precision.toDouble/10

      val formatString:String = "%"+ pValue.toString + "f"

      formatString format value
   }

   def summary: String = {
      val step = steps.last
      "Model # " + ID + " ->" + results.resultsForStep(step).valuesForAllLabels.map(k =>  k._1.toString + ": " + formatNumbers(k._1.precision,k._2,k._1.toString) + k._1.unit + " | ").foldLeft("| ")(_+_)
   }

   override def toString: String = {

      "Model # " + ID + " :" + EOL + steps.map(step => "Step: "+ step + results.resultsForStep(step).valuesForAllLabels.map(k => k._1.toString + ": " + k._2.toString + " | " ).foldLeft(" -> ")(_+_) + EOL).foldLeft(EOL)(_+_)
   }
}
