package com.clemaire.gexplore.gui.javafx.common

import com.clemaire.gexplore.gui.javafx.common.Side.Side
import com.jfoenix.controls.JFXButton
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.layout.{AnchorPane, HBox, Pane}
import javafx.scene.Node

object Side {

  sealed class Side

  object Left extends Side

  object Right extends Side

}

//noinspection VarCouldBeVal
class CollapsablePane(side: Side = Side.Left)
  extends Pane {

  private val _: Unit = {
    val loader = new FXMLLoader()
    loader.setRoot(this)
    loader.setController(this)

    loader.load(getClass.getResourceAsStream("/javafx/fxml/common/collapsable-pane.fxml"))

    setSide(side)
  }

  @FXML
  private var topBar: HBox = _

  @FXML
  private var editableTop: HBox = _

  @FXML
  private var content: AnchorPane = _

  @FXML
  private var collapseBtn: JFXButton = _

  private var lastIndex: Int = _

  def setSide(side: Side): Unit = {
    topBar.getChildren.clear()

    side match {
      case Side.Left =>
        topBar.getChildren.addAll(editableTop, collapseBtn)
      case Side.Right =>
        topBar.getChildren.addAll(collapseBtn, editableTop)
      case _ =>
        throw new RuntimeException(s"Invalid Side: $side")
    }
  }

  def setTopBar(addedContent: Node): Unit = {
    topBar.getChildren.clear()
    topBar.getChildren.add(addedContent)
  }

  def setContent(addedContent: Node): Unit = {
    content.getChildren.clear()
    content.getChildren.add(content)
  }

  @FXML
  def collapse(): Unit = {
    lastIndex = getParent.getChildrenUnmodifiable.indexOf(this)
    getParent.asInstanceOf[Pane].getChildren.remove(this)
  }

  def show(index: Int = lastIndex): Unit = {
    getParent.asInstanceOf[Pane].getChildren.add(index, this)
  }

}
