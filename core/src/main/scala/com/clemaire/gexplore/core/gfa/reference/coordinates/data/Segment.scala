package com.clemaire.gexplore.core.gfa.reference.coordinates.data

import com.clemaire.gexplore.core.gfa.data.Identifiable
import com.clemaire.gexplore.core.gfa.reference.coordinates.data.splay.SplayNode

class Segment(val id: Int,
              val p: AlternatingNode,
              val q: AlternatingNode)
  extends Identifiable {

  /**
    * The node this SplaySegment is represented
    * by in the splay-tree.
    */
  var node: Option[SplayNode[Segment]] = None
}