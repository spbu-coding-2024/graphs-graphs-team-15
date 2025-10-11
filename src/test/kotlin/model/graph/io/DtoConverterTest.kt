package model.graph.io

import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import model.graph.WeightedDirectedGraph
import model.graph.WeightedUndirectedGraph
import model.graph.base.Edge
import model.graph.base.Graph
import model.graph.base.Vertex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DtoConverterTest {
    @Test
    fun testConvertUndirectedGraph() {
        val g = UndirectedGraph<Int, String>()
        g.addEdge("A", "B", 1)

        val dto = convertToDto(g)

        assertEquals(GraphType.UNDIRECTED, dto.type)
        assertEquals(setOf("A", "B"), dto.vertices.map { it.label }.toSet())
        assertEquals(1, dto.edges.first().element)
        assertNull(dto.edges.first().weight)
    }

    @Test
    fun testConvertDirectedGraph() {
        val g = DirectedGraph<Int, String>()
        g.addEdge("A", "B", 69)

        val dto = convertToDto(g)

        assertEquals(GraphType.DIRECTED, dto.type)
        assertEquals(setOf("A", "B"), dto.vertices.map { it.label }.toSet())
        assertEquals(69, dto.edges.first().element)
    }

    @Test
    fun testConvertWeightedUndirectedGraph() {
        val g = WeightedUndirectedGraph<Int, String>()
        g.addEdge("A", "B", 96, 3.14)

        val dto = convertToDto(g)

        assertEquals(GraphType.WEIGHTED_UNDIRECTED, dto.type)
        assertEquals(setOf("A", "B"), dto.vertices.map { it.label }.toSet())
        assertEquals(96, dto.edges.first().element)
        assertEquals(3.14, dto.edges.first().weight)
    }

    @Test
    fun testConvertWeightedDirectedGraph() {
        val g = WeightedDirectedGraph<Int, String>()
        g.addEdge("A", "B", 96, 3.14)

        val dto = convertToDto(g)

        assertEquals(GraphType.WEIGHTED_DIRECTED, dto.type)
        assertEquals(setOf("A", "B"), dto.vertices.map { it.label }.toSet())
        assertEquals(96, dto.edges.first().element)
        assertEquals(3.14, dto.edges.first().weight)
    }

    @Test
    fun testConvertUnknownGraphType() {
        val unknownGraph =
            object : Graph<String, String> {
                override val vertices: Collection<Vertex<String>> = emptyList()
                override val edges: Collection<Edge<String, String>> = emptyList()

                override fun addEdge(
                    u: String,
                    v: String,
                    e: String,
                ): Edge<String, String> {
                    TODO("Not yet implemented")
                }

                override fun addVertex(v: String): Vertex<String> {
                    TODO("Not yet implemented")
                }

                override fun removeEdge(e: String): Boolean {
                    TODO("Not yet implemented")
                }

                override fun removeVertex(v: String): Boolean {
                    TODO("Not yet implemented")
                }

                override fun clear() {
                    TODO("Not yet implemented")
                }
            }

        assertThrows<IllegalArgumentException> {
            convertToDto(unknownGraph)
        }
    }
}
