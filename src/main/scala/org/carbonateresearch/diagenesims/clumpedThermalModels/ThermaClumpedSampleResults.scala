package org.carbonateresearch.diagenesims.clumpedThermalModels
import org.carbonateresearch.diagenesims.common.AbstractSimulationResults

final case class ThermalClumpedSampleResults (name: String, age:Double, depth:Double, parameters: List[ThermalClumpedSimulationParameter], results: List[ThermalCalculationStep]) extends AbstractSimulationResults {}
