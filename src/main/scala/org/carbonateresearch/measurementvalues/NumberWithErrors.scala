package org.carbonateresearch.measurementvalues


final case class NumberWithErrors (value: Double, error: Double)  {
  override def toString: String = value.toString + " ± " + error.toString
}

final object NumberWithErrors{
  def apply(value: Double): NumberWithErrors = new NumberWithErrors(value, 0.0)
  def apply(value: Double, min: Double, max: Double) = new NumberWithErrors(value, (max-min)/2)
}
