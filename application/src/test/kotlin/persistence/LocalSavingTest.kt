package persistence

import business.Note
import business.WindowConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.io.path.Path

class LocalSavingTest {

    // setup
    val note1: Note = Note()
    val note2: Note = Note()

    var notesList = mutableListOf<Note>(note1, note2)

    var config = WindowConfig(1.0, 1.0, 1.0, 1.0)

    private val l = LocalSaving()

    @BeforeEach
    fun setUp() {
        l.testFlag = true
    }

    @Test
    fun saveAndLoadConfig() {
        l.saveConfig(config)
        val path = Path("src/main/kotlin/persistence/saves/config.json")
        assert(Files.exists(path))

        val ret = l.loadConfig()
        assert(ret == config)
    }

    @Test
    fun saveAndLoadNotes() {
        l.saveNotes(notesList)
        val path = Path("src/main/kotlin/persistence/saves/notes.json")
        assert(Files.exists(path))

        val ret = l.loadNotes()
        for (i in 0 until ret.size) {
            assert(ret[i].dateCreated == notesList[i].dateCreated)
        }
    }

}