package view.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PopupVertex(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Surface(
        Modifier
            .padding(4.dp)
            .widthIn(min = 100.dp, max = 300.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Введите имя вершины")
            Spacer(Modifier.height(8.dp))
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                modifier = Modifier,
                placeholder = { Text("Введите текст") },
            )
            Spacer(Modifier.height(8.dp))
            Row {
                Button(onClick = { onConfirm(text) }) {
                    Text("OK")
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = onDismiss) {
                    Text("Отмена")
                }
            }
        }
    }
}
