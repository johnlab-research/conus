package org.carbonateresearch.diagenesims.calculationparameters
import spire.math._
import spire.algebra._
import spire.implicits._
import scala.annotation.tailrec

// Tagging trait for model parameters case classes
trait CalculationParameters {
  def modelName:String = "Model Name"
  def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]] = ???
}
