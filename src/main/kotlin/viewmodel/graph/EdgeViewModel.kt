package viewmodel.graph

import androidx.compose.runtime.*
import androidx.compose.runtime.State
import model.graph.base.Edge
import viewmodel.GraphColors

class EdgeViewModel<E, V>(
    val u: VertexViewModel<V>,
    val v: VertexViewModel<V>,
    private val e: Edge<E, V>,
    private val _labelVisible: State<Boolean>,
) {
    val label
        get() = e.element.toString()

    val labelVisible
        get() = _labelVisible.value

    var color by mutableStateOf(GraphColors.Edge.default)

    fun highlight() {
        color = GraphColors.Edge.highlighted
    }

    fun resetColor() {
        color = GraphColors.Edge.default
    }
}
