package viewmodel

import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import model.graph.WeightedDirectedGraph
import model.graph.WeightedUndirectedGraph
import model.graph.base.Graph
import model.graph.io.GraphType
import viewmodel.graph.GraphViewModel

class MainScreenViewModel<E, V>() {
    private var _showVerticesLabels = mutableStateOf(false)
    var showVerticesLabels: Boolean
        get() = _showVerticesLabels.value
        set(value) {
            _showVerticesLabels.value = value
        }

    private var _showEdgesLabels = mutableStateOf(false)
    var showEdgesLabels: Boolean
        get() = _showEdgesLabels.value
        set(value) {
            _showEdgesLabels.value = value
        }

    var selectedGraphType by mutableStateOf(GraphType.UNDIRECTED)
        private set

    var graphViewModel by mutableStateOf(
        GraphViewModel(createGraph(GraphType.UNDIRECTED), _showVerticesLabels, _showEdgesLabels),
    )
        private set

    fun resetGraphView() {
        graphViewModel.vertices.forEach { v -> v.color = GraphColors.Vertex.unfocused }
        graphViewModel.edges.forEach { e -> e.color = GraphColors.Edge.default }
    }

    fun applyLayoutAlgorithm(
        screenWidth: Dp,
        screenHeight: Dp,
    ) {
        graphViewModel.applyForceAtlas2(screenWidth, screenHeight)
    }

    fun findBridges() {
        graphViewModel.highlightBridges()
    }

    fun buildMst() {
        graphViewModel.highlightMst()
    }

    fun onGraphTypeSelected(type: GraphType) {
        selectedGraphType = type
        graphViewModel.clearAll()
        graphViewModel = GraphViewModel(createGraph(selectedGraphType), _showVerticesLabels, _showEdgesLabels)
    }

    private fun createGraph(type: GraphType): Graph<E, V> {
        return when (type) {
            GraphType.UNDIRECTED -> UndirectedGraph()
            GraphType.DIRECTED -> DirectedGraph()
            GraphType.WEIGHTED_UNDIRECTED -> WeightedUndirectedGraph()
            GraphType.WEIGHTED_DIRECTED -> WeightedDirectedGraph()
        }
    }
}
