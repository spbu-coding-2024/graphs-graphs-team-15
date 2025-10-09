package model.graph.io

import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import model.graph.WeightedUndirectedGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File

class JsonSerializerTest {

    @Test
    fun testSaveAndLoadUndirectedGraph() {
        val g = UndirectedGraph<String, String>()
        g.addEdge("A", "B", "e1")
        g.addEdge("B", "C", "e2")

        val file = File.createTempFile("graph", ".json")
        saveGraphToJson(g, file)

        val loaded = loadGraphFromJson(file)

        assertTrue(loaded is UndirectedGraph)
        assertEquals(3, loaded.vertices.size)
        assertEquals(2, loaded.edges.size)
    }

    @Test
    fun testSaveAndLoadDirectedGraph() {
        val g = DirectedGraph<String, String>()
        g.addEdge("A", "B", "e1")

        val file = File.createTempFile("graph", ".json")
        saveGraphToJson(g, file)

        val loaded = loadGraphFromJson(file)

        assertTrue(loaded is DirectedGraph)
        assertEquals(2, loaded.vertices.size)
        assertEquals(1, loaded.edges.size)
    }

    @Test
    fun testSaveAndLoadWeightedUndirectedGraph() {
        val g = WeightedUndirectedGraph<String, String>()
        g.addEdge("U", "V", "edge", 42.0)

        val file = File.createTempFile("graph", ".json")
        saveGraphToJson(g, file)

        val loaded = loadGraphFromJson(file)

        assertTrue(loaded is WeightedUndirectedGraph)

        val newGraph = loaded as WeightedUndirectedGraph<String, String>
        assertEquals(42.0, newGraph.edges.first().weight, 0.0001)
    }

    @Test
    fun testSaveAndLoadWeightedDirectedGraph() {
        val g = WeightedUndirectedGraph<String, String>()
        g.addEdge("U", "V", "edge", 42.0)

        val file = File.createTempFile("graph", ".json")
        saveGraphToJson(g, file)

        val loaded = loadGraphFromJson(file)

        assertTrue(loaded is WeightedUndirectedGraph)

        val newGraph = loaded as WeightedUndirectedGraph<String, String>
        assertEquals(42.0, newGraph.edges.first().weight, 0.0001)
    }
}
