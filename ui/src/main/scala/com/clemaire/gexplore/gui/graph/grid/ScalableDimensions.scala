package com.clemaire.gexplore.gui.graph.grid

trait ScalableDimensions {

  val unscaledWidth: Double
  val unscaledHeight: Double

  private var _width: Double = unscaledWidth
  private var _height: Double = unscaledHeight

  def setXScale(xScaling: Double): Unit =
    _width = unscaledWidth * xScaling

  def setYScale(yScaling: Double): Unit =
    _height = unscaledHeight * yScaling

  def setScale(xScaling: Double,
               yScaling: Double): Unit = {
    setXScale(xScaling)
    setYScale(yScaling)
  }

  def width: Double = _width
  def height: Double = _height

  def xScale: Double = unscaledWidth / width
  def yScale: Double = unscaledHeight / height

}
