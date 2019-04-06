package com.clemaire.gexplore.gui.javafx.main

import com.clemaire.gexplore.gui.javafx.Controller
import javafx.scene.canvas.Canvas

class GraphCanvas
  extends Canvas
    with Controller {

  override val path: String = "main/graph-canvas.fxml"

  private val _: Unit = init()

}
