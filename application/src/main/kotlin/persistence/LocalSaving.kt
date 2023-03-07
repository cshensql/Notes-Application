package persistence

import business.Note
import business.WindowConfig
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class LocalSaving() {

    var testFlag = false

    // stores local config data
    fun saveConfig(config: WindowConfig) {

        var fileName = "src/main/kotlin/persistence/saves/config.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/config.json"
        }
        val file = File(fileName)

        val format = Json { encodeDefaults = true }
        val configString = format.encodeToString(config)

        // always overwrite, for updates or initial saving
        file.writeText(configString)
    }

    // loads local config data
    fun loadConfig(): WindowConfig {

        var fileName = "src/main/kotlin/persistence/saves/config.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/config.json"
        }
        val configString: String = File(fileName).readText()

        return Json.decodeFromString<WindowConfig>(configString)
    }

    // stores local notes data
    fun saveNotes(notes: MutableList<Note>) {
        var fileName = "src/main/kotlin/persistence/saves/notes.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/notes.json"
        }
        val file = File(fileName)

        val format = Json { encodeDefaults = true }
        val notesString = format.encodeToString(notes)

        // always overwrite, for updates or initial saving
        file.writeText(notesString)
    }

    // loads local notes data
    fun loadNotes(): MutableList<Note> {

        var fileName = "src/main/kotlin/persistence/saves/notes.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/notes.json"
        }
        val notesString: String = File(fileName).readText()
        return Json.decodeFromString<MutableList<Note>>(notesString)
    }
}