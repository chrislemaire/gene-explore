package com.clemaire.gexplore.gui.javafx

import javafx.application.Application
import javafx.scene.layout.{AnchorPane, Pane}
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Stage
import jfxtras.styles.jmetro8.JMetro

class MainApplication
  extends Application {

  val root: Pane = new AnchorPane()

  override def start(stage: Stage): Unit = {
    val scene = new Scene(root, 400, 300)
    root.getChildren.add(new Button("bla"))

    stage.setTitle("G-Explore")
    stage.setScene(scene)
    stage.show()

    new JMetro(JMetro.Style.LIGHT).applyTheme(root)
  }

}
