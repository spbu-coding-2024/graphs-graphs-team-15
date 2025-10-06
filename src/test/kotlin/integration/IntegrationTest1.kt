package integration

import androidx.compose.runtime.mutableStateOf
import model.graph.WeightedUndirectedGraph
import org.junit.Test
import viewmodel.GraphColors
import viewmodel.graph.GraphViewModel
import java.io.File
import kotlin.test.assertEquals

class IntegrationTest1 {

    /* Взвешенный граф
    * A - B
    *   \ |
    *     C - D
    * Сохраняем граф в файл
    * Загружем граф из этого же файла
    * Применяем алгоритм построения минимального остовного дерева
    * Алгоритм соответствующие рёбра перекрашивает в розовый
    * */

    @Test
    fun testCreateLoadAndMstAlgorithm() {
        val graph = WeightedUndirectedGraph<String, String>()

        graph.addVertex("A")
        graph.addVertex("B")
        graph.addVertex("C")
        graph.addVertex("D")

        graph.addEdge("A", "B", "AB", 1.0)
        graph.addEdge("A", "C", "AC", 2.0)
        graph.addEdge("B", "C", "BC", 3.0)
        graph.addEdge("C", "D", "CD", 4.0)

        val viewModel = GraphViewModel(
            graph,
            mutableStateOf(true),
            mutableStateOf(true)
        )

        val file = File.createTempFile("graph", ".json")
        viewModel.saveGraph(file)

        viewModel.loadGraph(file)

        viewModel.highlightMst()

        val highlighted = viewModel.edges.filter { edge ->
            edge.color == GraphColors.Edge.highlighted
        }

        val highlightedLabels: Set<String> = highlighted.map { it.label }.toSet()

        val expectedMst = setOf("AB", "CD", "AC")

        assertEquals(expectedMst, highlightedLabels)
    }
}