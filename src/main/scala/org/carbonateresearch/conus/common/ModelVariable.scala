package org.carbonateresearch.conus.common
import shapeless.HMap
import org.carbonateresearch.conus.common.Step
import org.carbonateresearch.conus.util.StepFunctionUtils.StepFunction

final case class ModelVariable[T](override val name: String,
                                  defaultValue:T,
                                  unitName:String = "",
                                  silent: Boolean = false,
                                  override val precision:Int = 2,
                                  ifNoValue:NoValueHandler = ReturnDefaultValue) extends CalculationParametersIOLabels {

  override def toString: String = name

  override def getValueAsString(value:Any):String = {
    val typedValue:T = value.asInstanceOf[T]
    formatValue(precision,typedValue)
  }

  private def formatValue(precision:Int, value:T): String = {
    val pValue:Double =  1+precision.toDouble/10
    val formatString:String = "%"+ pValue.toString + "f"

    value match{
      case v:Double => formatString format value
      case v:Float => formatString format value
      case v:BigDecimal => formatString format value
      case _ => value.toString
    }
  }

  def apply(step:Step): T = {
    val calculatedStep = step.stepNumber+step.stepOffset
    val stepNumber:Int = if(calculatedStep<0){0}else{calculatedStep}
    val modelData = step.currentResults
    if (modelData.isDefinedAt(stepNumber)) {
      val optionWrappedData: Option[T] = modelData.getStepResult(stepNumber,this)

      optionWrappedData match {
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
            this.apply(Step(0,step.totalSteps, step.currentResults, step.stepErrors))
          } else {
            defaultValue
          }
        }
        case v: ReturnSpecificValue[T] => v.value
      }
    }
  }

  def =>>(f: StepFunction[T]):CalculationDescription[T] = {
    ApplyStepFunction(f).storeResultAs(this)
  }

}

