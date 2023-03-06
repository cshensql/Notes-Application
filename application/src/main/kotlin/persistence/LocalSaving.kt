package persistence

import business.Note
import business.WindowConfig
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class LocalSaving() {

    // stores local config data
    fun saveConfig(config: WindowConfig) {

        val fileName = "config.json"
        val file = File("src/main/kotlin/persistence/saves/$fileName")

        val format = Json { encodeDefaults = true }
        val configString = format.encodeToString(config)

        // always overwrite, for updates or initial saving
        file.writeText(configString)
    }

    // loads local config data
    fun loadConfig(): WindowConfig {

        val configString: String = File("src/main/kotlin/persistence/saves/config.json").readText()

        return Json.decodeFromString<WindowConfig>(configString)
    }

    // stores local notes data
    fun saveNotes(notes: MutableList<Note>) {
        val fileName = "notes.json"
        val file = File("src/main/kotlin/persistence/saves/$fileName")

        val format = Json { encodeDefaults = true }
        val notesString = format.encodeToString(notes)

        // always overwrite, for updates or initial saving
        file.writeText(notesString)
        println(notesString)
    }

    // loads local notes data
    fun loadNotes(): MutableList<Note> {

        val notesString: String = File("src/main/kotlin/persistence/saves/notes.json").readText()
        return Json.decodeFromString<MutableList<Note>>(notesString)
    }
}