package com.clemaire.gexplore.gui.graph

import com.clemaire.gexplore.core.gfa.data.GraphData
import com.clemaire.gexplore.gui.javafx.Controller
import javafx.scene.canvas.Canvas

class GraphCanvas(val graph: GraphData)
  extends Canvas
    with Controller {

  override val path: String = "main/graph-canvas.fxml"

  private val _: Unit = init()

  private val grid: MovableGraphicsGrid =
    new MovableGraphicsGrid(getGraphicsContext2D)

}
