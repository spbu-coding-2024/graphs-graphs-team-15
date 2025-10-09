package viewmodel.graph

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.graph.base.Vertex
import viewmodel.GraphColors

class VertexViewModel<V>(
    x: Dp = 0.dp,
    y: Dp = 0.dp,
    color: Color,
    val v: Vertex<V>,
    private val _labelVisible: State<Boolean>,
    val radius: Dp = 25.dp,
) {
    var x by mutableStateOf(x)
    var y by mutableStateOf(y)
    var color by mutableStateOf(color)

    private var isFocused by mutableStateOf(false)

    val label
        get() = v.label

    val labelVisible
        get() = _labelVisible.value

    fun onDrag(offset: Offset) {
        x += offset.x.dp
        y += offset.y.dp
    }

    fun focus() {
        isFocused = true
        color = GraphColors.Vertex.focused
    }

    fun unFocus() {
        isFocused = false
        color = GraphColors.Vertex.unfocused
    }
}
