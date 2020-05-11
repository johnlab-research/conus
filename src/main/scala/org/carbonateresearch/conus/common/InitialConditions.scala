package org.carbonateresearch.conus.common

case class InitialConditions(variable:CalculationParametersIOLabels,
                             values:List[Any],
                             coordinates: List[Seq[Int]] = List()) extends Combinatorial {

  val conditions: List[InitialCondition] = createConditionSpace

    private def createConditionSpace: List[InitialCondition] = {
    coordinates.size match {
      case 0 => combineSingleListToTuple(variable,values).map(x => InitialCondition(x._1,x._2,List()))
      case _ => combineSingleListListToTuple(variable,values, coordinates).map(x => InitialCondition(x._1,x._2,x._3))}
    }
}
