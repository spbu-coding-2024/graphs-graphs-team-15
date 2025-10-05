package viewmodel.graph


import algorithm.ForceAtlas2
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.graph.UndirectedGraph
import model.graph.WeightedUndirectedGraph
import model.graph.algorithms.FindBridges
import model.graph.algorithms.MinimumSpanningTree
import model.graph.base.Edge
import model.graph.base.Graph
import model.graph.base.Vertex
import viewmodel.GraphColors
import java.io.File
import javax.swing.text.Position

import model.graph.io.loadGraphFromJson
import model.graph.io.saveGraphToJson
import model.graph.weighted.WeightedEdge
import model.graph.weighted.WeightedGraph

class GraphViewModel<E, V>(
    private val graph: Graph<E, V>,
    private val showVerticesLabels: State<Boolean>,
    private val showEdgesLabels: State<Boolean>,
) {
    private val _vertices = mutableStateMapOf<Vertex<V>, VertexViewModel<V>>()
    private val _edges = mutableStateMapOf<Edge<E, V>, EdgeViewModel<E, V>>()

    val vertices: Collection<VertexViewModel<V>>
        get() = _vertices.values

    val edges: Collection<EdgeViewModel<E, V>>
        get() = _edges.values

    var showVertexPopup by mutableStateOf(false)
        private set
    var showEdgePopup by mutableStateOf(false)
        private set

    private var _focusedVertex: VertexViewModel<V>? = null
    private var _secondVertex: VertexViewModel<V>? = null

    fun onRightClick() {
        showVertexPopup = true
    }

    fun onVertexPopupConfirm(label: String, x: Dp, y: Dp) {
        addVertexViewModel(label, x, y, GraphColors.Vertex.unfocused)
        showVertexPopup = false
    }

    fun onVertexPopupDismiss() {
        showVertexPopup = false
    }

    fun onVertexClick(vertex: VertexViewModel<V>) {
        if(_focusedVertex == null) {
            vertex.focus()
            _focusedVertex = vertex
        } else if (_focusedVertex == vertex) {
            vertex.unFocus()
            _focusedVertex = null
        } else {
            _secondVertex = vertex
            showEdgePopup = true
        }
    }

    fun onEdgePopupConfirm(element: String, weight: String) {
        val first = _focusedVertex ?: return
        val second = _secondVertex ?: return
        val correct_weight = weight.toDouble()
        addEdgeViewModel(first, second, element as E, correct_weight)
        first.unFocus()
        _focusedVertex = null
        _secondVertex = null
        showEdgePopup = false
    }

    fun onEdgePopupDismiss() {
        showEdgePopup = false
        _secondVertex = null
    }

    fun onCanvasClick() {
        _focusedVertex?.unFocus()
        _focusedVertex = null
    }

    fun addVertexViewModel(label: String, x: Dp, y: Dp, color: Color) {
        val vertex = graph.addVertex(label as V)
        val vertexViewModel = VertexViewModel(
            x = x,
            y = y,
            color = color,
            v = vertex,
            showVerticesLabels
        )
        if (!_vertices.containsKey(vertex)) {
            _vertices[vertex] = vertexViewModel
        }
    }

    fun addEdgeViewModel(
        focused: VertexViewModel<V>,
        current: VertexViewModel<V>,
        e: E,
        weight: Double
    ) {
        val edge = if (graph is WeightedGraph) {
            (graph as WeightedGraph).addEdge(focused.label, current.label, e, weight)
        } else {
            graph.addEdge(focused.label, current.label, e)
        }
        val edgeViewModel = EdgeViewModel(
            focused, current, edge, showEdgesLabels
        )

        val areConnected = _edges.keys.any {
            it.incident(focused.v) && it.incident(current.v)
        }

        if(!areConnected && !_edges.containsKey(edge)) {
            _edges[edge] = edgeViewModel
        }
    }

    fun applyForceAtlas2(screenWidth: Dp, screenHeight: Dp) {
        val algorithm = ForceAtlas2(
            graph,
            width = screenWidth.value,
            height = screenHeight.value
        )
        val result = algorithm.run()

        result.forEach { (vertex, point) ->
            _vertices[vertex]?.let {
                it.x = point.x.dp
                it.y = point.y.dp
            }
        }
    }

    fun highlightBridges() {
        _edges.values.forEach { it.resetColor() }

        if (graph !is UndirectedGraph)
            return

        val algorithm = FindBridges(graph)
        val bridges = algorithm.find()

        bridges.forEach { edge ->
            _edges[edge]?.highlight()
        }
    }

    fun highlightMst() {
        _edges.values.forEach { it.resetColor() }

        if (graph !is WeightedUndirectedGraph)
            return

        val algo = MinimumSpanningTree(graph)
        val mstEdges = algo.kruskalAlgo()

        mstEdges.forEach { edge ->
            _edges[edge]?.highlight()
        }
    }

    fun saveGraph(file: File) {
        saveGraphToJson(graph as Graph<String, String>, file)
    }

    fun loadGraph(file: File) {
        val loaded = loadGraphFromJson(file)
        clearAll()

        loaded.vertices.forEach { v ->
            addVertexViewModel(v.label, 0.dp, 0.dp, GraphColors.Vertex.unfocused)
        }
        loaded.edges.forEach { e ->
            val fst = _vertices[e.vertices.first as Vertex<V>]!!
            val snd = _vertices[e.vertices.second as Vertex<V>]!!
            val weight = if (graph is WeightedGraph<E, V>)
                (e as WeightedEdge<E, V>).weight
            else
                0.0
            addEdgeViewModel(fst, snd, e.element as E, weight)
        }
    }

    fun clearAll() {
        _vertices.clear()
        _edges.clear()
        graph.clear()
        _focusedVertex = null
    }
}
