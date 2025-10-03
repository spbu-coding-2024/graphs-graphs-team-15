package model.graph

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.graph.base.Vertex
import model.graph.base.Edge
import model.graph.base.Graph

@Serializable
internal data class UndirectedVertex<V>(override var label: V) : Vertex<V>

@Serializable
internal data class UndirectedEdge<E, V>(
    override var element: E,
    var first: Vertex<V>,
    var second: Vertex<V>,
) : Edge<E, V> {
    override val vertices
        get() = first to second
}
@Serializable
@SerialName("UndirectedGraph")
internal class UndirectedGraph<E, V> : Graph<E, V> {
    private val _vertices = hashMapOf<V, Vertex<V>>()
    private val _edges = hashMapOf<E, Edge<E, V>>()

    override val vertices: Collection<Vertex<V>>
        get() = _vertices.values

    override val edges: Collection<Edge<E, V>>
        get() = _edges.values

    override fun addVertex(v: V): Vertex<V> = _vertices.getOrPut(v) { UndirectedVertex(v) }

    override fun addEdge(u: V, v: V, e: E): Edge<E, V> {
        val first = addVertex(u)
        val second = addVertex(v)
        return _edges.getOrPut(e) { UndirectedEdge(e, first, second) }
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
}
