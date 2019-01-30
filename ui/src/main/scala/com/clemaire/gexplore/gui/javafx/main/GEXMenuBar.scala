package com.clemaire.gexplore.gui.javafx.main

import com.clemaire.gexplore.gui.javafx.Controller
import javafx.scene.control.MenuBar

class GEXMenuBar
  extends MenuBar
    with Controller {

  override val path: String = "main/menu-bar.fxml"

  private val _: Unit = init()

}
