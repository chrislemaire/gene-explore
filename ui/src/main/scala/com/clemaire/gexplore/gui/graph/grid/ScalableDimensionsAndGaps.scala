package com.clemaire.gexplore.gui.graph.grid

trait ScalableDimensionsAndGaps
  extends ScalableDimensions {

  val xGapScale: Double = 0.2
  val yGapScale: Double = 0.2

  private var _xGap: Double = width * xGapScale
  private var _yGap: Double = height * yGapScale

  override def setXScale(xScaling: Double): Unit = {
    super.setXScale(xScaling)
    _xGap = width * xGapScale
  }

  override def setYScale(yScaling: Double): Unit = {
    super.setYScale(yScaling)
    _yGap = height * yGapScale
  }

}
