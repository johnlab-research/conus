package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels

final case class InitializeValues(inputs: List[(CalculationParametersIOLabels,List[Double])]) {

    private val pairedIOLabelValues:List[List[(CalculationParametersIOLabels,Double)]] = {
inputs.map(x => x match {
        case (p:CalculationParametersIOLabels,ln:List[Double]) => ln.map(v => (p,v))
      })}

 private val mergeTwoLists = (listA:List[List[(CalculationParametersIOLabels,Double)]],listB:List[(CalculationParametersIOLabels,Double)])
 => listB.flatMap(x => listA.map(y => x::y))

  private val headOfList:List[List[(CalculationParametersIOLabels,Double)]] = pairedIOLabelValues.head.map(x => List(x))
  private val tailOfList:List[List[(CalculationParametersIOLabels,Double)]] = pairedIOLabelValues.tail


  val ModelCalculationSpace: List[List[(CalculationParametersIOLabels, Double)]] = tailOfList.foldLeft(headOfList)(mergeTwoLists)

}


object InitializeValues {
  def apply(input: (CalculationParametersIOLabels,Double)*) = {

    new InitializeValues(input.toList.map( x => x match {
      case (p:CalculationParametersIOLabels,ln:Double) => (p,List(ln))}))
    }
}




