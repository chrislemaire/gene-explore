package com.clemaire.gexplore.gui.javafx

import com.clemaire.gexplore.gui.javafx.common.{CollapsablePane, Side}
import javafx.application.Application
import javafx.scene.layout.{AnchorPane, Pane}
import javafx.scene.Scene
import javafx.stage.Stage

class MainApplication
  extends Application {

  val root: Pane = new AnchorPane()

  override def start(stage: Stage): Unit = {
    val scene = new Scene(root, 400, 300)
    root.getChildren.add(new CollapsablePane(Side.Right))

    stage.setTitle("G-Explore")
    stage.setScene(scene)
    stage.show()
  }

}
