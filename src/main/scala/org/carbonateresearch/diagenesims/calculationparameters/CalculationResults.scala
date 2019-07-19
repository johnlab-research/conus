package org.carbonateresearch.diagenesims.calculationparameters
import spire.math._
import scala.compat.Platform.EOL


final case class CalculationResults(steps: List[Number], parameters: List[CalculationParameters], results: Map[String, Map[Number,Number]]){
   def values(label:String): Map[Number,Number] = results(label)
   def valueForStep(label: String, step:Number): Number = values(label)(step)

   override def toString: String = {

      val line:String = results.map(x => x._1.toString+" | ").foldLeft("Step # | ")(_+_) + EOL +
      steps.map(s => s.toString+" | "+ results.map(r => r._2(s).toString + " | "))
      println(line+"Did it work?")
     results.map(x => x._1.toString).foldLeft("Step #")(_+_)
   }
}

