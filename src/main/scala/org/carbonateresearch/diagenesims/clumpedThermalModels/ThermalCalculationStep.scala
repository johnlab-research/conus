package org.carbonateresearch.diagenesims.clumpedThermalModels

import org.carbonateresearch.diagenesims.ageSteppedModels.AbstractCalculationStep

final case class ThermalCalculationStep(stepNumber: Int, depth: Double, age: Double,  D47iFinal: Double, temperature: Double)
extends AbstractCalculationStep



