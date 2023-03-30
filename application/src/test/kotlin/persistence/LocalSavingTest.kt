package persistence

import business.Note
import business.WindowConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.io.path.Path

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li

class LocalSavingTest {

    // setup
    val note1: Note = Note()
    val note2: Note = Note()

    var notesList = mutableListOf<Note>(note1, note2)

    var config = WindowConfig(1.0, 1.0, 1.0, 1.0)

    var groupNames = mutableListOf<String>("group1", "group2")

    var recentlyDeletedNotes = mutableListOf<Note>(note1, note2)

    private val l = LocalSaving()

    @BeforeEach
    fun setUp() {
        l.testFlag = true
    }

    @Test
    fun saveAndLoadConfig() {
        l.saveConfig(config)
        val path = Path("src/test/kotlin/persistence/saves/config.json")
        assert(Files.exists(path))

        val ret = l.loadConfig()
        assert(ret == config)
    }

    @Test
    fun saveAndLoadNotes() {
        l.saveNotes(notesList)
        val path = Path("src/test/kotlin/persistence/saves/notes.json")
        assert(Files.exists(path))

        val ret = l.loadNotes()
        for (i in 0 until ret.size) {
            assert(ret[i].dateCreated == notesList[i].dateCreated)
        }
    }

    @Test
    fun saveAndLoadGroupNames() {
        l.saveGroupNames(groupNames)
        val path = Path("src/test/kotlin/persistence/saves/groupNames.json")
        assert(Files.exists(path))

        val ret = l.loadGroupNames()
        for (i in 0 until ret.size) {
            assert(ret[i] == groupNames[i])
        }
    }

    @Test
    fun saveAndLoadRecentlyDeletedNotes() {
        l.saveRecentlyDeletedNotes(recentlyDeletedNotes)
        val path = Path("src/test/kotlin/persistence/saves/recentlyDeleted.json")
        assert(Files.exists(path))

        val ret = l.loadRecentlyDeletedNotes()
        for (i in 0 until ret.size) {
            assert(ret[i].dateCreated == recentlyDeletedNotes[i].dateCreated)
        }
    }
}