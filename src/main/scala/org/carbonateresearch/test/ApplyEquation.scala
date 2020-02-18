package org.carbonateresearch.test

import RunTest.Step

import org.carbonateresearch.conus.calculationparameters.CalculateStepValue

case class ApplyEquation[T](equation:Step=>T){

  def storeResultAs(saveLabel:Parameter[T]): SingleCalculationDescription[T] = SingleCalculationDescription(equation,saveLabel)
}

/*
object ApplyEquation{
  def apply[A,B](equation:A => B, dummyImplicit: DummyImplicit): ApplyEquation[A,B] = {
    val functionWrapper: List[A] => B = (values:List[A]) => equation(values(0))
    new ApplyEquation[A,B](functionWrapper) }

  def apply[A,B](equation:(A,A) => B): ApplyEquation[A,B] = {
    val functionWrapper: List[A] => B = (values:List[A]) => equation(values(0),values(1))
    new ApplyEquation[A,B](functionWrapper) }

  def apply[A,B](equation:(A,A,A) => B): ApplyEquation[A,B] = {
    val functionWrapper: List[A] => B = (values:List[A]) => equation(values(0),values(1),values(2))
    new ApplyEquation[A,B](functionWrapper) }

  def apply[A,B](equation:(A,A,A,A) => B): ApplyEquation[A,B] = {
    val functionWrapper: List[A] => B = (values:List[A]) => equation(values(0),values(1),values(2),values(3))
    new ApplyEquation[A,B](functionWrapper) }

  def apply[A,B](equation:(A,A,A,A,A) => B): ApplyEquation[A,B] = {
    val functionWrapper: List[A] => B = (values:List[A]) => equation(values(0),values(1),values(2),values(3),values(4))
    new ApplyEquation[A,B](functionWrapper) }

  def apply[A,B](equation:(A,A,A,A,A,A) => B): ApplyEquation[A,B] = {
    val functionWrapper: List[A] => B = (values:List[A]) => equation(values(0),values(1),values(2),values(3),values(4),values(5))
    new ApplyEquation[A,B](functionWrapper) }
}*/

