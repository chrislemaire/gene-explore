package com.clemaire.gexplore.gui.javafx.common

import com.clemaire.gexplore.gui.javafx.common.Side.Side
import com.clemaire.gexplore.gui.javafx.Controller
import javafx.fxml.FXML
import javafx.scene.layout.{HBox, Pane, VBox}
import javafx.scene.Node
import javafx.scene.control.{Button, SplitPane}

object Side {

  sealed class Side

  object Left extends Side

  object Right extends Side

}

//noinspection VarCouldBeVal
class CollapsablePane
  extends VBox
    with Controller {

  override val path: String = "common/collapsable-pane.fxml"

  private val _: Unit = init()

  @FXML
  private var topBar: HBox = _

  @FXML
  private var editableTop: HBox = _

  @FXML
  private var content: Pane = _

  @FXML
  private var collapseBtn: Button = _

  private var lastIndex: Int = _

  var parentSP: SplitPane = _

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

  def getTopBar: Node = topBar

  @FXML
  def collapse(): Unit = {
    lastIndex = parentSP.getItems.indexOf(this)
    parentSP.getItems.remove(this)
  }

  def show(index: Int = lastIndex): Unit = {
    parentSP.getItems.add(index, this)
  }

}
