package org.carbonateresearch.test

case object equationWrapper{
  def apply[A](expression:A):Int=>A ={
    (x:Int) => expression
  }
}
