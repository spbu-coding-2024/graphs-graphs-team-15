package viewmodel

import androidx.compose.ui.graphics.Color

object GraphColors {
    object Vertex {
        val focused = Color(0xFFFF69B4)    // Pink
        val unfocused = Color(0xFF808080)  // Gray
        val highlighted = Color(0xFFFFA500) // Orange
    }

    object Edge {
        val default = Color.Black
        val highlighted = Color(0xFFFF69B4)
    }
}
