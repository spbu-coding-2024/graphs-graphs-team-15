package model.graph.io

import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import model.graph.WeightedDirectedGraph
import model.graph.WeightedUndirectedGraph
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.test.assertEquals

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
}
