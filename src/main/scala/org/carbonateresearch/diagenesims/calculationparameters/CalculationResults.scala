package org.carbonateresearch.diagenesims.calculationparameters
import spire.math._
import scala.compat.Platform.EOL


final case class CalculationResults(steps: List[Number], parameters: List[CalculationParameters], results: Map[String, Map[Number,Number]]){
   def values(label:String): Map[Number,Number] = results(label)
   def valueForStep(label: String, step:Number): Number = values(label)(step)

   def resultsPerStep: Map[Number,Map[String, Number]] = {
      steps.map(s => (s,results.map(r => (r._1, r._2(s))))).toMap

   }

   override def toString: String = {

      steps.map(step => "Step: "+ step.toString + resultsPerStep(step).map(k => k._1.toString + ": " + k._2.toString + " | " ).foldLeft(" -> ")(_+_) + EOL).foldLeft(EOL)(_+_)
   }
}

