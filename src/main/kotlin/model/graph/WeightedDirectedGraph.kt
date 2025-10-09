package model.graph

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.graph.base.Edge
import model.graph.base.Vertex
import model.graph.weighted.WeightedEdge
import model.graph.weighted.WeightedGraph

@Serializable
internal data class WeightedDirectedVertex<V>(override var label: V) : Vertex<V>

@Serializable
internal data class WeightedDirectedEdge<V, E>(
    override var element: E,
    val from: Vertex<V>,
    val to: Vertex<V>,
    override var weight: Double,
) : WeightedEdge<E, V> {
    override val vertices
        get() = from to to
}

@Serializable
@SerialName("WeightedDirectedGraph")
internal class WeightedDirectedGraph<E, V> : WeightedGraph<E, V> {
    private val _vertices = hashMapOf<V, Vertex<V>>()
    private val _edges = hashMapOf<E, WeightedEdge<E, V>>()

    override val vertices: Collection<Vertex<V>>
        get() = _vertices.values

    override val edges: Collection<WeightedEdge<E, V>>
        get() = _edges.values

    override fun addVertex(v: V): Vertex<V> = _vertices.getOrPut(v) { WeightedDirectedVertex(v) }

    override fun addEdge(
        u: V,
        v: V,
        e: E,
    ): Edge<E, V> {
        return addEdge(u, v, e, 0.0)
    }

    override fun removeEdge(e: E): Boolean {
        return _edges.remove(e) != null
    }

    override fun removeVertex(v: V): Boolean {
        val vertex = _vertices.remove(v) ?: return false
        val toRemove = _edges.filterValues { it.incident(vertex) }.keys
        toRemove.forEach { _edges.remove(it) }
        return true
    }

    override fun addEdge(
        u: V,
        v: V,
        e: E,
        weight: Double,
    ): WeightedEdge<E, V> {
        val first = addVertex(u)
        val second = addVertex(v)
        val edge = WeightedDirectedEdge(e, first, second, weight)

        return _edges.getOrPut(e) { edge }
    }

    override fun setEdgeWeight(
        e: E,
        weight: Double,
    ) {
        _edges[e]?.weight = weight
    }

    override fun getEdgeWeight(e: E): Double? {
        return _edges[e]?.weight
    }

    override fun clear() {
        _vertices.clear()
        _edges.clear()
    }
}
