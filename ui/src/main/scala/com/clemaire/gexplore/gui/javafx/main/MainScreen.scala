package com.clemaire.gexplore.gui.javafx.main

import com.clemaire.gexplore.gui.javafx.Controller
import com.clemaire.gexplore.gui.javafx.common.CollapsablePane
import javafx.fxml.FXML
import javafx.scene.control.SplitPane
import javafx.scene.layout.VBox

//noinspection VarCouldBeVal
class MainScreen
  extends VBox
    with Controller {

  override val path: String = "main/main-screen.fxml"

  private val _: Unit = init()

  @FXML
  private var mainSplitPane: SplitPane = _

  @FXML
  private var leftCollapsable: CollapsablePane = _

  @FXML
  private def initialize(): Unit = {
    leftCollapsable.parentSP = mainSplitPane
  }

}
