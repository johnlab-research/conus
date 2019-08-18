package org.carbonateresearch.conus.clumpedThermalModels

import org.carbonateresearch.conus.common.ForwardModelServices

final case class ThermalHistorySimulation(ageStep:Double, burialHistory: List[(Double,Double)], geothermalGradient: List[(Double,Double)], surfaceTemp: List[(Double, Double)], samples: List[ClumpedSample])
extends ForwardModelServices

