package model.graph.algorithms

import model.graph.base.Graph
import model.graph.base.Vertex
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

data class Point(var x: Double, var y: Double)

class ForceAtlas2<E, V>(
    private val graph: Graph<E, V>,
    private val width: Float = 1000.0f,
    private val height: Float = 1000.0f,
    private val iterations: Int = 500,
    private val area: Double = 10000.0,
    private val gravity: Double = 1.0,
) {
    private val positions = mutableMapOf<Vertex<V>, Point>()
    private val forces = mutableMapOf<Vertex<V>, Point>()
    private val forcesOld = mutableMapOf<Vertex<V>, Point>()
    private val rand = Random(42)

    init {
        for (v in graph.vertices) {
            positions[v] = Point(rand.nextDouble(-100.0, 100.0), rand.nextDouble(-100.0, 100.0))
            forces[v] = Point(0.0, 0.0)
        }
    }

    fun run(): Map<Vertex<V>, Point> {
        val scalingRatio = sqrt(area / (1.0 + graph.vertices.size))
        var speed = 1.0
        var speedEfficiency = 1.0

        repeat(iterations) {
            for (v in graph.vertices) {
                forcesOld[v] = forces[v]!!.copy()
                forces[v] = Point(0.0, 0.0)
            }

            for (v in graph.vertices) {
                for (u in graph.vertices) {
                    if (u == v) continue
                    val deltaX = positions[v]!!.x - positions[u]!!.x
                    val deltaY = positions[v]!!.y - positions[u]!!.y
                    val dist = sqrt(deltaX * deltaX + deltaY * deltaY)
                    if (dist > 0) {
                        val factor = scalingRatio * scalingRatio / dist
                        forces[v]!!.x += deltaX / dist * factor
                        forces[v]!!.y += deltaY / dist * factor
                    }
                }
            }

            for (v in graph.vertices) {
                val pos = positions[v]!!
                val dist = (pos.x * pos.x + pos.y * pos.y)
                if (dist > 0) {
                    val factor = 1 * gravity / dist
                    forces[v]!!.x -= pos.x * factor
                    forces[v]!!.y -= pos.y * factor
                }
            }

            for (edge in graph.edges) {
                val u = edge.vertices.first
                val v = edge.vertices.second
                val deltaX = positions[u]!!.x - positions[v]!!.x
                val deltaY = positions[u]!!.y - positions[v]!!.y
                val dist = sqrt(deltaX * deltaX + deltaY * deltaY)

                if (dist > 0) {
                    val factor = dist * dist / scalingRatio
                    forces[u]!!.x -= deltaX / dist * factor
                    forces[u]!!.y -= deltaY / dist * factor
                    forces[v]!!.x += deltaX / dist * factor
                    forces[v]!!.y += deltaY / dist * factor
                }
            }

            var totalSwinging = 0.0
            var totalEffectiveTraction = 0.0
            val jitterTolerance = 1.0

            for (v in graph.vertices) {
                val swinging =
                    sqrt(
                        (forcesOld[v]!!.x - forces[v]!!.x) *
                            (forcesOld[v]!!.x - forces[v]!!.x) +
                            (forcesOld[v]!!.y - forces[v]!!.y) *
                            (forcesOld[v]!!.y - forces[v]!!.y),
                    )
                totalSwinging += 1 * swinging
                totalEffectiveTraction +=
                    0.5 *
                    sqrt(
                        (forcesOld[v]!!.x + forces[v]!!.x) *
                            (forcesOld[v]!!.x + forces[v]!!.x) +
                            (forcesOld[v]!!.y + forces[v]!!.y) *
                            (forcesOld[v]!!.y + forces[v]!!.y),
                    )
            }

            val estimatedOptimalJitterTolerance = 0.5 * sqrt(graph.vertices.size.toDouble())
            val minJT = sqrt(estimatedOptimalJitterTolerance)
            val maxJT = 10.0
            var jt =
                jitterTolerance *
                    max(
                        minJT,
                        min(
                            maxJT,
                            estimatedOptimalJitterTolerance * totalEffectiveTraction / (
                                graph.vertices.size * graph.vertices.size
                            ),
                        ),
                    )
            val minSpeedEfficiency = 0.05

            if (totalEffectiveTraction > 0 && totalSwinging / totalEffectiveTraction > 2) {
                if (speedEfficiency > minSpeedEfficiency) {
                    speedEfficiency *= 0.5
                }
                jt = max(jt, jitterTolerance)
            }
            var targetSpeed: Double
            if (totalSwinging == 0.0) {
                targetSpeed = 1.7976931348623157E308
            } else {
                targetSpeed = jt * speedEfficiency * totalEffectiveTraction / totalSwinging
            }

            if (totalSwinging > jt * totalEffectiveTraction) {
                if (speedEfficiency > minSpeedEfficiency) {
                    speedEfficiency *= 0.7
                }
            } else {
                speedEfficiency *= 1.3
            }

            val maxRise = 0.5
            speed += min(targetSpeed - speed, maxRise * speed)

            for (v in graph.vertices) {
                val swinging =
                    1 *
                        sqrt(
                            (forcesOld[v]!!.x - forces[v]!!.x) *
                                (forcesOld[v]!!.x - forces[v]!!.x) +
                                (forcesOld[v]!!.y - forces[v]!!.y) *
                                (forcesOld[v]!!.y - forces[v]!!.y),
                        )
                val factor = speed / (1.0 + sqrt(speed * swinging))
                positions[v]!!.x += forces[v]!!.x * factor
                positions[v]!!.y += forces[v]!!.y * factor
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
                y = padding + ((point.y - minY) / rangeY) * availableHeight,
            )
        }
    }
}
