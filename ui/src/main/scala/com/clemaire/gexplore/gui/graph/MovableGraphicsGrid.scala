package com.clemaire.gexplore.gui.graph

import com.clemaire.gexplore.gui.graph.grid.{GridVisibilityCheck, MovableGrid, RedrawIndicator, ScalableDimensionsAndGaps}
import javafx.beans.value.ChangeListener
import javafx.scene.canvas.GraphicsContext

class MovableGraphicsGrid(val graphics: GraphicsContext)
  extends MovableGrid
    with GridVisibilityCheck
    with ScalableDimensionsAndGaps
    with RedrawIndicator {

  val maxXTranslation: Double = 1920.0 * 3
  val maxYTranslation: Double = 1080.0 * 3

  var origin: (Double, Double) = (0.0, 0.0)

  override val unscaledWidth: Double = 40.0
  override val unscaledHeight: Double = 15.0

  private val _: Unit = {
    val f: ChangeListener[_ >: Number] = (_, _, _) => checkTranslation()
    graphics.getTransform.txProperty.addListener(f)
    graphics.getTransform.txProperty.addListener(f)
  }


  def translationX: Double = graphics.getTransform.getTx
  def translationY: Double = graphics.getTransform.getTy

  def screenWidth: Double = graphics.getCanvas.getWidth
  def screenHeight: Double = graphics.getCanvas.getHeight


  def clearGraphics(): Unit =
    graphics.clearRect(-maxXTranslation, -maxYTranslation,
      maxXTranslation + screenWidth, maxYTranslation + screenHeight)


  def anchorHere(): Unit = {
    clearGraphics()

    super.jumpTo(coordinatesAt(0.0, 0.0))

    graphics.translate(-translationX, -translationY)
    requireRedraw()
  }

  def checkTranslation(): Unit =
    if (Math.abs(translationX) > maxXTranslation - screenWidth
      || Math.abs(translationY) > maxYTranslation - screenHeight) {

      anchorHere()
    }


  def screenCoordinatesFor(coordinates: (Double, Double))
  : (Double, Double) =
    ((coordinates._1 - col) * width + translationX,
      (coordinates._2 - row) * height + translationY)

  def coordinatesAt(screenCoordinates: (Double, Double))
  : (Double, Double) =
    (col + (screenCoordinates._1 - translationX) / width,
      row + (screenCoordinates._2 - translationY) / height)


  override def jumpTo(col: Double, row: Double): Unit = {
    clearGraphics()

    super.jumpTo(col, row)

    val (screenX, screenY) = screenCoordinatesFor(origin)
    graphics.translate(screenX, screenY)
  }

  override def translate(colTranslate: Double, rowTranslate: Double): Unit = {
    graphics.translate(colTranslate * width, rowTranslate * height)
  }

  override protected def leftMargin: Int = (maxXTranslation / width).toInt
  override protected def rightMargin: Int = ((maxXTranslation + screenWidth) / width).toInt
  override protected def topMargin: Int = (maxYTranslation / height).toInt
  override protected def bottomMargin: Int = ((maxYTranslation + screenHeight) / height).toInt

}
