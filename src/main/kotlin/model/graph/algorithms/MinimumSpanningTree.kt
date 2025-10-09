package model.graph.algorithms

import model.graph.WeightedUndirectedGraph
import model.graph.base.Vertex
import model.graph.weighted.WeightedEdge

internal class MinimumSpanningTree<E, V>(val graph: WeightedUndirectedGraph<E, V>) {
    fun kruskalAlgo(): List<WeightedEdge<E, V>> {
        val mst = mutableListOf<WeightedEdge<E, V>>()

        val parent = mutableMapOf<Vertex<V>, Vertex<V>>()
        val rank = mutableMapOf<Vertex<V>, Int>()

        for (v in graph.vertices) {
            parent[v] = v
            rank[v] = 0
        }

        val sortedEdges = graph.edges.sortedBy { it.weight }

        fun find(v: Vertex<V>): Vertex<V> {
            if (parent[v] != v) {
                parent[v] = find(parent[v]!!)
            }
            return parent[v]!!
        }

        fun union(
            u: Vertex<V>,
            v: Vertex<V>,
        ): Boolean {
            val rootU = find(u)
            val rootV = find(v)
            if (rootU == rootV) return false
            if (rank[rootU]!! < rank[rootV]!!) {
                parent[rootU] = rootV
            } else if (rank[rootU]!! > rank[rootV]!!) {
                parent[rootV] = rootU
            } else {
                parent[rootV] = rootU
                rank[rootU] = rank[rootU]!! + 1
            }
            return true
        }

        for (edge in sortedEdges) {
            val u = edge.vertices.first
            val v = edge.vertices.second
            if (union(u, v)) {
                mst += edge
            }
        }

        return mst
    }
}
