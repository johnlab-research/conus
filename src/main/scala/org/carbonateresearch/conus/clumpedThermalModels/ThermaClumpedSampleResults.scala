package org.carbonateresearch.conus.clumpedThermalModels
import org.carbonateresearch.conus.common.AbstractSimulationResults

final case class ThermalClumpedSampleResults (name: String, age:Double, depth:Double, parameters: List[ThermalClumpedSimulationParameter], results: List[ThermalCalculationStep]) extends AbstractSimulationResults {}
