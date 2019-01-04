package thermalmodel

class ThermalHistorySimulation(val ageStep:Double, val burialHistory: List[(Double,Double)], val geothermalGradient: List[(Double,Double)], val surfaceTemp: List[(Double, Double)], val samples: List[Sample]) {
  val (burialAge, burialDepth) = burialHistory.unzip
  val initialAge: Double = burialAge.head
  val finalAge: Double = 0.0
  val initialDepth: Int = 0
  val finalDepth: Int = math.ceil(burialDepth.max).toInt
  val nbSteps: Int = math.ceil(initialAge/ageStep).toInt
  val adjustedAgeStep: Double =  initialAge/nbSteps
  val stepIndex: Vector[Int] = Vector.range(0, nbSteps+1, 1)
  val burialDepths: Vector[Int] = Vector.range(0,finalDepth+1,1)
  val ageMap: Map[Int, Double] = stepIndex.zip(ages).toMap
  val temperatureTensor: Map[Int,Map[Int,Double]] = tempTensorMap
  this.calculate

  def calculate = samples.par.foreach(s => new SimulatedSample(s, this))

  def ages: Vector[Double] = stepIndex.map(step => initialAge - (step * adjustedAgeStep))

  def depth2Temp (thisAge: Double): Map[Int, Double] = {
    val (geothAge, _ ) = geothermalGradient.unzip
    val (surfAge, surfTemp) = surfaceTemp.unzip
    val geothermIndex = getClosest(thisAge,geothAge)
    val geoGradMap = geothermalGradient.toMap
    val thisGeoGradient = geoGradMap(geothermIndex)
    val interpolatedSurfaceT = interpolator(thisAge, surfAge, surfTemp)
    val depthTemp = burialDepths.map(depth => depth * thisGeoGradient/1000 + interpolatedSurfaceT)

    burialDepths.zip(depthTemp).toMap
  }

  def tempTensorMap: Map[Int, Map[Int, Double]] = ageMap map { case (k, v) => k -> depth2Temp(v) }

  def age2Depth (simulatedAge: Double): Int = interpolator(simulatedAge, burialAge, burialDepth).toInt

  def interpolator (value: Double, xs: List[Double], ys: List[Double]): Double = {
    // This method is meant to be generic and return an interpolated value between two vectors
    val topValue = getClosest(value,xs)
    val Index = xs.indexOf(topValue)
    var topX = 0.0
    var topY = 0.0
    var botX = 0.0
    var botY = 0.0

    if(Index < xs.size-1){
      topY = ys(Index)
      topX = topValue
      botY = ys(Index+1)
      botX = xs(Index+1)}
    else {
      topY = ys(Index-1)
      topX = xs(Index-1)
      botY = ys(Index)
      botX = xs(Index)
    }
    (botY-topY)/(topX-botX)*(topX-value)+topY
  }

  def getClosest(num: Double, list:List[Double]): Double = {
    for(i <- 0 to list.size-1){
      if (list.length-1>i && list(i+1)-num < 0) return list(i)
    }
    (list(list.length-1))
  }


}
