package model.graph

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WeightedUndirectedGraphTest {
    @Test
    fun testCreateVertex() {
        val graph = WeightedUndirectedGraph<String, String>()

        val v1 = graph.addVertex("A")
        val v2 = graph.addVertex("B")

        assertEquals(2, graph.vertices.size)
        assertTrue(graph.vertices.contains(v1))
        assertTrue(graph.vertices.contains(v2))
    }

    @Test
    fun testVertexNotDuplicates() {
        val graph = WeightedUndirectedGraph<String, String>()

        val v1 = graph.addVertex("A")
        val v2 = graph.addVertex("A")

        assertEquals(1, graph.vertices.size)
        assertSame(v1, v2)
    }

    @Test
    fun testCreateEdgeWithWeight() {
        val graph = WeightedUndirectedGraph<String, String>()

        graph.addVertex("A")
        graph.addVertex("B")

        val e = graph.addEdge("A", "B", "e1", 3.5)

        assertEquals(1, graph.edges.size)
        assertEquals(3.5, graph.getEdgeWeight("e1"))
        assertEquals("A", e.vertices.first.label)
        assertEquals("B", e.vertices.second.label)
    }

    @Test
    fun testSetEdgeWeight() {
        val graph = WeightedUndirectedGraph<String, String>()

        graph.addVertex("A")
        graph.addVertex("B")

        graph.addEdge("A", "B", "e1")

        graph.setEdgeWeight("e1", 10.0)
        graph.setEdgeWeight("e2", 20.0)

        assertEquals(1, graph.edges.size)
        assertEquals(10.0, graph.getEdgeWeight("e1"))
    }

    @Test
    fun testEdgeNotDuplicates() {
        val graph = WeightedUndirectedGraph<String, String>()

        graph.addVertex("A")
        graph.addVertex("B")

        val e1 = graph.addEdge("A", "B", "e1", 2.0)
        val e2 = graph.addEdge("A", "B", "e1", 5.0)

        assertEquals(1, graph.edges.size)
        assertSame(e1, e2)
        assertEquals(2.0, graph.getEdgeWeight("e1"))
    }

    @Test
    fun testDeleteEdge() {
        val graph = WeightedUndirectedGraph<String, String>()
        graph.addVertex("A")
        graph.addVertex("B")

        graph.addEdge("A", "B", "e1", 5.0)
        val removedTrue = graph.removeEdge("e1")
        val removedFalse = graph.removeEdge("e2")

        assertTrue(removedTrue)
        assertFalse(removedFalse)
        assertTrue(graph.edges.isEmpty())
    }

    @Test
    fun testRemoveVertexAndIncidentEdges() {
        val graph = WeightedUndirectedGraph<String, String>()
        graph.addEdge("A", "B", "e1", 2.0)
        graph.addEdge("B", "C", "e2", 3.0)

        val removedTrue = graph.removeVertex("B")
        val removedFalse = graph.removeVertex("D")

        assertTrue(removedTrue)
        assertFalse(removedFalse)
        assertEquals(2, graph.vertices.size)
        assertTrue(graph.edges.isEmpty())
    }

    @Test
    fun testGetEdgeWeightReturnNull() {
        val graph = WeightedUndirectedGraph<String, String>()

        assertNull(graph.getEdgeWeight("eX"))
    }

    @Test
    fun testClearGraph() {
        val graph = WeightedUndirectedGraph<String, String>()
        graph.addVertex("A")
        graph.addVertex("B")

        graph.addEdge("A", "B", "e1", 5.0)

        graph.clear()

        assertTrue(graph.vertices.isEmpty())
        assertTrue(graph.edges.isEmpty())
    }
}
