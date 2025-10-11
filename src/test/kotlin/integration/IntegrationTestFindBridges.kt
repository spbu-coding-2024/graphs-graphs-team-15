package integration

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import viewmodel.GraphColors
import viewmodel.graph.GraphViewModel
import java.io.File

class IntegrationTestFindBridges {

    /* Граф
     * A - B
     * Создаём модель взвешенного графа
     * Добавляем ещё 2 вершины и 3 ребра
     * A - B
     *   \ |
     *     C - D
     * Сохраняем граф в файл
     * Загружем граф из этого же файла
     * Применяем поиска мостов
     * Алгоритм находит единственный мост CD
     * */

    @Test
    fun testCreateLoadUndirectedGraphAndFindBridges() {
        val graph = UndirectedGraph<String, String>()

        graph.addVertex("A")
        graph.addVertex("B")
        graph.addEdge("A", "B", "AB")

        val viewModel =
            GraphViewModel(
                graph,
                mutableStateOf(true),
                mutableStateOf(true),
            )

        viewModel.onCanvasClick()

        viewModel.onRightClick()
        viewModel.onVertexPopupDismiss()

        viewModel.onRightClick()
        viewModel.onVertexPopupConfirm("C", 0.dp, 0.dp)

        viewModel.onRightClick()
        viewModel.onVertexPopupConfirm("D", 0.dp, 0.dp)

        viewModel.onVertexClick(viewModel.vertices.first())
        viewModel.onVertexClick(viewModel.vertices.first())
        viewModel.onVertexClick(viewModel.vertices.first())
        viewModel.onVertexClick(viewModel.vertices.last())

        viewModel.onEdgePopupDismiss()

        viewModel.onCanvasClick()

        viewModel.onVertexClick(viewModel.vertices.find { it.label == "A" }!!)
        viewModel.onVertexClick(viewModel.vertices.find { it.label == "C" }!!)
        viewModel.onEdgePopupConfirm("AC", "")

        viewModel.onVertexClick(viewModel.vertices.find { it.label == "B" }!!)
        viewModel.onVertexClick(viewModel.vertices.find { it.label == "C" }!!)
        viewModel.onEdgePopupConfirm("BC", "")

        val file = File.createTempFile("graph", ".json")

        viewModel.saveGraph(file)
        viewModel.loadGraph(file)

        viewModel.onVertexClick(viewModel.vertices.find { it.label == "D" }!!)
        viewModel.onVertexClick(viewModel.vertices.find { it.label == "C" }!!)
        viewModel.onEdgePopupConfirm("CD", "")

        viewModel.highlightBridges()

        val highlighted =
            viewModel.edges.filter { edge ->
                edge.color == GraphColors.Edge.highlighted
            }

        val highlightedLabels: Set<String> = highlighted.map { it.label }.toSet()

        assertEquals(setOf("CD"), highlightedLabels)
    }

    /* Орграф
     * A - B
     * Создаём модель взвешенного графа
     * Добавляем ещё 2 вершины и 3 ребра
     * A - B
     *   \ |
     *     C - D
     * Сохраняем граф в файл
     * Загружем граф из этого же файла
     * Применяем алгоритм поиска мостов
     * Алгоритм ничего не делает с орграфом
     * */

    @Test
    fun testCreateLoadDirectedGraphAndFindBridges() {
        val graph = DirectedGraph<String, String>()

        graph.addVertex("A")
        graph.addVertex("B")
        graph.addEdge("A", "B", "AB")

        val viewModel =
            GraphViewModel(
                graph,
                mutableStateOf(true),
                mutableStateOf(true),
            )

        viewModel.onCanvasClick()

        viewModel.onRightClick()
        viewModel.onVertexPopupDismiss()

        viewModel.onRightClick()
        viewModel.onVertexPopupConfirm("C", 0.dp, 0.dp)

        viewModel.onRightClick()
        viewModel.onVertexPopupConfirm("D", 0.dp, 0.dp)

        viewModel.onVertexClick(viewModel.vertices.first())
        viewModel.onVertexClick(viewModel.vertices.first())
        viewModel.onVertexClick(viewModel.vertices.first())
        viewModel.onVertexClick(viewModel.vertices.last())

        viewModel.onEdgePopupDismiss()

        viewModel.onCanvasClick()

        viewModel.onVertexClick(viewModel.vertices.find { it.label == "A" }!!)
        viewModel.onVertexClick(viewModel.vertices.find { it.label == "C" }!!)
        viewModel.onEdgePopupConfirm("AC", "")

        viewModel.onVertexClick(viewModel.vertices.find { it.label == "B" }!!)
        viewModel.onVertexClick(viewModel.vertices.find { it.label == "C" }!!)
        viewModel.onEdgePopupConfirm("BC", "")

        val file = File.createTempFile("graph", ".json")

        viewModel.saveGraph(file)
        viewModel.loadGraph(file)

        viewModel.onVertexClick(viewModel.vertices.find { it.label == "D" }!!)
        viewModel.onVertexClick(viewModel.vertices.find { it.label == "C" }!!)
        viewModel.onEdgePopupConfirm("CD", "")

        viewModel.highlightBridges()

        val highlighted =
            viewModel.edges.filter { edge ->
                edge.color == GraphColors.Edge.highlighted
            }

        assertTrue(highlighted.isEmpty())
    }
}
