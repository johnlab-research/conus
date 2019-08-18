package org.carbonateresearch.conus.clumpedThermalModels

import akka.actor.Actor
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.Scene
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}

import scala.collection.mutable.ListBuffer

class ThermalPlot extends Actor{

  override def receive = {

    case model: ThermalClumpedModel =>
  }

}
