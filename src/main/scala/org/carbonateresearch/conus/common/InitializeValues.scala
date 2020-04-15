package org.carbonateresearch.conus.common

final case class InitializeValues(inputs: List[(CalculationParametersIOLabels,List[Any])]) {

    private val pairedIOLabelValues:List[List[(CalculationParametersIOLabels,Any)]] = {
      inputs.map(x => x match {
        case (p:CalculationParametersIOLabels,ln:List[Any]) => ln.map(v => (p,v))
      })}

 private val mergeTwoLists = (listA:List[List[(CalculationParametersIOLabels,Any)]],listB:List[(CalculationParametersIOLabels,Any)])
 => listB.flatMap(x => listA.map(y => x::y))

  private val headOfList:List[List[(CalculationParametersIOLabels,Any)]] = pairedIOLabelValues.head.map(x => List(x))
  private val tailOfList:List[List[(CalculationParametersIOLabels,Any)]] = pairedIOLabelValues.tail


  val ModelCalculationSpace: List[List[(CalculationParametersIOLabels, Any)]] = tailOfList.foldLeft(headOfList)(mergeTwoLists)

}







