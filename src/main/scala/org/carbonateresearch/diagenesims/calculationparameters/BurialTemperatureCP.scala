package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.{Number, Numeric}
import spire.implicits._

final  class BurialTemperatureCP extends CalculationParameters {

  override def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]]  = {
/*
    val depths: Map[Int, Double] = previousResults("Depth").valueMap
    val surfaceTemps: Map[Int, Double] = previousResults("Surface Temperature").valueMap
    val geothermalGrds: Map[Int, Double] = previousResults("Geothermal Gradient").valueMap

    val thisResult:List[Map[Int,Double]] = for (
      step <- steps;
      (key1, surfaceTemp) <- surfaceTemps;
      (key2, geothermalGrd) <- geothermalGrds;
      (key3, depth) <- depths;
    ) yield (Map(step -> (surfaceTemp + geothermalGrd * depth)))

    List(Map("Burial Temperature" -> CalculationResults(this,thisResult(0))))*/
    Map()

  }

}


