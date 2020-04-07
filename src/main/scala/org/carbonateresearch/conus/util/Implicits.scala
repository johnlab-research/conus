package org.carbonateresearch.conus.util

import org.carbonateresearch.conus.common.{BiMapIS, ModelVariable}
case object Implicits {
  implicit val varToDouble = new BiMapIS[ModelVariable[Double], Double]
  implicit val varToInt = new BiMapIS[ModelVariable[Int], Int]
  implicit val varToFloat = new BiMapIS[ModelVariable[Float], Float]
  implicit val varToLong = new BiMapIS[ModelVariable[Long], Long]
  implicit val varToBigInt = new BiMapIS[ModelVariable[BigInt], BigInt]
  implicit val varToBigDecimal = new BiMapIS[ModelVariable[BigDecimal], BigDecimal]
  implicit val varToByte = new BiMapIS[ModelVariable[Byte], Byte]
  implicit val varToString = new BiMapIS[ModelVariable[String], String]
  implicit val varToListDouble = new BiMapIS[ModelVariable[List[Double]], List[Double]]
  implicit val varToListInt = new BiMapIS[ModelVariable[List[Int]], List[Int]]
  implicit val varToListFloat= new BiMapIS[ModelVariable[List[Float]], List[Float]]
  implicit val varToListLong = new BiMapIS[ModelVariable[List[Long]], List[Long]]
  implicit val varToListBigInt = new BiMapIS[ModelVariable[List[BigInt]], List[BigInt]]
  implicit val varToListBigDecimal = new BiMapIS[ModelVariable[List[BigDecimal]], List[BigDecimal]]
  implicit val varToListByte = new BiMapIS[ModelVariable[List[Byte]], List[Byte]]
  implicit val varToListString = new BiMapIS[ModelVariable[List[String]], List[String]]
}
