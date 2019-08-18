package org.carbonateresearch.conus.implicits

import spire.math.Number

trait NumberWrapper {
  implicit val wrapSimpleListDouble = (myValues:List[Double]) =>  myValues.map(nb => Number(nb))
  implicit val wrapSimpleListInt = (myValues:List[Int]) =>  myValues.map(nb => Number(nb))
  implicit val wrapTupledListDouble = (myValues:List[(Double,Double)]) =>  myValues.map(nb => (Number(nb._1),Number(nb._2)))
  implicit val wrapTupledListInt = (myValues:List[(Int,Int)]) => myValues.map(nb => (Number(nb._1),Number(nb._2)))
  implicit val wrapTupledListIntDouble = (myValues:List[(Int,Double)]) => myValues.map(nb => (Number(nb._1),Number(nb._2)))
  implicit val wrapTupledListDoubleInt = (myValues:List[(Double,Int)]) => myValues.map(nb => (Number(nb._1),Number(nb._2)))


  implicit def functionalWrapper (wrongSigFunc: Double => Double): Number => Number = {
    v1: Number => Number(wrongSigFunc(v1.toDouble))
  }

  implicit def functionalWrapper (wrongFunc: (Double, Double) => Double): (Number, Number) => Number = {
    (v1: Number, v2: Number) => Number(wrongFunc(v1.toDouble, v2.toDouble))
  }

  implicit def functionalWrapper (wrongFunc: (Double,Double,Double) => Double
                                 ): (Number, Number, Number) => Number = {
    (v1,v2,v3: Number) => Number(wrongFunc((v1.toDouble,v2.toDouble,v3.toDouble)))
  }

  implicit def functionalWrapper (wrongFunc: (Double,Double,Double,Double) => Double
                                 ): (Number, Number, Number,Number) => Number = {
    (v1,v2,v3,v4: Number) => Number(wrongFunc((v1.toDouble,v2.toDouble,v3.toDouble,v4.toDouble)))
  }

  implicit def functionalWrapper (wrongFunc: (Double, Double,Double,Double,Double) => Double
                                 ): (Number,Number, Number, Number,Number) => Number = {
    (v1,v2,v3,v4,v5: Number) => Number(wrongFunc((v1.toDouble,v2.toDouble,v3.toDouble,v4.toDouble,v5.toDouble)))
  }

  implicit def functionalWrapper (wrongFunc: (Double,Double, Double,Double,Double,Double) => Double
                                 ): (Number,Number,Number, Number, Number,Number) => Number = {
    (v1,v2,v3,v4,v5,v6: Number) => Number(wrongFunc((v1.toDouble,v2.toDouble,v3.toDouble,v4.toDouble,v5.toDouble,v6.toDouble)))
  }

  implicit def functionalWrapper (wrongFunc: (Double,Double,Double, Double,Double,Double,Double) => Double
                                 ): (Number,Number,Number,Number, Number, Number,Number) => Number = {
    (v1,v2,v3,v4,v5,v6,v7: Number) => Number(wrongFunc((v1.toDouble,v2.toDouble,v3.toDouble,v4.toDouble,v5.toDouble,v6.toDouble,v7.toDouble)))
  }

  implicit def functionalWrapper (wrongFunc: (Double,Double,Double,Double, Double,Double,Double,Double) => Double
                                 ): (Number,Number,Number,Number,Number, Number, Number,Number) => Number = {
    (v1,v2,v3,v4,v5,v6,v7,v8: Number) => Number(wrongFunc((v1.toDouble,v2.toDouble,v3.toDouble,v4.toDouble,v5.toDouble,v6.toDouble,v7.toDouble,v8.toDouble)))
  }

  implicit def functionalWrapper (wrongFunc: (Double,Double,Double,Double,Double, Double,Double,Double,Double) => Double
                                 ): (Number,Number,Number,Number,Number,Number, Number, Number,Number) => Number = {
    (v1,v2,v3,v4,v5,v6,v7,v8,v9: Number) => Number(wrongFunc((v1.toDouble,v2.toDouble,v3.toDouble,v4.toDouble,v5.toDouble,v6.toDouble,v7.toDouble,v8.toDouble,v9.toDouble)))
  }

  implicit def functionalWrapper (wrongFunc: (Double,Double,Double,Double,Double,Double, Double,Double,Double,Double) => Double
                                 ): (Number,Number,Number,Number,Number,Number,Number, Number, Number,Number) => Number = {
    (v1,v2,v3,v4,v5,v6,v7,v8,v9,v10: Number) => Number(wrongFunc((v1.toDouble,v2.toDouble,v3.toDouble,v4.toDouble,v5.toDouble,v6.toDouble,v7.toDouble,v8.toDouble,v9.toDouble,v10.toDouble)))
  }


}
