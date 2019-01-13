package org.carbonateresearch.diagenesims.thermalmodel

object ThermalModel extends App {
    //val regionalParameters = new Constants()
    val burialHistory = List((126.0,0.0), (0.0,4500.0))
    val geothermalGradient = List((126.0,35.0))
    val surfaceTemperatures = List((126.0, 20.0),(0.0, 20.0))

    val sample1 = new Sample(name = "Sample 1", age = 63.0, stratigraphicDepth = 0.0, D47observed = 0.712, depositionalTemperature = 20.0)
    val sample2 = new Sample(name = "Sample 2", age = 63.0, stratigraphicDepth = 800.0, D47observed = 0.600, depositionalTemperature = 67.0)

    val sampleList = List(sample1, sample2)


    val model1 = new ThermalHistorySimulation(ageStep = 1,
      burialHistory = burialHistory, samples = sampleList, geothermalGradient = geothermalGradient, surfaceTemp = surfaceTemperatures)

}
