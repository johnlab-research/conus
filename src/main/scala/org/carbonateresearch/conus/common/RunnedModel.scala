package org.carbonateresearch.conus.common

import java.lang.System.lineSeparator

final case class RunnedModel(ID:Int, steps: List[Int], parameters: List[Calculator], results: SingleModelResults){

   def valuesForAllSteps[T](label:ModelVariable[T]): Map[Int,Option[T]] = steps.map(s => (s,results.getStepResult(s,label))).toMap

   def valueForStep[T](label: ModelVariable[T], step:Int): T = results.getStepResult(step,label).getOrElse(label.defaultValue)

   def finalResult: StepResults =  results.resultsForStep(steps.size-1)

   def resultForVariable[T](variable: ModelVariable[T]):Map[Int,T] = {
      val resultsSeq:List[T] = steps.map(s => results.resultsForStep(s).get(variable).get)
      steps.zip(resultsSeq).toMap
   }

  def resultsPerLabel: ResultsPerModelVariable = {
    results.resultsPerLabel
   }

   def formatNumbers(precision:Int, value:Double): String = {

      val pValue:Double =  1+precision.toDouble/10

      val formatString:String = "%"+ pValue.toString + "f"

      formatString format value
   }

   def summary: String = {
      val step = steps.last
      results.resultsForStep(step).printAllStepValues + lineSeparator
   }

   override def toString: String = {

      "Model # " + ID + " :" + lineSeparator + results.printAllModelValues + lineSeparator
   }
}
