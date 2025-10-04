package model.graph.io

import kotlinx.serialization.Serializable

@Serializable
enum class GraphType {
    UNDIRECTED,
    DIRECTED,
    WEIGHTED_UNDIRECTED,
    WEIGHTED_DIRECTED
}

@Serializable
data class VertexDto<V>(
    val label: V
)

@Serializable
data class EdgeDto<E, V>(
    val element: E,
    val first: V,
    val second: V,
    val weight: Double? = null
)

@Serializable
data class GraphDto<E, V>(
    val type: GraphType,
    val vertices: List<VertexDto<V>>,
    val edges: List<EdgeDto<E, V>>
)
