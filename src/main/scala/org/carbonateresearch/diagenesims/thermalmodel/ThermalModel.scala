package org.carbonateresearch.diagenesims.thermalmodel

object ThermalModel extends App {
    //val regionalParameters = new Constants()
   // val burialHistory = List((85.0,0.0), (30.0,3000.0),(0.0,0.0))
   // val geothermalGradient = List((85.0,30.0))
   // val surfaceTemperatures = List((85.0, 30.0),(0.0, 25.0))


    val regionalParameters = Constants
    val burialHistory = List((85.0,0.0), (0.0,6000.0))
    val geothermalGradient = List((85.0,30.0), (0.0,30.0))
    val surfaceTemperatures = List((85.0, 30.0),(0.0, 25.0))

    val sample1 = new Sample(name = "cc1", depositionalAge = 85.0, depositionalDepth = 0.0, D47observed = 0.712, depositionalTemperature = 50.0)
    val sample2 = new Sample(name = "cc2", depositionalAge = 65.0, depositionalDepth = 0.0, D47observed = 0.600, depositionalTemperature = 37.0)
    val sample3 = new Sample(name = "cc3", depositionalAge = 54.0, depositionalDepth = 800.0, D47observed = 0.712, depositionalTemperature = 40.0)
    val sample4 = new Sample(name = "cc4", depositionalAge = 55.0, depositionalDepth = 801.0, D47observed = 0.600, depositionalTemperature = 40.0)

    val sampleList = List(sample1, sample2, sample3, sample4)


    val model1 = new ThermalHistorySimulation(ageStep = 1,
      burialHistory = burialHistory, samples = sampleList, geothermalGradient = geothermalGradient, surfaceTemp = surfaceTemperatures)

}
