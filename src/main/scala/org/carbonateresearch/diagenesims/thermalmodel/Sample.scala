package org.carbonateresearch.diagenesims.thermalmodel

class Sample (val name: String, val age: Double, val D47observed: Double, val stratigraphicDepth: Double, val depositionalTemperature: Double) {
  def getD47Observed: Double = D47observed
  def getSampleAge: Double = age
  def getDepth: Double = stratigraphicDepth
  def getDepositionalTemperature: Double = depositionalTemperature
}
