package view.graph

import androidx.compose.runtime.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import model.graph.io.chooseFileToLoad
import model.graph.io.chooseFileToSave
import view.dialog.PopupContent
import viewmodel.graph.GraphViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <E, V>GraphView(
    viewModel: GraphViewModel<E, V>,
) {
    var popupPosition by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures {
                viewModel.onCanvasClick()
            }
        }
        .onPointerEvent(PointerEventType.Press) { event ->
            popupPosition = event.changes[0].position
            if (event.buttons.isSecondaryPressed) {
                viewModel.onRightClick()
            }
        }
    ) {

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                val file = chooseFileToSave() ?: return@Button
                viewModel.saveGraph(file)
            }) {
                Text("Save Graph")
            }

            Button(onClick = {
                val file = chooseFileToLoad() ?: return@Button
                viewModel.loadGraph(file)
                viewModel.applyForceAtlas2(800.dp, 600.dp)
            }) {
                Text("Load Graph")
            }

            Button(onClick = {
                viewModel.clearAll()
            }) {
                Text("Clear graph")
            }
        }


        viewModel.edges.forEach { e ->
            EdgeView(e, Modifier)
        }
        viewModel.vertices.forEach { v ->
            VertexView(
                v,
                onVertexClick = { viewModel.onVertexClick(v) },
                Modifier
            )
        }

        if (viewModel.showVertexPopup) {
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(
                    popupPosition.x.toInt(),
                    popupPosition.y.toInt()
                ),
                onDismissRequest = { viewModel.onVertexPopupDismiss() },
                properties = PopupProperties(focusable = true)
            ) {
                PopupContent(
                    onConfirm = { text ->
                        viewModel.onVertexPopupConfirm(
                            text as V,
                            popupPosition.x.dp,
                            popupPosition.y.dp
                        )
                    },
                    onDismiss = {
                        viewModel.onVertexPopupDismiss()
                    }
                )
            }
        }

        if (viewModel.showEdgePopup) {
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(
                    popupPosition.x.toInt(),
                    popupPosition.y.toInt()
                ),
                onDismissRequest = { viewModel.onEdgePopupDismiss() },
                properties = PopupProperties(focusable = true)
            ) {
                PopupContent(
                    onConfirm = { text ->
                        viewModel.onEdgePopupConfirm(text as E)
                    },
                    onDismiss = {
                        viewModel.onEdgePopupDismiss()
                    }
                )
            }
        }
    }
}
