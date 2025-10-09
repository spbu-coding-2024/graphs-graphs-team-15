package model.graph

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WeightedDirectedGraphTest {
    @Test
    fun testCreateVertex() {
        val graph = WeightedDirectedGraph<String, String>()

        val v1 = graph.addVertex("A")
        val v2 = graph.addVertex("B")

        assertEquals(2, graph.vertices.size)
        assertTrue(graph.vertices.contains(v1))
        assertTrue(graph.vertices.contains(v2))
    }

    @Test
    fun testVertexNotDuplicates() {
        val graph = WeightedDirectedGraph<String, String>()

        val v1 = graph.addVertex("A")
        val v2 = graph.addVertex("A")

        assertEquals(1, graph.vertices.size)
        assertSame(v1, v2)
    }

    @Test
    fun testCreateEdgeWithWeight() {
        val graph = WeightedDirectedGraph<String, String>()

        graph.addVertex("A")
        graph.addVertex("B")

        val e = graph.addEdge("A", "B", "e1", 3.5)

        assertEquals(1, graph.edges.size)
        assertEquals(3.5, graph.getEdgeWeight("e1"))
        assertEquals("A", e.vertices.first.label)
        assertEquals("B", e.vertices.second.label)
    }

    @Test
    fun testEdgeNotDuplicates() {
        val graph = WeightedDirectedGraph<String, String>()

        graph.addVertex("A")
        graph.addVertex("B")

        val e1 = graph.addEdge("A", "B", "e1", 2.0)
        val e2 = graph.addEdge("A", "B", "e1", 5.0)

        assertEquals(1, graph.edges.size)
        assertSame(e1, e2)
        assertEquals(2.0, graph.getEdgeWeight("e1"))
    }

    @Test
    fun testSetEdgeWeight() {
        val graph = WeightedDirectedGraph<String, String>()
        graph.addVertex("A")
        graph.addVertex("B")

        graph.addEdge("A", "B", "e1", 1.0)
        graph.setEdgeWeight("e1", 10.0)

        assertEquals(10.0, graph.getEdgeWeight("e1"))
    }

    @Test
    fun testDeleteEdge() {
        val graph = WeightedDirectedGraph<String, String>()
        graph.addVertex("A")
        graph.addVertex("B")

        graph.addEdge("A", "B", "e1", 5.0)
        val removed = graph.removeEdge("e1")

        assertTrue(removed)
        assertTrue(graph.edges.isEmpty())
    }

    @Test
    fun testRemoveVertexAndIncidentEdges() {
        val graph = WeightedDirectedGraph<String, String>()
        graph.addEdge("A", "B", "e1", 2.0)
        graph.addEdge("B", "C", "e2", 3.0)

        val removed = graph.removeVertex("B")

        assertTrue(removed)
        assertEquals(2, graph.vertices.size)
        assertTrue(graph.edges.isEmpty()) // все рёбра были связаны с B
    }

    @Test
    fun testGetEdgeWeightReturnNull() {
        val graph = WeightedDirectedGraph<String, String>()

        assertNull(graph.getEdgeWeight("eX"))
    }
}
