package model.graph

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.graph.base.Edge
import model.graph.base.Graph
import model.graph.base.Vertex
import kotlin.collections.getOrPut

@Serializable
internal data class DirectedVertex<V>(override var label: V) : Vertex<V>

@Serializable
internal data class DirectedEdge<E, V>(
    override var element: E,
    val from: Vertex<V>,
    val to: Vertex<V>,
) : Edge<E, V> {
    override val vertices
        get() = from to to
}

@Serializable
@SerialName("DirectedGraph")
internal class DirectedGraph<E, V> : Graph<E, V> {
    private val _vertices = hashMapOf<V, Vertex<V>>()
    private val _edges = hashMapOf<E, Edge<E, V>>()

    override val vertices: Collection<Vertex<V>>
        get() = _vertices.values

    override val edges: Collection<Edge<E, V>>
        get() = _edges.values

    override fun addVertex(v: V): Vertex<V> = _vertices.getOrPut(v) { DirectedVertex(v) }

    override fun addEdge(
        u: V,
        v: V,
        e: E,
    ): Edge<E, V> {
        val from = addVertex(u)
        val to = addVertex(v)
        return _edges.getOrPut(e) { DirectedEdge(e, from, to) }
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

    override fun clear() {
        _vertices.clear()
        _edges.clear()
    }
}
