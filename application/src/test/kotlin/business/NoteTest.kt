package business

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li

class NoteTest {
    private var note = Note()
    private var currTime = ""
    private var TIMETOSECONDS = 19

    // helper function to get the current time
    private fun getCurrTime() = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        .take(TIMETOSECONDS)
    @BeforeEach
    fun setUp() {
        this.note = Note()
        this.currTime = getCurrTime()
    }

    @Test
    fun getDateCreated() {
        assert(currTime == note.dateCreated.take(TIMETOSECONDS))
    }

    @Test
    fun getLastModified() {
        assert(note.lastModified == note.dateCreated)
    }

    @Test
    fun setLastModified() {
        note.lastModified = "text string"
        assert(note.lastModified == "text string")
        note.lastModified = ""
        assert(note.lastModified == "")
    }

    @Test
    fun updateModified() {
        currTime = getCurrTime()
        note.updateModified()
        assert(note.lastModified.take(TIMETOSECONDS)
                == currTime)
        Thread.sleep(500L)
        note.updateModified()
        assert(note.lastModified != note.dateCreated)
    }

    @Test
    fun getPwd() {
        assert(note.getPwd() == "")
    }


    @Test
    fun getTitle() {
        assert(note.title == "New Note")
    }

    @Test
    fun setTitle() {
        note.title = ""
        assert(note.title == "")
        note.title = "another title"
        assert(note.title == "another title")
    }

    @Test
    fun getBody() {
        assert(note.body == "")
    }

    @Test
    fun setBody() {
        note.body = "something "
        assert(note.body == "something ")
        note.body = ""
        assert(note.body == "")
    }

    @Test
    fun isRecentlyDeleted() {
        assert(!note.isRecentlyDeleted)
    }

    @Test
    fun setRecentlyDeleted() {
        note.isRecentlyDeleted = true
        assert(note.isRecentlyDeleted)
    }

    @Test
    fun isLocked() {
        assert(!note.isLocked)
    }

    @Test
    fun setLocked() {
        note.isLocked = true
        assert(note.isLocked)
    }

    @Test
    fun testInitialGroupName() {
        assert(note.groupName == "")
    }

    @Test
    fun testSettingGroupName() {
        note.groupName = "group 1"
        assert(note.groupName == "group 1")
    }
}