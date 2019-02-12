package com.clemaire.gexplore.core.gfa.coordinates.dummy

import com.clemaire.gexplore.core.gfa.coordinates.data.splay.SplayNode

class SegmentDummy(id: Int,
                   override val layerStart: Int,
                   override val layerEnd: Int,
                   _incoming: Int,
                   _outgoing: Int)
  extends DummyNode(id, layerStart, _incoming, _outgoing) {

  /**
    * The node this SegmentDummy is represented
    * by in the splay-tree.
    */
  override var splayNode: Option[SplayNode[SegmentDummy]] = None

  /**
    * @return `true` when this [[DummyNode]] is a segment,
    *         `false` otherwise.
    */
  override def isSegment: Boolean = true

}
