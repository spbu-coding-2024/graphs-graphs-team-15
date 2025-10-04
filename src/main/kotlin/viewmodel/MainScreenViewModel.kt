package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import model.graph.base.Graph
import viewmodel.graph.GraphViewModel

class MainScreenViewModel<E, V>(graph: Graph<E, V>) {
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

    val graphViewModel = GraphViewModel(graph, _showVerticesLabels, _showEdgesLabels)

    fun resetGraphView() {
        graphViewModel.vertices.forEach { v -> v.color = GraphColors.Vertex.unfocused }
        graphViewModel.edges.forEach { e -> e.color = GraphColors.Edge.default }
    }

    fun applyLayoutAlgorithm(screenWidth: Dp, screenHeight: Dp) {
        graphViewModel.applyForceAtlas2(screenWidth, screenHeight)
    }

    fun findBridges() {
        graphViewModel.highlightBridges()
    }
}
