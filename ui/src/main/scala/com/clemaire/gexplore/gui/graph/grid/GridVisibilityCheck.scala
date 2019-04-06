package com.clemaire.gexplore.gui.graph.grid

trait GridVisibilityCheck
  extends Object
    with Grid {

  protected def leftMargin: Int
  protected def rightMargin: Int
  protected def topMargin: Int
  protected def bottomMargin: Int

  def scopedColumns(columnsShown: Int): Traversable[Int] =
    col.floor.toInt - leftMargin to
      col.floor.toInt + columnsShown + rightMargin + 1

  def scopedRows(rowsShown: Int): Traversable[Int] =
    row.floor.toInt - topMargin to
      row.floor.toInt + rowsShown + bottomMargin + 1

}
