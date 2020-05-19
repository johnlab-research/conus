package org.carbonateresearch.conus.common

case class InitialConditions(variable:CalculationParametersIOLabels,
                             values:List[List[Any]],
                             coordinates: List[Seq[Int]] = List()) extends Combinatorial {

  val conditions: List[InitialCondition] = conditionSpace

  def conditionSpace:List[InitialCondition] = {
    val zipV:List[(List[Any],Seq[Int])] = {
      values.map(v => {
      coordinates.isDefinedAt(values.indexOf(v)) match {
        case true => (v,coordinates(values.indexOf(v)))
        case false => (v,Seq())}
      })}

    val theList  = for {
      z <- zipV
      v1 <- z._1
    } yield InitialCondition(variable,v1,z._2)
println(theList)
    theList
  }



    private def createConditionSpace: List[InitialCondition] = combineValues.flatMap(cv => {
      cv._1.map(v => InitialCondition(variable,v,cv._2))})

  private def combineValues:List[(List[Any],Seq[Int])] = {
    val ret = values.indices.toList.map(i => {
      values(i) match {
        case l:List[Any] => (l,getCoordinates(i))
        case _ => (List(values(i)),getCoordinates(i))
      }
    })
    ret
  }

  private def getCoordinates(index:Int):Seq[Int] = {
    coordinates.isDefinedAt(index) match {
      case true => coordinates(index)
      case false => Seq()
    }
  }


}
