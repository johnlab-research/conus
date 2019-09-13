package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, _}
import spire.math.Number

import scala.annotation.tailrec


final case class InitializeValues(inputs: List[(CalculationParametersIOLabels,List[Number])]) {

    private val pairedIOLabelValues:List[List[(CalculationParametersIOLabels,Number)]] = {
inputs.map(x => x match {
        case (p:CalculationParametersIOLabels,ln:List[Number]) => ln.map(v => (p,v))
      })}

 private val mergeTwoLists = (listA:List[List[(CalculationParametersIOLabels,Number)]],listB:List[(CalculationParametersIOLabels,Number)])
 => listB.flatMap(x => listA.map(y => x::y))

  private val headOfList:List[List[(CalculationParametersIOLabels,Number)]] = pairedIOLabelValues.head.map(x => List(x))
  private val tailOfList:List[List[(CalculationParametersIOLabels,Number)]] = pairedIOLabelValues.tail


  val ModelCalculationSpace: List[List[(CalculationParametersIOLabels, Number)]] = tailOfList.foldLeft(headOfList)(mergeTwoLists)

}


object InitializeValues {
  def apply(input: (CalculationParametersIOLabels,Number)*) = {

    new InitializeValues(input.toList.map( x => x match {
      case (p:CalculationParametersIOLabels,ln:Number) => (p,List(ln))}))
    }
}




