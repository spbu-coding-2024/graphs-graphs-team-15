package model.graph.io

import model.graph.UndirectedGraph
import model.graph.DirectedGraph
import model.graph.WeightedUndirectedGraph
import model.graph.WeightedDirectedGraph
import model.graph.base.Graph

internal fun <E, V> convertToDto(graph: Graph<E, V>): GraphDto<E, V> {
    val vertices = graph.vertices.map { VertexDto(it.label) }
    val edges = graph.edges.map { edge ->
        val (from, to) = edge.vertices
        val weight = when (edge) {
            is model.graph.weighted.WeightedEdge<E, V> -> edge.weight
            else -> null
        }
        EdgeDto(edge.element, from.label, to.label, weight)
    }

    val type = when (graph) {
        is UndirectedGraph -> GraphType.UNDIRECTED
        is DirectedGraph -> GraphType.DIRECTED
        is WeightedUndirectedGraph -> GraphType.WEIGHTED_UNDIRECTED
        is WeightedDirectedGraph -> GraphType.WEIGHTED_DIRECTED
        else -> throw IllegalArgumentException()
    }
    return GraphDto(type, vertices, edges)
}
