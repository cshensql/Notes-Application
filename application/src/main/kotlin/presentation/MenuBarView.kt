package presentation

import business.Group
import business.Model
import javafx.geometry.Insets
import business.Note
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.layout.*
import javafx.scene.paint.Color
import net.codebot.application.ConfigData
import org.controlsfx.control.textfield.CustomTextField
import java.util.*

class MenuBarView(model: Model) : IView, BorderPane() {
    // Model
    private val model = model

    // menu and searchBar
    private val menu = MenuBar()
    val searchBar = CustomTextField()
    val cancelButton = Button()

    // mutableListOf<Boolean>(searchByTitle, searchByContent): list of search options
    val searchOptions = mutableListOf<Boolean>(true, true)

    // top: menubar
    private val noteMenu = Menu("Note")
    private val groupMenu = Menu("Group")
    private val searchMenu = Menu("Search")

    // Note Menu Sub options
    private val addNote = MenuItem("Add Note")
    private val deleteNote = MenuItem("Delete Note")
    private val lockOrUnlockNote = MenuItem("Lock Note")
    private val groupNotes = MenuItem("Group Notes")
    private val moveNotes = MenuItem("Move Notes")
    private val sortNotes = MenuItem("Sort Notes")
    private val recoverNote = MenuItem("Recover Notes")

    // Group Menu Sub options
    private val addGroup = MenuItem("Add Group")
    private val deleteGroup = MenuItem("Delete Group")
    private val renameGroup = MenuItem("Rename Group")

    // Search Menu Sub options
    private val searchAll = MenuItem("Search All")
    private val searchByTitle = MenuItem("Search By Note Title")
    private val searchByContent = MenuItem("Search By Note Content")

