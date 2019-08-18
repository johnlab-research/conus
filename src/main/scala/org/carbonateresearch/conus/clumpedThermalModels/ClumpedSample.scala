package org.carbonateresearch.conus.clumpedThermalModels

final case class ClumpedSample(val name: String, val depositionalAge: Double, val D47observed: Double, val depositionalDepth: Double = 0.0, val depositionalTemperature: Double)
