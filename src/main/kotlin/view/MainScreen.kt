package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.sp
import model.graph.io.GraphType
import view.graph.GraphView
import viewmodel.MainScreenViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(viewModel: MainScreenViewModel<String, String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(modifier = Modifier.width(370.dp)) {
            Text("Select graph type:", fontSize = 20.sp, modifier = Modifier.padding(vertical = 8.dp))

            val graphTypes = listOf(
                GraphType.UNDIRECTED,
                GraphType.DIRECTED,
                GraphType.WEIGHTED_UNDIRECTED,
                GraphType.WEIGHTED_DIRECTED
            )

            graphTypes.forEach { type ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = viewModel.selectedGraphType == type,
                        onClick = { viewModel.onGraphTypeSelected(type) }
                    )
                    Text(type.name.lowercase().replaceFirstChar { it.uppercase() }, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Checkbox(checked = viewModel.showVerticesLabels, onCheckedChange = {
                    viewModel.showVerticesLabels = it
                })
                Text("Show vertices labels", fontSize = 28.sp, modifier = Modifier.padding(4.dp))
            }
            Row {
                Checkbox(checked = viewModel.showEdgesLabels, onCheckedChange = {
                    viewModel.showEdgesLabels = it
                })
                Text("Show edges labels", fontSize = 28.sp, modifier = Modifier.padding(4.dp))
            }
            Button(
                onClick = viewModel::resetGraphView,
                enabled = true,
            ) {
                Text(
                    text = "Reset default settings"
                )
            }
            Button(
                onClick = { viewModel.applyLayoutAlgorithm(800.dp, 600.dp) },
                enabled = true
            ) {
                Text("Apply layout algorithm")
            }
            Button(
                onClick = viewModel::findBridges,
                enabled = true
            ) {
                Text("Highlight Bridges")
            }
            Button(
                onClick = viewModel::buildMst,
                enabled = true
            ) {
                Text("Build MST")
            }
        }

        Surface(
            modifier = Modifier.weight(1f)
        ) {
            GraphView(viewModel.graphViewModel)
        }

    }
}
