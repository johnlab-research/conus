package org.carbonateresearch.conus.common
import shapeless.HMap
import org.carbonateresearch.conus.common.Step

final case class ModelVariable[T](name: String,
                                  defaultValue:T,
                                  unitName:String = "",
                                  silent: Boolean = false,
                                  override val precision:Int = 2,
                                  ifNoValue:NoValueHandler = ReturnDefaultValue) extends CalculationParametersIOLabels {

  override def toString: String = name


  def apply(step:Step): T = {

    val modelData: Map[Int, StepResults] = step.currentResults.currentResults
    val stepNumber = step.stepNumber

    implicit val myImplicitBiMapIS = new BiMapIS[ModelVariable[T], T]

    if (modelData.isDefinedAt(stepNumber)) {
      val optionWrappedData: HMap[BiMapIS] = modelData(stepNumber).dataContainer

      optionWrappedData.get(this) match {
        case v: Some[T] => v.get
        case None => handleMissingValue(step)
      }
    }
    else {
      handleMissingValue(step)
    }
  }

  private def handleMissingValue(step:Step):T = {
    {
      val stepNumber = step.stepNumber

      this.ifNoValue match {
        case ReturnDefaultValue => defaultValue
        case ReturnInitialConditions => {
          if (stepNumber != 0) {
            this.apply(Step(0, step.currentResults, step.stepErrors))
          } else {
            defaultValue
          }
        }
        case v: ReturnSpecificValue[T] => v.value
      }
    }
  }

}

