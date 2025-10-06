package model.graph.algorithms

import kotlin.math.sqrt
import kotlin.math.min
import kotlin.random.Random
import model.graph.base.Graph
import model.graph.base.Vertex

data class Point(var x: Double, var y: Double)

class ForceAtlas2<E, V>(
    private val graph: Graph<E, V>,
    private val width: Float = 1000.0f,
    private val height: Float = 1000.0f,
    private val iterations: Int = 500,
    private val area: Double = 10000.0,
    private val gravity: Double = 1.0,
    private val speed: Double = 1.0
) {
    private val positions = mutableMapOf<Vertex<V>, Point>()
    private val forces = mutableMapOf<Vertex<V>, Point>()
    private val rand = Random(42)

    init {
        for (v in graph.vertices) {
            positions[v] = Point(rand.nextDouble(-100.0, 100.0), rand.nextDouble(-100.0, 100.0))
        }
    }

    fun run(): Map<Vertex<V>, Point> {
        val k = sqrt(area / (1.0 + graph.vertices.size))

        repeat(iterations) {
            for (v in graph.vertices) {
                forces[v] = Point(0.0, 0.0)
            }

            for (v in graph.vertices) {
                for (u in graph.vertices) {
                    if (u == v) continue
                    val deltaX = positions[v]!!.x - positions[u]!!.x
                    val deltaY = positions[v]!!.y - positions[u]!!.y
                    val dist = sqrt(deltaX * deltaX + deltaY * deltaY) + 0.01
                    val force = k * k / dist
                    forces[v]!!.x += deltaX / dist * force
                    forces[v]!!.y += deltaY / dist * force
                }
            }

            for (edge in graph.edges) {
                val u = edge.vertices.first
                val v = edge.vertices.second
                val deltaX = positions[u]!!.x - positions[v]!!.x
                val deltaY = positions[u]!!.y - positions[v]!!.y
                val dist = sqrt(deltaX * deltaX + deltaY * deltaY) + 0.01
                val force = dist * dist / k
                forces[u]!!.x -= deltaX / dist * force
                forces[u]!!.y -= deltaY / dist * force
                forces[v]!!.x += deltaX / dist * force
                forces[v]!!.y += deltaY / dist * force
            }

            for (v in graph.vertices) {
                val pos = positions[v]!!
                val dist = sqrt(pos.x * pos.x + pos.y * pos.y)
                val gForce = gravity * dist
                forces[v]!!.x -= pos.x / dist * gForce
                forces[v]!!.y -= pos.y / dist * gForce
            }

            for (v in graph.vertices) {
                val f = forces[v]!!
                val pos = positions[v]!!
                val dist = sqrt(f.x * f.x + f.y * f.y)
                if (dist > 0) {
                    val limited = min(speed, dist)
                    pos.x += f.x / dist * limited
                    pos.y += f.y / dist * limited
                }
            }
        }

        val minX = positions.values.minOf { it.x }
        val maxX = positions.values.maxOf { it.x }
        val minY = positions.values.minOf { it.y }
        val maxY = positions.values.maxOf { it.y }

        val rangeX = maxX - minX
        val rangeY = maxY - minY

        val padding = 50.0
        val availableWidth = width - 2 * padding
        val availableHeight = height - 2 * padding

        return positions.mapValues { (_, point) ->
            Point(
                x = padding + ((point.x - minX) / rangeX) * availableWidth,
                y = padding + ((point.y - minY) / rangeY) * availableHeight
            )
        }
    }
}
