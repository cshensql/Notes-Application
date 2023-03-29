package business

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li

class GroupTest {
    private var group = Group()
    @BeforeEach
    fun setUp() {
        this.group = Group()
    }

    @Test
    fun getName() {
        assert(group.name == "New Group")
    }

    @Test
    fun getNoteList() {
        var emptyNoteList: MutableList<Note> = mutableListOf<Note>()
        assert(group.noteList == emptyNoteList)
    }

    @Test
    fun setName() {
        group.name = "non empty test group"
        assert(group.name == "non empty test group")
    }

    @Test
    fun setNoteList() {
        val note1 = Note()
        val note2 = Note()
        group.noteList.add(note1)
        group.noteList.add(note2)
        assert(group.noteList.contains(note1))
        assert(group.noteList.contains(note2))
    }

}