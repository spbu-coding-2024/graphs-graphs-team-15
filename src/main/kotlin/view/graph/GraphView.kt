package view.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import viewmodel.graph.GraphViewModel
import viewmodel.graph.VertexViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <V, E>GraphView(
    viewModel: GraphViewModel<V, E>,
) {
    Box(modifier = Modifier
        .fillMaxSize()

    ) {
        viewModel.vertices.forEach { v ->
            VertexView(v, Modifier)
        }
        viewModel.edges.forEach { e ->
            EdgeView(e, Modifier)
        }
    }
}