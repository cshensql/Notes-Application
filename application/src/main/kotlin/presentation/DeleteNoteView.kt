package presentation

import business.Model
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode

class DeleteNoteView(model: Model) : ListView<String>() {
    private val groupList = model.groupList
    private val noteList = model.noteList
    // list of dateCreated to help locate the correct note in model
    private val dateCreated = mutableListOf<String>()

    init {
        // enable multiple selections
        this.selectionModel.selectionMode = SelectionMode.MULTIPLE

        for (group in groupList) {
            for (note in group.noteList) {
                val noteItem = "${note.title} (${group.name}): ${note.dateCreated}"
                this.items.add(noteItem)
                dateCreated.add(note.dateCreated)
            }
        }

        for (entry in noteList) {
            val noteItem = "${entry.value.title}: ${entry.key}"
            this.items.add(noteItem)
            dateCreated.add(entry.value.dateCreated)
        }
    }

    fun getDateCreatedList(): MutableList<String> {
        val selectedIndex = this.selectionModel.selectedIndices
        val selectedItems = mutableListOf<String>()
        selectedIndex.forEach{
            selectedItems.add(dateCreated[it])
        }
        return selectedItems
    }
}