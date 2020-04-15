package org.carbonateresearch.conus.common
import java.lang.System.lineSeparator

trait StepResults {
  val EOL = lineSeparator()
def isDefinedAt[T](k:ModelVariable[T]):Boolean
def get[T](k:ModelVariable[T]): Option[T]
def get[T](k:CalculationParametersIOLabels): Any
def getAllKeys: List[CalculationParametersIOLabels]
  def prettyPrint[T](k:ModelVariable[T]):String
  def printAllStepValues: String
  def add[T](k:ModelVariable[T],v:T):StepResults
  def add(m:Map[CalculationParametersIOLabels,Any]):StepResults
}
