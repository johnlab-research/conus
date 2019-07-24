package org.carbonateresearch.diagenesims.common
import spire.math._

trait NumberWrapper {
  implicit val wrapSimpleListDouble = (myValues:List[Double]) =>  myValues.map(nb => Number(nb))
  implicit val wrapSimpleListInt = (myValues:List[Int]) =>  myValues.map(nb => Number(nb))
  implicit val wrapTupledListDouble = (myValues:List[(Double,Double)]) =>  myValues.map(nb => (Number(nb._1),Number(nb._2)))
  implicit val wrapTupledListInt = (myValues:List[(Int,Int)]) => myValues.map(nb => (Number(nb._1),Number(nb._2)))
  implicit val wrapTupledListIntDouble = (myValues:List[(Int,Double)]) => myValues.map(nb => (Number(nb._1),Number(nb._2)))
  implicit val wrapTupledListDoubleInt = (myValues:List[(Double,Int)]) => myValues.map(nb => (Number(nb._1),Number(nb._2)))
}
