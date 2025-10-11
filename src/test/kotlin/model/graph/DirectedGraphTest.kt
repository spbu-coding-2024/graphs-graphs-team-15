package model.graph

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DirectedGraphTest {
    @Test
    fun testCreateVertex() {
        val graph = DirectedGraph<String, String>()
        val v1 = graph.addVertex("A")
        val v2 = graph.addVertex("b")

        assertEquals(2, graph.vertices.size)
        assertTrue(graph.vertices.contains(v1))
        assertTrue(graph.vertices.contains(v2))
    }

    @Test
    fun testVertexNotDuplicates() {
        val graph = DirectedGraph<String, String>()

        val v1 = graph.addVertex("A")
        val v2 = graph.addVertex("A")

        assertEquals(1, graph.vertices.size)
        assertSame(v1, v2)
    }

    @Test
    fun testCreateEdge() {
        val graph = DirectedGraph<String, String>()

        val v1 = graph.addVertex("A")
        val v2 = graph.addVertex("B")

        val e1 = graph.addEdge("A", "B", "edge1")

        assertEquals(1, graph.edges.size)
        assertTrue(e1.incident(v1))
        assertTrue(e1.incident(v2))
        assertEquals(Pair(v1, v2), e1.vertices)
    }

    @Test
    fun testEdgeNotDuplicates() {
        val graph = DirectedGraph<String, String>()

        val e1 = graph.addEdge("A", "B", "edge1")
        val e2 = graph.addEdge("A", "B", "edge1")

        assertEquals(1, graph.edges.size)
        assertSame(e1, e2)
    }

    @Test
    fun testIncidentDetectOnlyCorrectVertices() {
        val graph = DirectedGraph<String, String>()
        val v1 = graph.addVertex("X")
        val v2 = graph.addVertex("Y")
        val v3 = graph.addVertex("Z")

        val edge = graph.addEdge("X", "Y", "e")

        assertTrue(edge.incident(v1))
        assertTrue(edge.incident(v2))
        assertFalse(edge.incident(v3))
    }

    @Test
    fun testRemoveEdgeDeleteEdge() {
        val graph = DirectedGraph<String, String>()
        graph.addVertex("A")
        graph.addVertex("B")

        graph.addEdge("A", "B", "e1")
        val removed = graph.removeEdge("e1")

        assertTrue(removed)
        assertTrue(graph.edges.isEmpty())
    }

    @Test
    fun testRemoveUnexistentEdge() {
        val graph = DirectedGraph<String, String>()

        graph.addEdge("A", "B", "e1")
        val removed = graph.removeEdge("e2")

        assertFalse(removed)
        assertTrue(!graph.edges.isEmpty())
    }

    @Test
    fun testRemoveVertexDeleteVertexAndIncidentEdges() {
        val graph = DirectedGraph<String, String>()

        graph.addEdge("A", "B", "e1")
        graph.addEdge("B", "C", "e2")
        val removed = graph.removeVertex("B")

        assertTrue(removed)
        assertEquals(2, graph.vertices.size)
        assertTrue(graph.edges.isEmpty())
    }

    @Test
    fun testRemoveUnexistentVertex() {
        val graph = DirectedGraph<String, String>()

        graph.addEdge("A", "B", "e1")
        val removed = graph.removeVertex("C")

        assertFalse(removed)
        assertEquals(2, graph.vertices.size)
    }

    @Test
    fun testClearAll() {
        val graph = DirectedGraph<String, String>()

        graph.addEdge("A", "B", "e1")
        graph.addEdge("B", "C", "e2")
        graph.addEdge("C", "D", "e3")

        graph.clear()

        assertTrue(graph.vertices.isEmpty())
        assertTrue(graph.edges.isEmpty())
    }

    @Test
    fun testClearAllOnEmptyGraph() {
        val graph = DirectedGraph<String, String>()
        graph.clear()

        assertTrue(graph.vertices.isEmpty())
        assertTrue(graph.edges.isEmpty())
    }
}
