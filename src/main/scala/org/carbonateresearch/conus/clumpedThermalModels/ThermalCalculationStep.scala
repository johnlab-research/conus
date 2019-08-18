package org.carbonateresearch.conus.clumpedThermalModels

import org.carbonateresearch.conus.common.AbstractCalculationStep

final case class ThermalCalculationStep(stepNumber: Int, depth: Double, age: Double,  D47iFinal: Double, temperature: Double)
extends AbstractCalculationStep



