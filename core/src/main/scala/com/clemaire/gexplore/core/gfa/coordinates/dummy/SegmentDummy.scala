package com.clemaire.gexplore.core.gfa.coordinates.dummy

class SegmentDummy(id: Int,
                   override val layerStart: Int,
                   override val layerEnd: Int,
                   _incoming: Int,
                   _outgoing: Int)
  extends DummyNode(id, layerStart, _incoming, _outgoing) {

  /**
    * @return `true` when this [[DummyNode]] is a segment,
    *         `false` otherwise.
    */
  override def isSegment: Boolean = true

}
