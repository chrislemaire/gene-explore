package com.clemaire.gexplore.gui.javafx

import com.clemaire.gexplore.gui.javafx.main.MainScreen
import javafx.application.Application
import javafx.scene.layout.Pane
import javafx.scene.Scene
import javafx.stage.Stage

class MainApplication
  extends Application {

  val root: Pane = new MainScreen()

  override def start(stage: Stage): Unit = {
    val scene = new Scene(root, 900, 600)
    root.getStyleClass.add("panel-primary")

    scene.getStylesheets.add("org/kordamp/bootstrapfx/bootstrapfx.css")

    stage.setTitle("G-Explore")
    stage.setMinWidth(500.0)
    stage.setMinHeight(400.0)
    stage.setScene(scene)
    stage.show()
  }

}
