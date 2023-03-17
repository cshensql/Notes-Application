package presentation

import business.Model
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

class MoveNotesView(model: Model): IView, VBox() {
    private val modelPassed = model
    private val noteListView = ListView<String>()
    private val groupList = model.groupList
    private val fromGroupLabel = Label("from group")
    private val toGroupLabel = Label("to group")
    private var fromGroupSelectionBox = ComboBox(FXCollections.observableArrayList(mutableListOf<String>()))
    private var toGroupSelectionBox = ComboBox(FXCollections.observableArrayList(mutableListOf<String>()))
    private val createNewGroupButton = Button("Create a new group?")
    // list of dateCreated to help locate the correct note in model
    private val dateCreated = mutableListOf<String>()

    init {
        this.alignment = Pos.CENTER
        this.spacing = 10.0
        model.addView(this)
        noteListView.selectionModel.selectionMode = SelectionMode.MULTIPLE
        setupNoteViewAndGroupSelectionBox()
        setupCreateNewGroupButton()

    }

    fun getSelectedFromGroupName(): String {
        if (fromGroupSelectionBox.items.isNotEmpty()) {
            return fromGroupSelectionBox.selectionModel.selectedItem
        } else {
            return ""
        }
    }

    fun getSelectedToGroupName(): String {
        if (toGroupSelectionBox.items.isNotEmpty()) {
            return toGroupSelectionBox.selectionModel.selectedItem
        } else {
            return ""
        }
    }

    private fun setupNoteViewAndGroupSelectionBox() {
        val groupNames = mutableListOf<String>()
        groupList.forEach {
            groupNames.add(it.name)
        }
        fromGroupSelectionBox.items = FXCollections.observableArrayList(groupNames)
        toGroupSelectionBox.items = FXCollections.observableArrayList(groupNames)
        fromGroupSelectionBox.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            if (newValue.isNotEmpty()) {
                for (group in groupList) {
                    if (group.name == newValue) {
                        noteListView.items.clear()
                        group.noteList.forEach {
                            val noteItem = "${it.title}: ${it.dateCreated}"
                            noteListView.items.add(noteItem)
                            dateCreated.add(it.dateCreated)
                        }
                    }
                }
            }
        }

        if (groupNames.isNotEmpty()) {
            fromGroupSelectionBox.selectionModel.select(0)
            fromGroupSelectionBox.isVisible = true

            toGroupSelectionBox.selectionModel.select(0)
            toGroupSelectionBox.isVisible = true
        } else {
            fromGroupSelectionBox.isVisible = false
            toGroupSelectionBox.isVisible = false
        }

        val fromGroupSection = HBox()
        val toGroupSection = HBox()

        fromGroupSection.alignment = Pos.TOP_LEFT
        fromGroupSection.spacing = 5.0
        toGroupSection.alignment = Pos.TOP_LEFT
        toGroupSection.spacing = 5.0

        fromGroupSection.children.add(fromGroupLabel)
        fromGroupSection.children.add(fromGroupSelectionBox)
        toGroupSection.children.add(toGroupLabel)
        toGroupSection.children.add(toGroupSelectionBox)

        fromGroupSection.prefWidthProperty().bind(this.widthProperty())
        toGroupSection.prefWidthProperty().bind(this.widthProperty())

        this.children.add(fromGroupSection)
        this.children.add(noteListView)
        this.children.add(toGroupSection)


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
                if (isAllowedGroupName(newGroupName)) {
                    modelPassed.addGroup(newGroupName)
                }
            }
        }
    }

    // check for duplicate group name

    private fun isAllowedGroupName(inputGroupName: String): Boolean {
        if (inputGroupName == "") {
            val warningAlert = WarningAlertView("Empty Group Name", "Empty Group names are not allowed")
            warningAlert.present()
            return false
        }
        for (group in modelPassed.groupList) {
            if (group.name == inputGroupName) {
                val warningAlert = WarningAlertView("Duplicate Group Name", "Duplicate group names are not allowed")
                warningAlert.present()
                return false
            }
        }
        return true
    }

    fun getDateCreatedList(): MutableList<String> {
        val selectedIndex = noteListView.selectionModel.selectedIndices
        val selectedItems = mutableListOf<String>()
        selectedIndex.forEach{
            selectedItems.add(dateCreated[it])
        }
        return selectedItems
    }

    override fun updateView() {
        // update selection options
        val groupNames = mutableListOf<String>()
        groupList.forEach {
            groupNames.add(it.name)
        }
        if (groupNames.isNotEmpty()) {
            val previousCount = fromGroupSelectionBox.items.count()
            fromGroupSelectionBox.items = FXCollections.observableArrayList(groupNames)
            toGroupSelectionBox.items = FXCollections.observableArrayList(groupNames)

            // If previously there is no item in the selection box, when we add new items
            // the selection will still shown as empty unless the user choose some item in the list
            // select the first item will make sure there is some group name shown in the selection box
            if (previousCount == 0) {
               fromGroupSelectionBox.selectionModel.select(0)
               toGroupSelectionBox.selectionModel.select(0)
            }
            fromGroupSelectionBox.isVisible = true
            toGroupSelectionBox.isVisible = true
        } else {
            fromGroupSelectionBox.isVisible = false
            toGroupSelectionBox.isVisible = false
        }
    }
}