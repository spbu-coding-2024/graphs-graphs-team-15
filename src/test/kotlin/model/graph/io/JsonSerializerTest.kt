package model.graph.io

import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import model.graph.WeightedDirectedGraph
import model.graph.WeightedUndirectedGraph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class JsonSerializerTest {
    @Test
    fun testSaveAndLoadUndirectedGraph() {
        val g = UndirectedGraph<String, String>()
        g.addEdge("A", "B", "e1")
        g.addEdge("B", "C", "e2")

        val file = File.createTempFile("graph", ".json")
        saveGraphToJson(g, file)

        assertTrue(file.readText().contains("UNDIRECTED"))
        assertTrue(file.readText().contains("e1"))
        assertTrue(file.readText().contains("e2"))

        val loaded = loadGraphFromJson(file)

        assertTrue(loaded is UndirectedGraph)
        assertEquals(setOf("A", "B", "C"), loaded.vertices.map { it.label }.toSet())
        assertEquals(3, loaded.vertices.size)
        assertEquals(2, loaded.edges.size)
    }

    @Test
    fun testSaveAndLoadDirectedGraph() {
        val g = DirectedGraph<String, String>()
        g.addEdge("A", "B", "e1")
        g.addEdge("B", "C", "e2")

        val file = File.createTempFile("graph", ".json")
        saveGraphToJson(g, file)

        assertTrue(file.readText().contains("DIRECTED"))

        val loaded = loadGraphFromJson(file)

        assertTrue(loaded is DirectedGraph)
        assertEquals(setOf("A", "B", "C"), loaded.vertices.map { it.label }.toSet())
        assertEquals(3, loaded.vertices.size)
        assertEquals(2, loaded.edges.size)
    }

    @Test
    fun testSaveAndLoadWeightedUndirectedGraph() {
        val g = WeightedUndirectedGraph<String, String>()
        g.addEdge("A", "B", "e1", 42.0)
        g.addEdge("B", "C", "e2", 20.0)

        val file = File.createTempFile("graph", ".json")
        saveGraphToJson(g, file)

        assertTrue(file.readText().contains("WEIGHTED_UNDIRECTED"))

        val loaded = loadGraphFromJson(file)

        assertTrue(loaded is WeightedUndirectedGraph)

        val newGraph = loaded as WeightedUndirectedGraph<String, String>
        assertEquals(62.0, newGraph.edges.sumOf { it.weight }, 0.0001)
    }

    @Test
    fun testSaveAndLoadWeightedDirectedGraph() {
        val g = WeightedDirectedGraph<String, String>()
        g.addEdge("A", "B", "e1", 42.0)
        g.addEdge("B", "C", "e2", 20.0)

        val file = File.createTempFile("graph", ".json")
        saveGraphToJson(g, file)

        assertTrue(file.readText().contains("WEIGHTED_DIRECTED"))

        val loaded = loadGraphFromJson(file)

        assertTrue(loaded is WeightedDirectedGraph)

        val newGraph = loaded as WeightedDirectedGraph<String, String>
        assertEquals(62.0, newGraph.edges.sumOf { it.weight }, 0.0001)
    }

    @Test
    fun testInvalidGraph() {
        val jsonContent =
            "{\n" +
                "    \"type\": \"NEW_TYPE\",\n" +
                "    \"vertices\": [\n" +
                "        \n" +
                "    ],\n" +
                "    \"edges\": [\n" +
                "        \n" +
                "    ]\n" +
                "}"

        val file = File.createTempFile("graph", ".json")
        file.writeText(jsonContent)

        assertThrows<IllegalArgumentException> {
            loadGraphFromJson(file)
        }
    }
}
