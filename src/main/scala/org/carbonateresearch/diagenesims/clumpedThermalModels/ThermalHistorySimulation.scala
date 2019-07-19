package org.carbonateresearch.diagenesims.clumpedThermalModels

import org.carbonateresearch.diagenesims.ageSteppedModels.ForwardModelServices

final case class ThermalHistorySimulation(ageStep:Double, burialHistory: List[(Double,Double)], geothermalGradient: List[(Double,Double)], surfaceTemp: List[(Double, Double)], samples: List[ClumpedSample])
extends ForwardModelServices

