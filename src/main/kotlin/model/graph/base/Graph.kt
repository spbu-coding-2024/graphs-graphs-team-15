package model.graph.base

interface Graph<E, V> {
    val vertices: Collection<Vertex<V>>
    val edges: Collection<Edge<E, V>>

    fun addVertex(v: V): Vertex<V>
    fun addEdge(u: V, v: V, e: E): Edge<E, V>

    fun removeEdge(e: E): Boolean
    fun removeVertex(v: V): Boolean

    fun clear()
}
