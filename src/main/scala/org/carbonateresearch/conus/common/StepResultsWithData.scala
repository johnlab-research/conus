package org.carbonateresearch.conus.common

import java.lang.System.lineSeparator

case class StepResultsWithData(private val dataContainer:Map[CalculationParametersIOLabels,Data]) extends StepResults {

  override def isDefinedAt[T](k: ModelVariable[T]): Boolean = {
    dataContainer.isDefinedAt(k)
  }

  override def get[T](k: ModelVariable[T]): Option[T] = {
    getAnOption(k)
  }

  override def get[T](k: CalculationParametersIOLabels): Any = {
    k match {
      case v: ModelVariable[T] => getAnOption(v).getOrElse(v.defaultValue)
      case _ => None
    }
  }

  def apply[T](k: ModelVariable[T]): Option[T] = {
    getAnOption(k)
  }

  override def getAllKeys: List[CalculationParametersIOLabels] = dataContainer.keys.toList

  override def prettyPrint[T](k: ModelVariable[T]): String = {
    k.labelToValueFormattedString(dataContainer(k).asInstanceOf[T])
  }

  private def retrieveMVType[T](label: CalculationParametersIOLabels): ModelVariable[T] = {
    label match {
      case k: ModelVariable[T] => k
      case _ => label.asInstanceOf[ModelVariable[T]]
    }
  }

  override def allStepResultsString: String = {
    dataContainer.map(k => (k._2).descriptiveString).foldLeft(" | ")(_ + _)
  }

  override def add[T](k: ModelVariable[T], v: T): StepResultsWithData = {
    val data = Data(v, k.getValueAsString(v), k.labelToValueFormattedString(v))
    StepResultsWithData(this.dataContainer ++ Map(k -> data))
  }

  def add(m: Map[CalculationParametersIOLabels, Any]): StepResults = {
    val theData: Map[CalculationParametersIOLabels, Data] = m.flatMap(i => Map(i._1 -> Data(i._2, (i._1).getValueAsString(i._2), (i._1).labelToValueFormattedString(i._2))))
    StepResultsWithData(dataContainer++theData)
  }

  private def getAnOption[T](k: ModelVariable[T]): Option[T] = {
    if (dataContainer.isDefinedAt(k)) {
      dataContainer(k).numericValue match {
        case v: T => Some(v)
        case _ => None
      }
    } else {
      println("Variable "+k.name+" not defined")
      None
    }
  }
}
  object StepResultsWithData {
    def apply[T](k: ModelVariable[T], v: T): StepResultsWithData = {
      val data = Data(v, k.labelToValueFormattedString(v), k.labelToValueFormattedString(v))
      StepResultsWithData(Map(k -> data))
    }
}
