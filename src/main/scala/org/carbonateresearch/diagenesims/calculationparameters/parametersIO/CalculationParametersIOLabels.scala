package org.carbonateresearch.diagenesims.calculationparameters.parametersIO
import spire.math.{Number}

trait CalculationParametersIOLabels
case object NumberOfSteps extends CalculationParametersIOLabels {override def toString: String = "Number of Steps"}
case object Depth extends CalculationParametersIOLabels {override def toString: String = "Depth"}
case object Age extends CalculationParametersIOLabels {override def toString: String = "Age"}
case object SurfaceTemperature extends CalculationParametersIOLabels {override def toString: String = "Surface Temperature"}
case object BurialTemperature extends CalculationParametersIOLabels {override def toString: String = "Burial Temperature"}
case object GeothermalGradient extends CalculationParametersIOLabels {override def toString: String = "Geothermal Gradient"}
case class Doubler(n:Int =0) extends CalculationParametersIOLabels {override def toString: String = "My Doubled"}