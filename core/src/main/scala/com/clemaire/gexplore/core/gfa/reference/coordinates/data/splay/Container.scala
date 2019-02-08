package com.clemaire.gexplore.core.gfa.reference.coordinates.data.splay

import com.clemaire.gexplore.core.gfa.reference.coordinates.crossing.AlternatingEntry
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.{AlternatingNode, Segment}

class Container(var root: Option[SplayNode[Segment]] = None)
  extends Object
    with AlternatingEntry[AlternatingNode, Container] {

  def this(elem: Segment) = this(Some(new SplayNode(elem)))
  def this(node: SplayNode[Segment]) = this(Some(node))

  private val _: Unit = root.foreach(_.container = Some(this))

  /**
    * Binary searches the splay-tree for a node with the
    * given index. Passes on the next node to search and its
    * index if the current node is not the target.
    *
    * @param index     The index to find.
    * @param node      The current node to search.
    * @param currIndex The index of the current node.
    * @return The Node with the given index if one exists.
    */
  private def get(index: Int,
                  node: SplayNode[Segment],
                  currIndex: Int): Option[SplayNode[Segment]] =
    if (currIndex == index) Some(node.splay(this))
    else if (index < currIndex) node.left.flatMap(l => get(index, l, l.index(currIndex)))
    else node.right.flatMap(r => get(index, r, r.index(currIndex)))

  /**
    * Looks up the node with the given index and returns
    * it if one is found.
    *
    * @param index The index of the node to look for.
    * @return The target node if it exists.
    */
  def get(index: Int): Option[SplayNode[Segment]] =
    root.flatMap(r =>
      get(index + 1, r, r.index).flatMap(sn => {
        root = Some(sn)
        root
      })
    )

  def append(elem: Segment): Unit = {
    root = Some(root.fold(new SplayNode(elem))(_.append(elem)(this)))
    elem.node = root
  }

  def join(other: Container): Container = {
    other.root.foreach(otherRoot =>
      root = Some(root.fold(otherRoot)(_.join(otherRoot)(this))))
    this
  }

  def split(at: Segment): Container = {
    val split = at.node.flatMap(_.split(this))
    root = at.node

    split.foreach(_.right.foreach(_.parent = None))
    new Container(split.flatMap(_.right))
  }

  def split(index: Int): Container = {
    val atIndex = get(index)
    root = atIndex
    atIndex.map(_.split(this))
      .fold(new Container())(new Container(_))
  }

  def first: Option[Segment] = root.map(_.leftmost(this).value)

  def last: Option[Segment] = root.map(_.rightmost(this).value)

  def size: Int = root.fold(0)(_.nodeSize)

}
