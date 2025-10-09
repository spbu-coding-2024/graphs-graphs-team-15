package model.graph.weighted

import model.graph.base.Edge

interface WeightedEdge<E, V> : Edge<E, V> {
    var weight: Double
}
