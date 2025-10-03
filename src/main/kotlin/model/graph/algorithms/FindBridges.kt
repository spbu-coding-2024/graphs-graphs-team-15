package model.graph.algorithms

import model.graph.UndirectedGraph
import model.graph.base.Edge
import model.graph.base.Vertex

internal class FindBridges<E, V>(val graph: UndirectedGraph<E, V>) {
    private val tin = mutableMapOf<Vertex<V>, Int>()
    private val fup = mutableMapOf<Vertex<V>, Int>()
    private var timer = 0
    private val visited = mutableSetOf<Vertex<V>>()
    private val bridges = mutableListOf<Edge<E, V>>()

    fun find(): List<Edge<E, V>> {
        timer = 0
        bridges.clear()
        visited.clear()
        tin.clear()
        fup.clear()

        for (v in graph.vertices) {
            if (v !in visited) {
                dfs(v, parent = null)
            }
        }
        return bridges
    }

    private fun dfs(v: Vertex<V>, parent: Vertex<V>?) {
        visited += v
        tin[v] = timer
        fup[v] = timer
        timer++

        for (edge in graph.edges) {
            if (!edge.incident(v)) continue
            val to = if (edge.vertices.first == v) edge.vertices.second else edge.vertices.first
            if (to == parent) continue

            if (to !in visited) {
                dfs(to, v)
                fup[v] = minOf(fup[v]!!, fup[to]!!)
                if (fup[to]!! > tin[v]!!) {
                    bridges += edge
                }
            } else {
                fup[v] = minOf(fup[v]!!, tin[to]!!)
            }
        }
    }
}