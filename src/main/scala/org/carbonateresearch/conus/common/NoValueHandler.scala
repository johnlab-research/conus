package org.carbonateresearch.conus.common

sealed trait NoValueHandler
case object ReturnDefaultValue extends NoValueHandler
case object ReturnInitialConditions extends NoValueHandler
case class ReturnSpecificValue[T](value:T) extends NoValueHandler
