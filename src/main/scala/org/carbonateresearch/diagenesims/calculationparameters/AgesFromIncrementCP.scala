package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.{Number, Numeric}
import spire.implicits._

final case class AgesFromIncrementCP(increment:Number) extends CalculationParameters{

    override def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]] = {
        val numberOfSteps = steps.length-1
        val thisResult = steps.map(stepNb => (stepNb, (numberOfSteps-stepNb)*increment)).toMap
        println(thisResult)
        Map("Age" -> thisResult)
  }
}
