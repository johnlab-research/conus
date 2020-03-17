package org.carbonateresearch.conus

import org.carbonateresearch.test.ModelVariable

case object Implicits {
  class BiMapIS[K, V]
  implicit val intToString = new BiMapIS[Int, String]
  implicit val stringToInt = new BiMapIS[String, Int]
  implicit val IntToInt = new BiMapIS[Int, Int]
  implicit val ModelVariabletoDouble:BiMapIS[ModelVariable[Double],Double] = new BiMapIS[ModelVariable[Double],Double]
}
