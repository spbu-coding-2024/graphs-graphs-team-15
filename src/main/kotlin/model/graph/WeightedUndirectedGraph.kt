package model.graph

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.graph.weighted.WeightedVertex
import model.graph.weighted.WeightedEdge
import model.graph.weighted.WeightedGraph

@Serializable
private data class WeightedUndirectedVertex<V>(override var label: V) : WeightedVertex<V>

@Serializable
private data class WeightedUndirectedEdge<V, E>(
    override var element: E,
    val first: WeightedVertex<V>,
    val second: WeightedVertex<V>,
    override var weight: Double
) : WeightedEdge<E, V> {
    override val vertices
        get() = first to second
}

@Serializable
@SerialName("WeightedUndirectedGraph")
internal class WeightedUndirectedGraph<E, V> : WeightedGraph<E, V> {
    private val _vertices = hashMapOf<V, WeightedVertex<V>>()
    private val _edges = hashMapOf<E, WeightedEdge<E, V>>()

    override val vertices: Collection<WeightedVertex<V>>
        get() = _vertices.values

    override val edges: Collection<WeightedEdge<E, V>>
        get() = _edges.values

    override fun addVertex(v: V): WeightedVertex<V> =
        _vertices.getOrPut(v) { WeightedUndirectedVertex(v) }

    override fun addEdge(u: V, v: V, e: E): WeightedEdge<E, V> {
        return addEdge(u, v, e, 0.0)
    }

    override fun addEdge(u: V, v: V, e: E, weight: Double): WeightedEdge<E, V> {
        val first = addVertex(u)
        val second = addVertex(v)
        val edge = WeightedUndirectedEdge(e, first, second, weight)

        return _edges.getOrPut(e) { edge }
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

    override fun setEdgeWeight(e: E, weight: Double) {
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