    init {
        // Set hotkeys for each feature
        addNote.accelerator = KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN)
        deleteNote.accelerator = KeyCodeCombination(KeyCode.D, KeyCodeCombination.CONTROL_DOWN)
        lockOrUnlockNote.accelerator = KeyCodeCombination(KeyCode.L, KeyCodeCombination.CONTROL_DOWN)
        groupNotes.accelerator = KeyCodeCombination(KeyCode.G, KeyCodeCombination.CONTROL_DOWN)
        moveNotes.accelerator = KeyCodeCombination(KeyCode.M, KeyCodeCombination.CONTROL_DOWN)
        sortNotes.accelerator = KeyCodeCombination(KeyCode.S, KeyCodeCombination.ALT_DOWN)
        recoverNote.accelerator = KeyCodeCombination(KeyCode.R, KeyCodeCombination.CONTROL_DOWN)
        addGroup.accelerator = KeyCodeCombination(KeyCode.A, KeyCodeCombination.ALT_DOWN)
        deleteGroup.accelerator = KeyCodeCombination(KeyCode.D, KeyCodeCombination.ALT_DOWN)
        renameGroup.accelerator = KeyCodeCombination(KeyCode.R, KeyCodeCombination.ALT_DOWN)
        searchAll.accelerator = KeyCodeCombination(KeyCode.F, KeyCodeCombination.CONTROL_DOWN)
        searchByContent.accelerator = KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN)
        searchByTitle.accelerator = KeyCodeCombination(KeyCode.T, KeyCodeCombination.CONTROL_DOWN)


        // add css stylesheet
        this.stylesheets.add("MenuBarView.css")

        // modify and add items to searchBar
        val magnifyingGlass = Label()
        magnifyingGlass.graphic = ImageView(Image("magnifying-glass-64.png", 18.0, 18.0, true, true))
        searchBar.left = magnifyingGlass

        cancelButton.graphic = ImageView(Image("cross.png", 10.0, 10.0, true,true))

        cancelButton.styleClass.add("button")
        searchBar.right = cancelButton

        searchBar.promptText = "Search"

        searchBar.isFocusTraversable = false

        // set positions in BorderPane
        this.padding = Insets(0.0,10.0,0.0,0.0)
        this.center = menu
        this.right = searchBar

        // Add menu options to menubar
        menu.menus.add(noteMenu)
        menu.menus.add(groupMenu)
        menu.menus.add(searchMenu)

        // Add submenu to their corresponding menu
        noteMenu.items.add(addNote)
        noteMenu.items.add(deleteNote)
        noteMenu.items.add(lockOrUnlockNote)
        noteMenu.items.add(groupNotes)
        noteMenu.items.add(moveNotes)
        noteMenu.items.add(sortNotes)
        noteMenu.items.add(recoverNote)
        groupMenu.items.add(addGroup)
        groupMenu.items.add(deleteGroup)
        groupMenu.items.add(renameGroup)
        searchMenu.items.add(searchAll)
        searchMenu.items.add(searchByTitle)
        searchMenu.items.add(searchByContent)


        sortNotes.setOnAction {
            val alert = Alert(AlertType.CONFIRMATION)
            val dialogPane = alert.dialogPane
            val sortSettingsView = SortSettingsView(model)

            dialogPane.content = sortSettingsView
            alert.title = "Sort Settings"
            alert.isResizable = true
            alert.width = 300.0
            alert.height = 400.0
            val result = alert.showAndWait()

            if (result.isPresent && result.get() == ButtonType.OK){
                val (sortOption, sortOrder, sortRange) = sortSettingsView.getSelections()
                model.sort(sortOption, sortOrder, sortRange)
            }
        }

        // check for duplicate group name

        fun isAllowedGroupName(inputGroupName: String): Boolean {
            if (inputGroupName == "") {
                showErrorMessage("Empty Group names are not allowed")
                return false
            }
            for (group in this.model.groupList) {
                if (group.name == inputGroupName) {
                    showErrorMessage("Duplicate Group names are not allowed")
                    return false
                }
            }
            return true
        }

        addNote.setOnAction {
            val isAdded  =
                if (model.getCurrSelectedGroupIndex() >= 0) model.addNoteUnderGroup()
                else model.addNote()
            if (!isAdded) {
                val alert = WarningAlertView("Duplicate New Note",
                    "There is an empty note already, new note not created")
                alert.present()
            }
        }

        deleteNote.setOnAction {
            val alert = Alert(AlertType.CONFIRMATION)
            val dialogPane = alert.dialogPane
            val deleteNoteView = DeleteNoteView(model)

            dialogPane.content = deleteNoteView
            alert.title = "Delete"
            alert.isResizable = true
            alert.width = 300.0
            alert.height = 400.0

            val result = alert.showAndWait()

            if (result.isPresent && result.get() == ButtonType.OK) {
                val selectedItems = deleteNoteView.getDateCreatedList()
                model.deleteNote(selectedItems)
            }
        }

        groupNotes.setOnAction {
            var shouldStop = false
            while (!shouldStop) {
                val alert = Alert(AlertType.CONFIRMATION)
                val dialogPane = alert.dialogPane
                val groupNotesView = GroupNotesView(model)

                dialogPane.content = groupNotesView
                alert.title = "Group Notes"
                alert.isResizable = true
                alert.width = 300.0
                alert.height = 400.0
                shouldStop = successfullyGroupedNotes(alert.showAndWait(), groupNotesView)
            }

        }

        moveNotes.setOnAction {
            println("Move notes pressed")
        }

        recoverNote.setOnAction {
            val alert = Alert(AlertType.CONFIRMATION)
            val dialogPane = alert.dialogPane
            val recoverNoteView = RecoverNoteView(model)

            dialogPane.content = recoverNoteView
            alert.title = "Recover"
            alert.isResizable = true
            alert.width = ConfigData.DEFAULT_POPUP_WIDTH
            alert.height = ConfigData.DEFAULT_POPUP_HEIGHT

            val result = alert.showAndWait()

            if (result.isPresent && result.get() == ButtonType.OK) {
                val selectedItems = recoverNoteView.getRecentlyDeletedNotesSelected()
                model.recoverNote(selectedItems)
            }
        }

        addGroup.setOnAction {
            val renamePrompt = TextInputDialog()
            renamePrompt.title = "Create Group"
            renamePrompt.headerText = "Enter the New Name for the Group"
            val result = renamePrompt.showAndWait()
            var newGroupName: String = ""
            if (result.isPresent) {
                newGroupName = renamePrompt.editor.text
                if (isAllowedGroupName(newGroupName)) {
                    model.addGroup(newGroupName)
                }
            }
        }

        deleteGroup.setOnAction {
            val alert = Alert(AlertType.CONFIRMATION)
            val dialogPane = alert.dialogPane
            val deleteGroupView = DeleteGroupView(model)

            dialogPane.content = deleteGroupView
            alert.title = "Delete"
            alert.isResizable = true
            alert.width = 300.0
            alert.height = 400.0

            val result = alert.showAndWait()

            if (result.isPresent && result.get() == ButtonType.OK) {
                val selectedItems = deleteGroupView.getGroupSelectedList()
                model.deleteGroup(selectedItems)
            }
        }

        renameGroup.setOnAction {
            val alert = Alert(AlertType.CONFIRMATION)
            val dialogPane = alert.dialogPane
            val renameGroupView = RenameGroupView(model)

            dialogPane.content = renameGroupView
            alert.title = "Rename"
            alert.isResizable = true
            alert.width = 300.0
            alert.height = 400.0

            val result = alert.showAndWait()

            if (result.isPresent && result.get() == ButtonType.OK) {
                val selectedGroup = renameGroupView.getGroupSelected()
                // rename dialog pop up below
                val renamePrompt = TextInputDialog()
                renamePrompt.title = "Rename Group"
                renamePrompt.headerText = "Enter the New Name for the Group"
                val renamePromptResult = renamePrompt.showAndWait()
                var newGroupName: String = ""
                if (renamePromptResult.isPresent) {
                    newGroupName = renamePrompt.editor.text
                    if (isAllowedGroupName(newGroupName)) {
                        model.renameGroup(newGroupName, selectedGroup)
                    }
                }
            }
        }
        searchAll.setOnAction {
            searchMenu.items.forEach {
                it.styleClass.clear()
            }
            searchAll.styleClass.add("active")
            searchBar.text = ""
            searchBar.promptText = "Search"
            searchBar.border = Border(BorderStroke(Color.BLUE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))
            searchOptions[0] = true
            searchOptions[1] = true
        }

        searchByContent.setOnAction {
            searchMenu.items.forEach {
                it.styleClass.clear()
            }
            searchByContent.styleClass.add("active")
            searchBar.text = ""
            searchBar.promptText = "Search By Content"
            searchBar.border = Border(BorderStroke(Color.BLUE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))
            searchOptions[0] = false
            searchOptions[1] = true
        }

        searchByTitle.setOnAction {
            searchMenu.items.forEach {
                it.styleClass.clear()
            }
            searchByTitle.styleClass.add("active")
            searchBar.text = ""
            searchBar.promptText = "Search By Title"
            searchBar.border = Border(BorderStroke(Color.BLUE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))
            searchOptions[0] = true
            searchOptions[1] = false
        }

        searchBar.setOnMouseClicked {
            searchBar.border = null
        }
    }

    // display error message function
    private fun showErrorMessage(message: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Warning"
        alert.headerText = message
        alert.showAndWait()
    }

    // check for duplicate group name

    private fun isAllowedGroupName(inputGroupName: String): Boolean {
        if (inputGroupName == "") {
            showErrorMessage("Empty Group names are not allowed")
            return false
        }
        for (group in this.model.groupList) {
            if (group.name == inputGroupName) {
                showErrorMessage("Duplicate Group names are not allowed")
                return false
            }
        }
        return true
    }

    private fun successfullyGroupedNotes(result: Optional<ButtonType>, groupNotesView: GroupNotesView): Boolean {
        if (result.isPresent && result.get() == ButtonType.OK) {
            val selectedNotesDates = groupNotesView.getDateCreatedList()

            val selectedGroupName = groupNotesView.getSelectedGroupName()
            if (selectedGroupName.isEmpty()) {
                val warningAlertView = WarningAlertView("No Group Selected", "You haven't selected a group yet. Please select one and try again")
                warningAlertView.present()
                return false
            } else if (selectedNotesDates.isEmpty()){
                val warningAlertView = WarningAlertView("No Note Selected", "You haven't selected any note yet. Please select some notes and try again")
                warningAlertView.present()
                return false
            } else {
                model.moveNotes(selectedNotesDates, selectedGroupName)
                return true
            }
        }
        return true
    }

    override fun updateView() {
        // disable note and group functions while searching
        noteMenu.isDisable = model.getSearchFlag()
        groupMenu.isDisable = model.getSearchFlag()

        val currSelectedNote = model.getCurrSelectedNote()
        if (currSelectedNote != null) {
            val isLocked = currSelectedNote?.isLocked ?: false
            if (isLocked) {
                lockOrUnlockNote.text = "Unlock Note"
                lockOrUnlockNote.setOnAction {
                    FileListView(model).unlockNote()
                }
            } else {
                lockOrUnlockNote.text = "Lock Note"
                lockOrUnlockNote.setOnAction {
                    FileListView(model).lockNote()
                }
            }
        } else {
            lockOrUnlockNote.text = "Lock Note"
            lockOrUnlockNote.setOnAction {
                val warningAlert = WarningAlertView("No Note Selected", "\"Please select a note first\"")
                warningAlert.present()
            }
        }
    }
}