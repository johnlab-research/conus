package org.carbonateresearch.test

object implicits {

  implicit def convertSet1ToListForFunctionWrapper[A,B](equation:A => B): List[A] => B = (values:List[A]) => equation(values(0))

  implicit def convertSet2ToListForFunctionWrapper[A,B](equation:(A,A) => B): List[A] => B = (values:List[A]) => equation(values(0),values(1))

  implicit def convertSet3ToListForFunctionWrapper[A,B](equation:(A,A,A) => B): List[A] => B = (values:List[A]) => equation(values(0),values(1),values(2))

  implicit def convertSet4ToListForFunctionWrapper[A,B](equation:(A,A,A,A) => B): List[A] => B = (values:List[A]) => equation(values(0),values(1),values(2),values(3))

  implicit def convertSet5ToListForFunctionWrapper[A,B](equation:(A,A,A,A,A) => B):  List[A] => B = (values:List[A]) => equation(values(0),values(1),values(2),values(3),values(4))

  implicit def convertSet6ToListForFunctionWrapper[A,B](equation:(A,A,A,A,A,A) => B): List[A] => B = (values:List[A]) => equation(values(0),values(1),values(2),values(3),values(4),values(5))
}
