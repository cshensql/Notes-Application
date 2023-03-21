package presentation

import javafx.scene.layout.VBox
import business.Model
import javafx.collections.FXCollections
import javafx.collections.ObservableArray
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.paint.Color

class GroupNotesView(model: Model): IView, VBox() {
    private val modelPassed = model
    private val noteListView = ListView<String>()
    private val noteList = model.noteList
    private val groupList = model.groupList
    private var selectionBox = ComboBox(FXCollections.observableArrayList(mutableListOf<String>()))
    private val createNewGroupButton = Button("Create a new group?")
    // list of dateCreated to help locate the correct note in model
    private val dateCreated = mutableListOf<String>()
    init {
        this.alignment = Pos.CENTER
        this.spacing = 10.0
        model.addView(this)
        setupNoteList()
        setupGroupSelectionBox()
        setupCreateNewGroupButton()
    }

    fun getSelectedGroupName(): String {
        if (selectionBox.items.isNotEmpty()) {
            return selectionBox.selectionModel.selectedItem
        } else {
            return ""
        }
    }

    fun getDateCreatedList(): MutableList<String> {
        val selectedIndex = noteListView.selectionModel.selectedIndices
        val selectedItems = mutableListOf<String>()
        selectedIndex.forEach{
            selectedItems.add(dateCreated[it])
        }
        return selectedItems
    }

    private fun setupNoteList() {
        // enable multiple selections
        noteListView.selectionModel.selectionMode = SelectionMode.MULTIPLE

        for (entry in noteList) {
            val noteItem = "${entry.value.title}: ${entry.key}"
            noteListView.items.add(noteItem)
            dateCreated.add(entry.key)
        }
        this.children.add(noteListView)
    }

    private fun setupGroupSelectionBox() {
        val groupNames = mutableListOf<String>()
        groupList.forEach {
            groupNames.add(it.name)
        }
        selectionBox.prefWidthProperty().bind(this.widthProperty())
        selectionBox.items = FXCollections.observableArrayList(groupNames)
        if (groupNames.isNotEmpty()) {
            selectionBox.selectionModel.select(0)
            selectionBox.isVisible = true
        } else {
            selectionBox.isVisible = false
        }
        this.children.add(selectionBox)
    }

    override fun updateView() {
        // update selection options
        val groupNames = mutableListOf<String>()
        groupList.forEach {
            groupNames.add(it.name)
        }
        if (groupNames.isNotEmpty()) {
            val previousCount = selectionBox.items.count()
            selectionBox.items = FXCollections.observableArrayList(groupNames)

            // If previously there is no item in the selection box, when we add new items
            // the selection will still shown as empty unless the user choose some item in the list
            // select the first item will make sure there is some group name shown in the selection box
            if (previousCount == 0) {
                selectionBox.selectionModel.select(0)
            }
            selectionBox.isVisible = true
        } else {
            selectionBox.isVisible = false
        }
    }

    private fun setupCreateNewGroupButton() {
        createNewGroupButton.textFill = Color.BLUE
        createNewGroupButton.background = null
        this.children.add(createNewGroupButton)

        createNewGroupButton.setOnAction {
            val createGroupPrompt = TextInputDialog()
            createGroupPrompt.title = "Create Group"
            createGroupPrompt.headerText = "Enter the New Name for the Group"
            val result = createGroupPrompt.showAndWait()
            var newGroupName: String = ""
            if (result.isPresent) {
                newGroupName = createGroupPrompt.editor.text
                if (modelPassed.isAllowedGroupName(newGroupName)) {
                    modelPassed.addGroup(newGroupName)
                }
            }
        }
    }
}