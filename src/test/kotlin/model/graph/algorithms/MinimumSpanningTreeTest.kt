package model.graph.algorithms

import model.graph.WeightedUndirectedGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class MinimumSpanningTreeTest {

    @Test
    fun testSingleEdgeGraph() {
        val g = WeightedUndirectedGraph<Int, String>()
        g.addEdge("A", "B", 1, 5.0)

        val mst = MinimumSpanningTree(g).kruskalAlgo()
        assertEquals(1, mst.size)
        assertEquals(5.0, mst.first().weight)
    }

    @Test
    fun testCycleGraph() {
        val g = WeightedUndirectedGraph<Int, String>()
        g.addEdge("A", "B", 1, 10.0)
        g.addEdge("B", "C", 2, 11.0)
        g.addEdge("A", "C", 3, 13.0)

        val mst = MinimumSpanningTree(g).kruskalAlgo()
        assertEquals(2, mst.size)
        val totalWeight = mst.sumOf { it.weight }
        assertEquals(21.0, totalWeight)
    }

    @Test
    fun testGraphWithTwoComponents() {
        val g = WeightedUndirectedGraph<Int, String>()
        g.addEdge("A", "B", 1, 5.0)
        g.addEdge("B", "C", 2, 6.0)

        g.addEdge("X", "Y", 3, 7.0)

        val mst = MinimumSpanningTree(g).kruskalAlgo()
        assertEquals(3, mst.size)
        val totalWeight = mst.sumOf { it.weight }
        assertEquals(18.0, totalWeight)
    }
}