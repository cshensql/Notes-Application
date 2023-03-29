package persistence

import business.Note
import business.WindowConfig
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li

class LocalSaving() {

    var testFlag = false

    // stores local config data
    fun saveConfig(config: WindowConfig) {

        var fileName = "${System.getProperty("user.home")}/cs346Data/persistence/saves/config.json"

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
        checkDir()
        var fileName = "${System.getProperty("user.home")}/cs346Data/persistence/saves/config.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/config.json"
        }

        val file = File(fileName)
        if (!file.exists()) {
            file.writeText("{\"positionX\":1.0,\"positionY\":1.0,\"width\":1.0,\"height\":1.0}")
        }

        val configString: String = file.readText()

        return Json.decodeFromString<WindowConfig>(configString)
    }

    // stores local notes data
    fun saveNotes(notes: MutableList<Note>) {
        var fileName = "${System.getProperty("user.home")}/cs346Data/persistence/saves/notes.json"

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
        checkDir()

        var fileName = "${System.getProperty("user.home")}/cs346Data/persistence/saves/notes.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/notes.json"
        }

        val file = File(fileName)

        if (!file.exists()) {
            file.writeText("[]")
        }

        val notesString: String = file.readText()
        return Json.decodeFromString<MutableList<Note>>(notesString)
    }

    // saves group names data locally
    fun saveGroupNames(group: MutableList<String>) {
        var fileName = "${System.getProperty("user.home")}/cs346Data/persistence/saves/groupNames.json"

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
        checkDir()
        var fileName = "${System.getProperty("user.home")}/cs346Data/persistence/saves/groupNames.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/groupNames.json"
        }

        val file = File(fileName)
        if (!file.exists()) {
            file.writeText("[]")
        }
        val groupString: String = file.readText()

        return Json.decodeFromString<MutableList<String>>(groupString)
    }

    // saves recently deleted notes data locally
    fun saveRecentlyDeletedNotes(notes: MutableList<Note>) {
        var fileName = "${System.getProperty("user.home")}/cs346Data/persistence/saves/recentlyDeleted.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/recentlyDeleted.json"
        }
        val file = File(fileName)

        val format = Json { encodeDefaults = true }
        val recentlyDeletedNotesString = format.encodeToString(notes)

        // always overwrite, for updates or initial saving
        file.writeText(recentlyDeletedNotesString)
    }

    // loads recently deleted notes data
    fun loadRecentlyDeletedNotes(): MutableList<Note> {
        checkDir()
        var fileName = "${System.getProperty("user.home")}/cs346Data/persistence/saves/recentlyDeleted.json"

        if (testFlag) {
            fileName = "src/test/kotlin/persistence/saves/recentlyDeleted.json"
        }

        val file = File(fileName)
        if (!file.exists()) {
            file.writeText("[]")
        }


        val recentlyDeletedNotesString: String = file.readText()

        return Json.decodeFromString<MutableList<Note>>(recentlyDeletedNotesString)
    }

    private fun checkDir() {
        if (!testFlag) {
            val dir = File( "${System.getProperty("user.home")}/cs346Data/persistence/saves")
            if (!dir.exists()) {
                Files.createDirectories(Paths.get("${System.getProperty("user.home")}/cs346Data/persistence/saves"))
            }
        }
    }

}