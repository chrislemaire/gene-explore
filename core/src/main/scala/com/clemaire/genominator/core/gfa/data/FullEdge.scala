package com.clemaire.genominator.core.gfa.data

case class FullEdge(from: Node,
                    to: Node,
                    reversedFrom: Boolean,
                    reversedTo: Boolean,
                    options: Map[Int, Long])
