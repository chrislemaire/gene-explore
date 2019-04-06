package com.clemaire.gexplore.gui.graph.grid

trait MovableGrid
  extends Object
    with Grid {

  def jumpTo(coords: (Double, Double)): Unit =
    jumpTo(coords._1, coords._2)
  def jumpTo(col: Double,
             row: Double): Unit = {
    _col = col
    _row = row
  }

  def translate(colTranslate: Double,
                rowTranslate: Double): Unit = {
    _col += colTranslate
    _row += rowTranslate
  }

}
