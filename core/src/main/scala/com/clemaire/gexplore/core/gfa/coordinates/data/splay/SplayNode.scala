package com.clemaire.gexplore.core.gfa.coordinates.data.splay

class SplayNode[V](val value: V) {

  /**
    * The left subtree of this Node.
    */
  var left: Option[SplayNode[V]] = None

  /**
    * The right subtree of this Node.
    */
  var right: Option[SplayNode[V]] = None

  /**
    * The parent of this Node if one exists.
    */
  var parent: Option[SplayNode[V]] = None

  /**
    * The container this node is a part of.
    */
  var container: Option[Container] = None

  /**
    * The size of this node's subtrees +1.
    */
  var nodeSize: Int = calculateSize()

  /**
    * Calculates the index of this Node based on
    * the size of subtrees and the index of the parent node.
    *
    * @return The index of this Node.
    */
  def index: Int = indexImpl - 1

  /**
    * Calculates the index of this Node based on
    * the size of subtrees and the index of the parent node.
    *
    * @param parentIndex The index of the parent node.
    * @return The index of this Node.
    */
  def index(parentIndex: Int): Int = indexImpl(parentIndex) - 1

  /**
    * Calculates the 1-based index of this Node based on
    * the size of subtrees and the index of the parent
    * node.
    *
    * @return The 1-based index of this Node.
    */
  private def indexImpl: Int = indexImpl(parent.fold(0)(_.indexImpl))

  /**
    * Calculates the 1-based index of this Node based on
    * the size of subtrees and the index of the parent
    * node.
    *
    * @param parentIndex The index of the parent node.
    * @return The 1-based index of this Node.
    */
  private def indexImpl(parentIndex: Int): Int =
    if (parent.flatMap(_.left).contains(this)) {
      parentIndex - right.fold(0)(_.nodeSize) - 1
    } else {
      parentIndex + left.fold(0)(_.nodeSize) + 1
    }

  /**
    * Appends the given element to this splay-tree by setting
    * it as the rightmost value.
    *
    * @param elem The element to add.
    * @return The resulting tree, splayed at the added element.
    */
  def append(elem: V)(implicit c: Container): SplayNode[V] =
    join(new SplayNode(elem))

  /**
    * Appends a splay-tree Node to this SplayNode
    * on the rightmost side of the tree. This ensures
    * The added tree maintains order and is now at the end
    * of this tree.
    *
    * @param node The SplayNode to append.
    * @return The resulting tree, splayed at the added node.
    */
  def join(node: SplayNode[V])(implicit c: Container): SplayNode[V] = {
    val last = rightmost

    last.right = Some(node)
    node.parent = Some(last)

    node.splay
  }

  /**
    * Splits the tree at the current node and returns
    * a pair of the left disconnected subtree and the
    * right disconnected subtree of this Node.
    *
    * @return The left and right splits of this Node.
    */
  def split(implicit c: Container): Option[SplayNode[V]] = {
    splay

    val splitOff = right
    right.foreach(_.parent = None)
    right = None

    splitOff
  }

  def leftmost(implicit c: Container): SplayNode[V] =
    left.fold(splay)(_.leftmost)

  /**
    * Traverses the right-hand subtree of this Node until
    * the right-hand tree of a subsequent Node is empty.
    * This last node is splayed and returned.
    *
    * @return The right-most node below this Node.
    */
  def rightmost(implicit c: Container): SplayNode[V] =
    right.fold(splay)(_.rightmost)

  /**
    * @return The container this Node is in.
    */
  def getContainer: Container = parent.fold(container.get)(_.getContainer)

  /**
    * Calculates the size of this node based on the size
    * of its children.
    *
    * @return The current size of the list of elements
    *         below and including this node.
    */
  def calculateSize(): Int =
    left.map(_.nodeSize).getOrElse(0) +
      right.map(_.nodeSize).getOrElse(0) + 1

  /**
    * Replaces the child matching the given node with the
    * second given node if one such child exists.
    *
    * @param child The child to find.
    * @param x     The node to replace the child with.
    */
  private def replace(child: SplayNode[V],
                      x: SplayNode[V]): Unit =
    if (left.contains(child)) left = Some(x)
    else if (right.contains(child)) right = Some(x)

  /**
    * Rotates this node up by swapping it over the
    * edge it has with its parent. If no parent exists,
    * no swapping is done. The direction of rotation
    * is determined based on which side of the parent this
    * node is at.
    *
    * @return This SplayNode after rotating it up.
    */
  def rotateUp(): SplayNode[V] = {
    parent.foreach(p => {
      if (p.left.contains(this)) {
        p.left = right
        p.left.foreach(_.parent = Some(p))
        right = Some(p)
      } else {
        p.right = left
        p.right.foreach(_.parent = Some(p))
        left = Some(p)
      }

      parent = p.parent
      p.parent = Some(this)

      parent.foreach(_.replace(p, this))

      nodeSize = p.nodeSize
      p.nodeSize = p.calculateSize()
    })

    this
  }

  /**
    * Splays the current node. This operation guarantees
    * this Node to be the root of the tree after splaying.
    *
    * @return This node, now the root of the tree.
    */
  def splay(implicit c: Container): SplayNode[V] = {
    container = Some(c)
    parent.map(p => {
      p.parent.map(g => {
        if (g.left.flatMap(_.left).contains(this) ||
          g.right.flatMap(_.right).contains(this)) {

          p.rotateUp()
          rotateUp().splay
        } else {
          rotateUp().rotateUp().splay
        }
      }).getOrElse(rotateUp())
    }).getOrElse(this)
  }

}
