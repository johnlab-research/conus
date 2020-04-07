package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.equations.parametersIO.CalculationParametersIOLabels

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







