package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.calculationparameters.CalculationStepValue
import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels
import spire.math.Number

import scala.compat.Platform.EOL

final case class SingleModelWithResults(ID:Int, steps: List[Number], parameters: List[CalculationStepValue], results: Map[Number, Map[CalculationParametersIOLabels,Number]]){

   def valuesForAllSteps(label:CalculationParametersIOLabels): Map[Number,Number] = steps.map(s => (s,results(s)(label))).toMap

   def valueForStep(label: CalculationParametersIOLabels, step:Number): Number = results(step)(label)

   def finalResult: Map[CalculationParametersIOLabels, Number] =  results(Number(steps.size-1))

   def resultsPerLabel: Map[CalculationParametersIOLabels,Map[Number, Number]] = {
      val IOLabels:Map[CalculationParametersIOLabels,Number] = results.last._2
        IOLabels.map(l => (l._1,steps.map(s => (s,results(s)(l._1))).toMap))
   }

   def formatNumbers(precision:Int, value:Number, culprit:String): String = {

      val pValue:Double =  1+precision.toDouble/10

      val formatString:String = "%"+ pValue.toString + "f"

      formatString format value.toDouble
   }

   def summary: String = {
      val step = steps.last
      //k._2.toString f"$pi%1.5f"
      "Model # " + ID + " ->" + results(step).map(k =>  k._1.toString + ": " + formatNumbers(k._1.precision,k._2,k._1.toString) + k._1.unit + " | ").foldLeft("| ")(_+_)
   }

   override def toString: String = {

      "Model # " + ID + " :" + EOL + steps.map(step => "Step: "+ step + results(step).map(k => k._1.toString + ": " + k._2.toString + " | " ).foldLeft(" -> ")(_+_) + EOL).foldLeft(EOL)(_+_)
   }
}
