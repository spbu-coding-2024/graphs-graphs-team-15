package model.graph

import kotlin.collections.getOrPut

import model.graph.base.Vertex
import model.graph.base.Edge
import model.graph.base.Graph

internal class DirectedGraph<E, V> : Graph<E, V> {
    private val _vertices = hashMapOf<V, Vertex<V>>()
    private val _edges = hashMapOf<E, Edge<E, V>>()

    override val vertices: Collection<Vertex<V>>
        get() = _vertices.values

    override val edges: Collection<Edge<E, V>>
        get() = _edges.values

    override fun addVertex(v: V): Vertex<V> = _vertices.getOrPut(v) { DirectedVertex(v) }

    override fun addEdge(u: V, v: V, e: E): Edge<E, V> {
        val from = addVertex(u)
        val to = addVertex(v)
        return _edges.getOrPut(e) { DirectedEdge(e, from, to) }
    }

    private data class DirectedVertex<V>(override var element: V) : Vertex<V>

    private data class DirectedEdge<E, V>(
        override var element: E,
        val from: Vertex<V>,
        val to: Vertex<V>,
    ) : Edge<E, V> {
        override val vertices
            get() = from to to
    }
}
