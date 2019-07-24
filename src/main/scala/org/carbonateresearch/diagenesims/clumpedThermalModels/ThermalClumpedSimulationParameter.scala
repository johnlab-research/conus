package org.carbonateresearch.diagenesims.clumpedThermalModels
import org.carbonateresearch.diagenesims.common.{AbstractCalculationStep, AbstractSimulationParameters}

final case class ThermalClumpedSimulationParameter (stepNumber: Int,
                                                    age: Double,
                                                    depth: Double,
                                                    D47Function: (Double, Double, Double) => Double,
                                                    D47iStart: Double,
                                                    D47eq: Double,
                                                    burialTemperatureAtStep: Double,
                                                    dT: Double )
  extends AbstractSimulationParameters with ThermalCalculationStepServices {}
