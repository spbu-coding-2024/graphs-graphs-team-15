package integration

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import model.graph.UndirectedGraph
import model.graph.WeightedUndirectedGraph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import viewmodel.GraphColors
import viewmodel.MainScreenViewModel
import viewmodel.graph.GraphViewModel
import java.io.File

class IntegrationTestMstAlgo {
    /* Граф
     * A - B
     * Создаём модель взвешенного графа
     * Добавляем ещё 2 вершины и 3 ребра
     * A - B
     *   \ |
     *     C - D
     * Сохраняем граф в файл
     * Загружем граф из этого же файла
     * Применяем алгоритм построения минимального остовного дерева
     * Алгоритм ничего не делает с орграфом
     * */

    @Test
    fun testCreateLoadUndirectedGraphAndApplyMstAlgorithm() {
        val graph = UndirectedGraph<String, String>()

        graph.addVertex("A")
        graph.addVertex("B")
        graph.addEdge("A", "B", "AB")

        val viewModel =
            GraphViewModel(
                graph,
                mutableStateOf(false),
                mutableStateOf(false),
            )

        assertTrue(viewModel.edges.all { !it.labelVisible })
        assertTrue(viewModel.vertices.all { !it.labelVisible })

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

        viewModel.highlightMst()

        val highlighted =
            viewModel.edges.filter { edge ->
                edge.color == GraphColors.Edge.highlighted
            }

        assertTrue(highlighted.isEmpty())
    }

    /* Взвешенный граф
     * A - B
     * Создаём модель взвешенного графа
     * Добавляем ещё 2 вершины и 3 ребра
     * A - B
     *   \ |
     *     C - D
     * Сохраняем граф в файл
     * Загружем граф из этого же файла
     * Применяем алгоритм построения минимального остовного дерева
     * Алгоритм соответствующие рёбра перекрашивает в розовый
     * */

    @Test
    fun testCreateLoadWeightedUndirectedGraphAndApplyMstAlgorithm() {
        val graph = WeightedUndirectedGraph<String, String>()

        graph.addVertex("A")
        graph.addVertex("B")
        graph.addEdge("A", "B", "AB", 1.0)

        val viewModel =
            GraphViewModel(
                graph,
                mutableStateOf(true),
                mutableStateOf(true),
            )

        assertTrue(viewModel.edges.all { it.labelVisible })
        assertTrue(viewModel.vertices.all { it.labelVisible })

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
        viewModel.onEdgePopupConfirm("AC", "2")

        viewModel.onVertexClick(viewModel.vertices.find { it.label == "B" }!!)
        viewModel.onVertexClick(viewModel.vertices.find { it.label == "C" }!!)
        viewModel.onEdgePopupConfirm("BC", "3")

        val file = File.createTempFile("graph", ".json")

        viewModel.saveGraph(file)
        viewModel.loadGraph(file)

        viewModel.onVertexClick(viewModel.vertices.find { it.label == "D" }!!)
        viewModel.onVertexClick(viewModel.vertices.find { it.label == "C" }!!)
        viewModel.onEdgePopupConfirm("CD", "4")

        viewModel.highlightMst()

        val highlighted =
            viewModel.edges.filter { edge ->
                edge.color == GraphColors.Edge.highlighted
            }

        val highlightedLabels: Set<String> = highlighted.map { it.label }.toSet()

        val expectedMst = setOf("AB", "CD", "AC")

        assertEquals(expectedMst, highlightedLabels)
    }
}
