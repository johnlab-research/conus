package org.carbonateresearch.conus

import org.carbonateresearch.test.ModelVariable

case object Implicits {
  class BiMapIS[K, V]
  implicit def GenericModelVariableTtoTImplicit[T](myVar:ModelVariable[T]):BiMapIS[ModelVariable[T],T] = myVar.implicitBiMapIS
}
