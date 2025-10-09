package model.graph.io

import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

fun chooseFileToSave(): File? {
    val chooser =
        JFileChooser().apply {
            dialogTitle = "Save Graph as JSON"
            fileFilter = FileNameExtensionFilter("JSON Files", "json")
        }
    val result = chooser.showSaveDialog(null)
    return if (result == JFileChooser.APPROVE_OPTION) {
        var file = chooser.selectedFile
        if (!file.name.endsWith(".json")) {
            file = File(file.parentFile, file.name + ".json")
        }
        file
    } else {
        null
    }
}

fun chooseFileToLoad(): File? {
    val chooser =
        JFileChooser().apply {
            dialogTitle = "Open Graph JSON"
            fileFilter = FileNameExtensionFilter("JSON Files", "json")
        }
    val result = chooser.showOpenDialog(null)
    return if (result == JFileChooser.APPROVE_OPTION) {
        chooser.selectedFile
    } else {
        null
    }
}
