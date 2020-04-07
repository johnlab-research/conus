package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.equations.parametersIO.CalculationParametersIOLabels
import shapeless.{HMap,HList}

import scala.compat.Platform.EOL

final case class SingleModelWithResults(ID:Int, steps: List[Int], parameters: List[Calculator], results: NumericalResults){

   def valuesForAllSteps[T](label:ModelVariable[T]): Map[Int,T] = steps.map(s => (s,results.getStepResult(s,label))).toMap

   def valueForStep[T](label: ModelVariable[T], step:Int): T = results.getStepResult(step,label)

   def finalResult: HMap[BiMapIS] =  results.resultsForStep(steps.size-1).dataContainer

   def resultForVariable[T](variable: ModelVariable[T]):Map[Int,T] = {
      val resultsSeq:List[T] = steps.map(s => results.resultsForStep(s).get(variable).get)
      steps.zip(resultsSeq).toMap
   }

  /* def resultsPerLabel[T]: Map[ModelVariable[T],Map[Int, T]] = {
      val lastStepResults:HMap[BiMapIS] = results.resultsForStep(results.currentResults.size-1).dataContainer
      val allVariables = lastStepResults.
      val IOLabels:List[ModelVariable[T]] = results.currentResults.last._2.dataContainer.map(x => x._1).toList
        IOLabels.map(l => (l,steps.map(s => (s,results.resultsForStep(s).valuesForAllLabels(l))).toMap)).toMap
   }*/

   def formatNumbers(precision:Int, value:Double, culprit:String): String = {

      val pValue:Double =  1+precision.toDouble/10

      val formatString:String = "%"+ pValue.toString + "f"

      formatString format value
   }

   def summary: String = {
      val step = steps.last
      "Model # " + ID + " ->" //+ results.currentResults.map(k =>  k._1.toString + ": " + formatNumbers(k._1.precision,k._2,k._1.toString) + k._1.unit + " | ").foldLeft("| ")(_+_)
   }

   override def toString: String = {

      "Model # " + ID + " :" //+ EOL + steps.map(step => "Step: "+ step + results.resultsForStep(step).valuesForAllLabels.map(k => k._1.toString + ": " + k._2.toString + " | " ).foldLeft(" -> ")(_+_) + EOL).foldLeft(EOL)(_+_)
   }
}
