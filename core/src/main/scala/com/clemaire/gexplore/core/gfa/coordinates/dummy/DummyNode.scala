package com.clemaire.gexplore.core.gfa.coordinates.dummy

import com.clemaire.gexplore.core.gfa.coordinates.data.splay.SplayNode

class DummyNode(val id: Int,
                val layer: Int,
                _incoming: Int,
                _outgoing: Int)
  extends OrderNode {

  /**
    * The list of outgoing node connections. Node connections
    * are indicated by the connected node IDs.
    */
  override val outgoing: Set[Int] = Set(_outgoing)

  /**
    * The list of incoming node connections. Node connections
    * are indicated by the connected node IDs.
    */
  override val incoming: Set[Int] = Set(_incoming)

  /**
    * @return `true` when this [[OrderNode]] is a dummy-node,
    *         `false` otherwise.
    */
  override def isDummy: Boolean = true


  /**
    * @return `true` when this [[DummyNode]] is a segment,
    *         `false` otherwise.
    */
  def isSegment: Boolean = false

  /**
    * The node this SegmentDummy is represented
    * by in the splay-tree.
    */
  def splayNode: Option[SplayNode[SegmentDummy]] = None

  /**
    * @return The layer this [[DummyNode]] starts in. Only
    *         relevant for segments.
    */
  def layerStart: Int = layer

  /**
    * @return The layer this [[DummyNode]] ends in. Only
    *         relevant for segments.
    */
  def layerEnd: Int = layer

}
