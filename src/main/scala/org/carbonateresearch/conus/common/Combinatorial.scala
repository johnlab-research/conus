package org.carbonateresearch.conus.common

trait Combinatorial {
  def combineSingleListToTuple[A,B](a:A, listB:List[B]): List[(A,B)] = {
    listB.map(b => (a,b))
    }

  def combineListListtoTuple[A,B](listA:List[A], ListB:List[B]): List[(A,B)] ={
    listA.flatMap(a => combineSingleListToTuple(a,ListB))
  }

  def combineSingleListTupletoTuple[A,B,C](a:A, listB: List[(B,C)]): List[(A,B,C)] = {
    listB.map(t => (a,t._1,t._2))
  }

  def combineSingleListListToTuple[A,B,C](a:A, b:List[B], c:List[C]): List[(A,B,C)] ={
    combineSingleListTupletoTuple(a,combineListListtoTuple(b,c))
  }

  def combineSeqs[A](a:Seq[A],b:Seq[A]):Seq[A] = {
    b.flatMap(x => a:+x)
  }

  def combineListOfLists[A](nestedLists:List[List[A]]):List[List[A]] = {
     val mergeTwoLists = (listA:List[List[A]],listB:List[A])
    => listB.flatMap(x => listA.map(y => x::y))

       val headOfList:List[List[A]] = nestedLists.head.map(x => List(x))
       val tailOfList:List[List[A]] = nestedLists.tail

      val returnList = tailOfList.foldLeft(headOfList)(mergeTwoLists)

    returnList.map(rl => rl.reverse)
  }
}
