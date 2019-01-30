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
  private var collapseBtn: Button = _

  @FXML
  private var _top: Pane = _

  @FXML
  private var _content: Pane = _

  private var lastIndex: Int = _

  var parentSP: SplitPane = _

  def setSide(side: Side): Unit = {
    topBar.getChildren.clear()

    side match {
      case Side.Left =>
        topBar.getChildren.addAll(_top, collapseBtn)
      case Side.Right =>
        topBar.getChildren.addAll(collapseBtn, _top)
      case _ =>
        throw new RuntimeException(s"Invalid Side: $side")
    }
  }

  def setTop(top: Pane): Unit =
    _top = top

  def getTop: Node = _top

  def setContent(content: Pane): Unit =
    _content = content

  def getContent: Node = _content

  @FXML
  def collapse(): Unit = {
    lastIndex = parentSP.getItems.indexOf(this)
    parentSP.getItems.remove(this)
  }

  def show(index: Int = lastIndex): Unit = {
    parentSP.getItems.add(index, this)
  }

}
