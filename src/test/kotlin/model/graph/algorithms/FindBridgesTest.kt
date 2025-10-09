package model.graph.algorithms

import model.graph.UndirectedGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class FindBridgesTest {

    @Test
    fun testEmptyGraphHaveNoBridges() {
        val g = UndirectedGraph<Int, String>()
        val algo = FindBridges(g)

        val bridges = algo.find()

        assertTrue(bridges.isEmpty())
    }

    @Test
    fun testLineGraphHaeAllEdgesAsBridges() {
        val g = UndirectedGraph<Int, String>()
        g.addEdge("A", "B", 1)
        g.addEdge("С", "B", 2)
        g.addEdge("С", "D", 3)

        val algo = FindBridges(g)
        val bridges = algo.find().map { it.element }

        assertEquals(setOf(1, 2, 3), bridges.toSet())
    }

    @Test
    fun testCycleHasNoBridges() {
        val g = UndirectedGraph<Int, String>()
        g.addEdge("A", "B", 1)
        g.addEdge("С", "B", 2)
        g.addEdge("С", "A", 3)

        val algo = FindBridges(g)
        val bridges = algo.find()

        assertTrue(bridges.isEmpty())
    }

    @Test
    fun testCycleHasOneBridge() {
        val g = UndirectedGraph<Int, String>()
        g.addEdge("A", "B", 1)
        g.addEdge("C", "B", 2)
        g.addEdge("C", "A", 3)
        g.addEdge("C", "D", 4)

        val algo = FindBridges(g)
        val bridges = algo.find().map { it.element }

        assertEquals(setOf(4), bridges.toSet())
    }

    @Test
    fun testDifferentComponentsWorksCorrectly() {
        val g = UndirectedGraph<Int, String>()

        g.addEdge("A", "B", 1)
        g.addEdge("B", "C", 2)

        g.addEdge("X", "Y", 3)
        g.addEdge("Y", "Z", 4)
        g.addEdge("Z", "X", 5)

        val algo = FindBridges(g)
        val bridges = algo.find().map { it.element }

        assertEquals(setOf(1,2), bridges.toSet())
    }
}