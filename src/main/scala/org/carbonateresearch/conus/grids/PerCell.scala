package org.carbonateresearch.conus.grids

import org.carbonateresearch.conus.common.{CalculationParametersIOLabels, Combinatorial, InitialCondition}

case class PerCell(variableName:CalculationParametersIOLabels, value:List[(Any,Seq[Int])]) extends GridValueDescriptor with Combinatorial {
  def setOfValues:List[List[(Any,Seq[Int])]] = {


      val valueAsLists:List[(List[Any],Seq[Int])] = {
        value.map(v => {v._1 match {
          case l:List[Any] => (l,v._2)
          case l:Any => (List(l),v._2)}
        })}

    val nestedList:List[List[Any]] = combineListOfLists(valueAsLists.map(x => x._1))
    val allPossibleValues: List[List[(Any,Seq[Int])]] = nestedList.map(nl => nl.zip(valueAsLists.unzip._2))

      allPossibleValues
    }
  }
