import kotlinx.serialization.*
import kotlinx.serialization.json.*
import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import model.graph.WeightedDirectedGraph
import model.graph.WeightedUndirectedGraph
import java.io.File

import model.graph.base.Graph
import model.graph.io.GraphDto
import model.graph.io.GraphType
import model.graph.io.convertToDto

fun saveGraphToJson(graph: Graph<String, String>, file: File) {
    val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    file.writeText(json.encodeToString(convertToDto(graph)))
}

fun loadGraphFromJson(file: File): Graph<String, String> {
    val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    val dto = json.decodeFromString<GraphDto<String, String>>(file.readText())

    return when (dto.type) {
        GraphType.UNDIRECTED -> UndirectedGraph<String, String>().apply {
            dto.vertices.forEach { addVertex(it.label) }
            dto.edges.forEach { addEdge(it.first, it.second, it.element) }
        }
        GraphType.DIRECTED -> DirectedGraph<String, String>().apply {
            dto.vertices.forEach { addVertex(it.label) }
            dto.edges.forEach { addEdge(it.first, it.second, it.element) }
        }
        GraphType.WEIGHTED_UNDIRECTED -> WeightedUndirectedGraph<String, String>().apply {
            dto.vertices.forEach { addVertex(it.label) }
            dto.edges.forEach { addEdge(it.first, it.second, it.element, it.weight ?: 0.0) }
        }
        GraphType.WEIGHTED_DIRECTED -> WeightedDirectedGraph<String, String>().apply {
            dto.vertices.forEach { addVertex(it.label) }
            dto.edges.forEach { addEdge(it.first, it.second, it.element, it.weight ?: 0.0) }
        }
    }
}
