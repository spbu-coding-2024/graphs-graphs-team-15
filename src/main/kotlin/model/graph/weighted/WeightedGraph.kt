package model.graph.weighted

import model.graph.base.Graph

interface WeightedGraph<E, V> : Graph<E, V> {
    fun setEdgeWeight(
        e: E,
        weight: Double,
    )

    fun getEdgeWeight(e: E): Double?

    fun addEdge(
        u: V,
        v: V,
        e: E,
        weight: Double,
    ): WeightedEdge<E, V>
}
