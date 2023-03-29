package presentation

import business.Model
import business.Note
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li
class RecoverNoteView(model: Model) : ListView<String>() {
    private val recentlyDeletedNoteList = model.recentlyDeletedNoteList

    init {
        // enable multiple selections
        this.selectionModel.selectionMode = SelectionMode.MULTIPLE

        for (entry in recentlyDeletedNoteList) {
            val note = entry.value
            // add the name and datecreated of every note in recentlyDeletedNoteList to ListView.items for displaying
            val noteItem = "${note.title}: ${note.dateCreated}"
            this.items.add(noteItem)
        }
    }

    fun getRecentlyDeletedNotesSelected(): MutableList<Note> {
        val selectedIndices = this.selectionModel.selectedIndices
        val notes = recentlyDeletedNoteList.values.toList()
        val selectedItems = mutableListOf<Note>()
        // add the user selected note to selectedItems
        selectedIndices.forEach{
            selectedItems.add(notes[it])
        }
        return selectedItems
    }
}
