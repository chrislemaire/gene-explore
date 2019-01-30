package com.clemaire.gexplore.gui.javafx

import javafx.fxml.FXMLLoader

trait Controller {
  val path: String

  protected def init(): Unit = {
    val loader = new FXMLLoader()
    loader.setRoot(this)
    loader.setController(this)

    loader.load(getClass.getResourceAsStream(
      Global.FXML_RES + path))
    Unit
  }

}
