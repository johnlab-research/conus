package org.carbonateresearch.conus.util
import StepFunctionUtils._
import org.carbonateresearch.conus.common.{Step,ModelVariable}

object CommonFunctions {

  def scaleFromMinToMaxByStep[T](nbSteps: Int,min: T, max: T)(implicit num:Fractional[T]): StepFunction[T] = {
    val typedNbSteps = min match {
      case m: Double => nbSteps.toDouble
      case m: Float => nbSteps.toFloat
      case _ => nbSteps.toDouble
    }
    val increment = num.div(num.minus(max,min),typedNbSteps.asInstanceOf[T])
    val myFunction: StepFunction[T] = (s: Step) => {
      val v = min match {
        case m: Double => s.stepNumber.toDouble
        case m: Float => s.stepNumber.toFloat
        case _ => s.stepNumber.toDouble
      }
      num.minus(max, (num.times(v.asInstanceOf[T],increment)))}

    myFunction
  }

def interpolatedValue[T](v:ModelVariable[T], xyList: List[(T, T)])(implicit num:Fractional[T]):StepFunction[T] = {
  val stepEquation = (s: Step) => {
    // This method is meant to be generic and return an interpolated value between two numbers
    val xValue = v(s)
    val xs = xyList.sorted.map(a => a._1)
    val ys = xyList.sorted.map(a => a._2)
    val firstNegative: Option[T] = getLowerBound(xValue,xs)
    var Index = 0
    firstNegative match {
      case Some(i) => {
        if (xs.indexOf(i) < xs.size && xs.indexOf(i) > 0) {
          Index = xs.indexOf(i)
        } else if (xs.indexOf(i) == 0) {
          Index = 1
        } else {
          Index = xs.indexOf(i) - 1
        }
      }
      case None => Index = xs.size - 1
    }
    val topY = ys(Index - 1)
    val topX = xs(Index - 1)
    val botY = ys(Index)
    val botX = xs(Index)
    num.plus(num.times(num.div(num.minus(botY,topY), num.minus(topX,botX)), num.minus(topX,xValue)),topY)
  }
stepEquation
}

  private def getLowerBound[T](value:T, xs:List[T])(implicit num:Fractional[T]): Option[T] = {
    val signedIndex = xs.map(x => if(num.compare((num.minus(value,x)),num.zero) < 0 || num.compare((num.minus(value,x)),num.zero) == 0) {
      -1*(xs.indexOf(x)+1)
    } else {xs.indexOf(x)+1})

    val pairedVals = signedIndex.zip(xs).sortBy(pairedVals=>pairedVals._1)

    if(pairedVals(0)._1<=0){Some(pairedVals(0)._2)} else {None}
  }
}
