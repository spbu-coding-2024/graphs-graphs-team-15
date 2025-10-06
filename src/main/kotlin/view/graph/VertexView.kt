package view.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import viewmodel.graph.VertexViewModel

@Composable
fun <V> VertexView(
    viewModel: VertexViewModel<V>,
    onVertexClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    offset: Offset,
    scale: Float
) {
    Box(modifier = modifier
        .size(viewModel.radius * 2 * scale, viewModel.radius * 2 * scale)
        .offset(viewModel.x * scale + offset.x.dp, viewModel.y * scale + offset.y.dp)
        .background(
            color = viewModel.color,
            shape = CircleShape
        )
        .pointerInput(viewModel) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                viewModel.onDrag(dragAmount)
            }
        }
        .pointerInput(viewModel) {
            detectTapGestures(
                onTap = { onVertexClick?.invoke() }
            )
        }
    ) {
        if (viewModel.labelVisible) {
            Text(
                text = viewModel.label as String,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(0.dp, -viewModel.radius - 10.dp)
            )
        }
    }
}
