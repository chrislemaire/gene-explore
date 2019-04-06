package com.clemaire.gexplore.gui.graph.grid

import javafx.beans.property.{BooleanProperty, SimpleBooleanProperty}

trait RedrawIndicator {
  val redrawNeededProperty: BooleanProperty = new SimpleBooleanProperty(false)

  def redrawNeeded: Boolean = redrawNeededProperty.get()
  def requireRedraw(): Unit = redrawNeededProperty.setValue(true)
  def redrawDone(): Unit = redrawNeededProperty.setValue(false)
}
