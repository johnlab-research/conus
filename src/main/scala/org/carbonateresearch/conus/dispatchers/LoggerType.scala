package org.carbonateresearch.conus.dispatchers

trait LoggerType
case class ConsoleLoggerType() extends LoggerType
case class AlmondLoggerType(kernel: almond.api.JupyterApi, cellID:String) extends LoggerType
