package org.carbonateresearch.conus.calculationparameters
import spire.math._
import org.carbonateresearch.conus.calculationparameters.parametersIO._
import scala.compat.Platform.EOL


final case class CalculationResults(steps: List[Number], parameters: List[CalculationParameters], results: Map[Number, Map[CalculationParametersIOLabels,Number]]){

   def valuesForAllSteps(label:CalculationParametersIOLabels): Map[Number,Number] = steps.map(s => (s,results(s)(label))).toMap

   def valueForStep(label: CalculationParametersIOLabels, step:Number): Number = results(step)(label)

   def finalResult: Map[CalculationParametersIOLabels, Number] =  results(Number(steps.size-1))

   def resultsPerLabel: Map[CalculationParametersIOLabels,Map[Number, Number]] = {
      val IOLabels:Map[CalculationParametersIOLabels,Number] = results.last._2
        IOLabels.map(l => (l._1,steps.map(s => (s,results(s)(l._1))).toMap))

   }

   def summary: String = {
      val step = steps.last
      results(step).map(k => k._1.toString + ": " + k._2.toString + " | ").foldLeft("| ")(_+_)
   }

   override def toString: String = {

      steps.map(step => "Step: "+ step + results(step).map(k => k._1.toString + ": " + k._2.toString + " | " ).foldLeft(" -> ")(_+_) + EOL).foldLeft(EOL)(_+_)

   }
}

