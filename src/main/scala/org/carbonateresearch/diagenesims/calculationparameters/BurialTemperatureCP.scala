package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.{Number, Numeric}
import spire.implicits._

final case class BurialTemperatureCP(geothermalGradientsAgeMap: List[(Number,Number)]) extends CalculationParameters {

  override def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]]  = {
    val burialTemperatures = steps.map(step => (step,previousResults("Depth")(step)*previousResults("Geothermal Gradient")(step)/1000+previousResults("Surface Temperature")(step)))

      Map("Burial Temperature" -> burialTemperatures.toMap)++previousResults

  }

}


