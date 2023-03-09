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

    // saves group names data locally
    fun saveGroupNames(group: MutableList<String>) {
        var fileName = "src/main/kotlin/persistence/saves/groupNames.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/groupNames.json"
        }
        val file = File(fileName)

        val format = Json { encodeDefaults = true }
        val groupString = format.encodeToString(group)

        // always overwrite, for updates or initial saving
        file.writeText(groupString)
    }

    // loads local group names data
    fun loadGroupNames(): MutableList<String> {
        var fileName = "src/main/kotlin/persistence/saves/groupNames.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/groupNames.json"
        }
        val groupString: String = File(fileName).readText()

        return Json.decodeFromString<MutableList<String>>(groupString)
    }

    // saves recently deleted notes data locally
    fun saveRecentlyDeletedNotes(notes: MutableList<Note>) {
        var fileName = "src/main/kotlin/persistence/saves/recentlyDeleted.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/recentlyDeleted.json"
        }
        val file = File(fileName)

        val format = Json { encodeDefaults = true }
        val notesString = format.encodeToString(notes)

        // always overwrite, for updates or initial saving
        file.writeText(notesString)
    }

    // loads recently deleted notes data
    fun loadRecentlyDeletedNotes(): MutableList<Note> {
        var fileName = "src/main/kotlin/persistence/saves/recentlyDeleted.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/recentlyDeleted.json"
        }
        val noteString: String = File(fileName).readText()

        return Json.decodeFromString<MutableList<Note>>(noteString)
    }

}