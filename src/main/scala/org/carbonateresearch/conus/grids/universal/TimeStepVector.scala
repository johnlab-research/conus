/*
 * Copyright © 2020 by Cédric John.
 *
 * This file is part of CoNuS.
 *
 * CoNuS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * CoNuS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CoNuS. If not, see <http://www.gnu.org/licenses/>.
 */

package org.carbonateresearch.conus.grids.universal

import breeze.linalg._
import breeze.storage.Zero
import org.carbonateresearch.conus.ModelVariable
import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.grids.GridElement

import scala.reflect.ClassTag

case class TimeStepVector(gridGeometry:Seq[Int],
                          private val underlyingGrid:DenseVector[GridElement])  {
   val vecSize:Int = gridGeometry.head

  override def toString():String = {
    (0 until gridGeometry.head).map(i => underlyingGrid(i).toString()).foldLeft("")(_+_)
  }
   def toString(timeStep:Int):String = {"TODO"}
   def toString(timeStep:Int,keys:CalculationParametersIOLabels*):String = {"TODO"}
   def timestep(t:Int):GridElement = underlyingGrid(t)
}

object TimeStepVector {
  def apply[A:ClassTag:Zero](modelVariable: CalculationParametersIOLabels, nbSteps: Int, gridGeometry: Seq[Int]): TimeStepVector = {
    val vecSize = gridGeometry.head
    val underlyingGrid = DenseVector.tabulate(nbSteps) { i => {
      modelVariable match {
        case v: ModelVariable[A] => {
          gridGeometry.size match {
            case 1 => {
              val underlyingGrid = DenseVector.fill(vecSize){v.initialValue.asInstanceOf[Object]}
              UGrid1D(gridGeometry,Seq(i),underlyingGrid.asInstanceOf[DenseVector[Object]])}
            case 2 =>{
              //println("attempting A for "+modelVariable.name+" using the following classTag:"+getClassName(v.initialValue))
              UGrid2D[Object](gridGeometry,Seq(i),v.initialValue.asInstanceOf[Object])}
            case 3 =>{
              val underlyingGrid = DenseVector.fill(vecSize){v.initialValue.asInstanceOf[Object]}
              UGrid1D(gridGeometry,Seq(i),underlyingGrid.asInstanceOf[DenseVector[Object]])}
          }
        }
        case v: ModelVariable[Double] => {
          gridGeometry.size match {
            case 1 => {
              val underlyingGrid = DenseVector.fill(vecSize){v.initialValue.asInstanceOf[Object]}
              UGrid1D(gridGeometry,Seq(i),underlyingGrid.asInstanceOf[DenseVector[Object]])}
            case 2 =>{
              println("attempting doubles")
              UGrid2D[Double](gridGeometry,Seq(i),v.initialValue)}
            case 3 =>{
              val underlyingGrid = DenseVector.fill(vecSize){v.initialValue.asInstanceOf[Object]}
              UGrid1D(gridGeometry,Seq(i),underlyingGrid.asInstanceOf[DenseVector[Object]])}
          }
        }
        case v: ModelVariable[Float] => {
          gridGeometry.size match {
            case 1 => {
              val underlyingGrid = DenseVector.fill(vecSize){v.initialValue.asInstanceOf[Object]}
              UGrid1D(gridGeometry,Seq(i),underlyingGrid.asInstanceOf[DenseVector[Object]])}
            case 2 =>{
              println("attempting floats")
              UGrid2D[Float](gridGeometry,Seq(i),v.initialValue)}
            case 3 =>{
              val underlyingGrid = DenseVector.fill(vecSize){v.initialValue.asInstanceOf[Object]}
              UGrid1D(gridGeometry,Seq(i),underlyingGrid.asInstanceOf[DenseVector[Object]])}
          }
        }
        case v: ModelVariable[Int] => {
          gridGeometry.size match {
            case 1 => {
              val underlyingGrid = DenseVector.fill(vecSize){v.initialValue.asInstanceOf[Object]}
              UGrid1D(gridGeometry,Seq(i),underlyingGrid.asInstanceOf[DenseVector[Object]])}
            case 2 =>{
              UGrid2D[Int](gridGeometry,Seq(i),v.initialValue)}
            case 3 =>{
              val underlyingGrid = DenseVector.fill(vecSize){v.initialValue.asInstanceOf[Object]}
              UGrid1D(gridGeometry,Seq(i),underlyingGrid.asInstanceOf[DenseVector[Object]])}
          }
        }
        case v: ModelVariable[Any] => {
          gridGeometry.size match {
            case 1 => {
              val underlyingGrid = DenseVector.fill(vecSize){v.initialValue.asInstanceOf[Object]}
              UGrid1D(gridGeometry,Seq(i),underlyingGrid.asInstanceOf[DenseVector[Object]])}
            case 2 =>{
              UGrid2D[Object](gridGeometry,Seq(i),v.initialValue.asInstanceOf[Object])}
            case 3 =>{
              val underlyingGrid = DenseVector.fill(vecSize){v.initialValue.asInstanceOf[Object]}
              UGrid1D(gridGeometry,Seq(i),underlyingGrid.asInstanceOf[DenseVector[Object]])}
          }
        }
      }
    }
    }
    new TimeStepVector(gridGeometry, underlyingGrid.asInstanceOf[DenseVector[GridElement]])
  }
}

