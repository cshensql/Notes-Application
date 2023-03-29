package presentation

import business.Model
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text

class MoveNotesView(model: Model): IView, VBox() {
    private val modelPassed = model
    private val noteListView = ListView<String>()
    private val groupList = model.groupList
    private val fromGroupLabel = Label("from group")
    private val toGroupLabel = Label("to group")
    private val infoText = Text("If you do not choose any group for 'to group', then the selected note(s) will be moved back to the 'Notes' section.")
    private var fromGroupSelectionBox = ComboBox(FXCollections.observableArrayList(mutableListOf<String>()))
    private var toGroupSelectionBox = ComboBox(FXCollections.observableArrayList(mutableListOf<String>()))
    private val createNewGroupButton = Button("Create a new group?")
    // list of dateCreated to help locate the correct note in model
    private val dateCreated = mutableListOf<String>()

    // Constants used
    // We need to subtract these value from the view's width so that the label
    // and selection box can be presented properly
    private val fromSelectionBoxWidthSubtractionConstant: Double = 90.0
    private val toSelectionBoxWidthSubtractionConstant: Double = 75.0

    // the wrapping width for the info text
    private val wrappingWidth: Double = 300.0

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

        val toGroupBoxGroupNames = mutableListOf<String>()
        // The "" means move the notes back the "Notes" section
        toGroupBoxGroupNames.add("")
        toGroupBoxGroupNames.addAll(groupNames)

        fromGroupSelectionBox.items = FXCollections.observableArrayList(groupNames)
        toGroupSelectionBox.items = FXCollections.observableArrayList(toGroupBoxGroupNames)

        // Once the "from group" selection changes, the noteList will be updated to show the
        // notes in that group
        fromGroupSelectionBox.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            if (!newValue.isNullOrEmpty()) {
                // clear the old notes in dateCreated list
                dateCreated.clear()
                for (group in groupList) {
                    if (group.name == newValue) {
                        noteListView.items.clear()
                        group.noteList.forEach {
                            val noteItem = "${it.title}: ${it.dateCreated}"
                            noteListView.items.add(noteItem)
                            dateCreated.add(it.dateCreated)
                        }
                        break
                    }
                }
            }
        }

        if (groupNames.isNotEmpty()) {
            // If there are groups, we set the default selection to be the first group
            // so that the selection box will not show empty string
            fromGroupSelectionBox.selectionModel.select(0)
            fromGroupSelectionBox.isVisible = true

            toGroupSelectionBox.selectionModel.select(0)
            toGroupSelectionBox.isVisible = true
        } else {
            // If there is no group, then we hide both selection boxes
            fromGroupSelectionBox.isVisible = false
            toGroupSelectionBox.isVisible = false
        }

        // put the label and selection box into a HBox
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

        // bind the width of the selection box to the width of this view
        fromGroupSection.prefWidthProperty().bind(this.widthProperty())
        toGroupSection.prefWidthProperty().bind(this.widthProperty())

        fromGroupSelectionBox.prefWidthProperty().bind(this.widthProperty().subtract(fromGroupLabel.width + fromSelectionBoxWidthSubtractionConstant))
        toGroupSelectionBox.prefWidthProperty().bind(this.widthProperty().subtract(toGroupLabel.width + toSelectionBoxWidthSubtractionConstant))
        infoText.wrappingWidth = wrappingWidth

        this.children.add(fromGroupSection)
        this.children.add(noteListView)
        this.children.add(toGroupSection)
        this.children.add(infoText)


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

        val toGroupBoxGroupNames = mutableListOf<String>()
        // The "" means move the notes back the "Notes" section
        toGroupBoxGroupNames.add("")
        toGroupBoxGroupNames.addAll(groupNames)

        if (groupNames.isNotEmpty()) {
            val previousCount = fromGroupSelectionBox.items.count()
            fromGroupSelectionBox.items = FXCollections.observableArrayList(groupNames)
            toGroupSelectionBox.items = FXCollections.observableArrayList(toGroupBoxGroupNames)

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