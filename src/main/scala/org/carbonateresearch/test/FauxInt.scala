package org.carbonateresearch.test

class FauxInt (i: Int) { val value = i }

trait LowPriorityImplicits {implicit def faux2int(f: FauxInt) = f.value}

object FauxInt extends LowPriorityImplicits {
  implicit def faux2rich(f: FauxInt): runtime.RichInt = f.value
}
