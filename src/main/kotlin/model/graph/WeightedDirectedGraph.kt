package model.graph

import model.graph.base.Vertex
import model.graph.base.Edge
import model.graph.weighted.WeightedEdge
import model.graph.weighted.WeightedGraph

internal class WeightedDirectedGraph<E, V> : WeightedGraph<E, V> {
    private val _vertices = hashMapOf<V, Vertex<V>>()
    private val _edges = hashMapOf<E, WeightedEdge<E, V>>()

    override val vertices: Collection<Vertex<V>>
        get() = _vertices.values

    override val edges: Collection<WeightedEdge<E, V>>
        get() = _edges.values

    override fun addVertex(v: V): Vertex<V> =
        _vertices.getOrPut(v) { WeightedDirectedVertex(v) }

    override fun addEdge(u: V, v: V, e: E): Edge<E, V> {
        return addEdge(u, v, e, 0.0)
    }

    override fun addEdge(u: V, v: V, e: E, weight: Double): WeightedEdge<E, V> {
        val first = addVertex(u)
        val second = addVertex(v)
        val edge = WeightedDirectedEdge(e, first, second, weight)

        return _edges.getOrPut(e) { edge }
    }

    override fun setEdgeWeight(e: E, weight: Double) {
        _edges[e]?.weight = weight
    }

    override fun getEdgeWeight(e: E): Double? {
        return _edges[e]?.weight
    }

    private data class WeightedDirectedVertex<V>(override var element: V) : Vertex<V>

    private data class WeightedDirectedEdge<V, E>(
        override var element: E,
        val from: Vertex<V>,
        val to: Vertex<V>,
        override var weight: Double
    ) : WeightedEdge<E, V> {
        override val vertices
            get() = from to to
    }
}
