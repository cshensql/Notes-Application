package presentation

import business.Group
import business.Model
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode

class RenameGroupView(model: Model) : ListView<String>() {
    private val groupList = model.groupList

    init {
        // enable single selection
        this.selectionModel.selectionMode = SelectionMode.SINGLE

        for (entry in groupList) {
            // add the name of every group in the model to ListView.items for displaying
            this.items.add(entry.name)
        }
    }

    fun getGroupSelected(): Group {
        val selectedIndices = this.selectionModel.selectedIndices
        val selectedItems = mutableListOf<Group>()
        // add all the user selected groups to selectedItems
        selectedIndices.forEach{
            selectedItems.add(groupList[it])
        }
        return selectedItems[0]
    }
}