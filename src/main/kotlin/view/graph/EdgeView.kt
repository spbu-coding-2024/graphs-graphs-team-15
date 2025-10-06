package view.graph

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import viewmodel.graph.EdgeViewModel

@Composable
fun <E, V> EdgeView(
    viewModel: EdgeViewModel<E, V>,
    modifier: Modifier = Modifier,
    offset: Offset,
    scale: Float
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawLine(
            start = Offset(
                (viewModel.u.x.toPx() + viewModel.u.radius.toPx()) * scale + offset.x,
                (viewModel.u.y.toPx() + viewModel.u.radius.toPx()) * scale + offset.y,
            ),
            end = Offset(
                (viewModel.v.x.toPx() + viewModel.v.radius.toPx()) * scale + offset.x,
                (viewModel.v.y.toPx() + viewModel.v.radius.toPx()) * scale + offset.y,
            ),
            color = viewModel.color,
            strokeWidth = 5.0f * scale
        )
    }
    if (viewModel.labelVisible) {
        Text(
            modifier = Modifier
                .offset(
                    (viewModel.u.x + (viewModel.v.x - viewModel.u.x) / 2) * scale  + offset.x.dp,
                    (viewModel.u.y + (viewModel.v.y - viewModel.u.y) / 2) * scale  + offset.y.dp
                ),
            text = viewModel.label,
        )
    }
}
