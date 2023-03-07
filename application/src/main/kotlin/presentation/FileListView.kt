package presentation

import business.Model
import javafx.geometry.Pos
import javafx.scene.control.*

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import java.util.*
import kotlin.collections.HashMap

class FileListView(model: Model) : IView, HBox() {
    private val model = model

    // fileListView
    private val fileListView = TreeView<String>()
    private val groupRoot = TreeItem("Groups")
    private val noteRoot = TreeItem("Notes")
    private val root = TreeItem("Categories")
    // list of dateCreated to help locate the correct note in noteList in model
    private val dateCreatedList = mutableListOf<String>()
    // list of group names to track the groups inside groupList in model
    private val groupNameList = mutableListOf<String>()

    // searchView
    private val searchView = TreeView<String>()
    private var searchFlag = false
    private val search = TreeItem("Results")
    private val searchByTitle = TreeItem("By Title")
    private val searchByContent = TreeItem("By Content")
    //TODO: better structure to store search results
    private val searchResults = mutableListOf<TreeItem<String>>()
    private val MAX_CHAR_SHOWN: Int = 15

    init {
        setupCategories()
        setupSearchView()
        setupClickAction()
        setupContextMenuForTreeItem()
    }

    fun search(input:String){
        searchFlag = true
        val mock1 = TreeItem("mock1")
        val mock2 = TreeItem("mock2")
        searchResults.add(mock1)
        searchResults.add(mock2)
        this.updateView()
    }

    fun exitSearch() {
        searchFlag = false
        this.updateView()
    }

    fun unlockNote() {
        val currSelectedNote = model.getCurrSelectedNote()
        if (currSelectedNote == null) return
        val passwordHint = currSelectedNote.passwordHint
        val correctPassword = currSelectedNote.getPwd()

        var shouldStop = false
        while (!shouldStop) {
            val alert = Alert(Alert.AlertType.CONFIRMATION)
            val inputView = VBox()
            val inputField = PasswordField()
            inputView.children.add(inputField)
            inputView.alignment = Pos.CENTER_LEFT
            inputView.spacing = 10.0
            if (passwordHint.isNotBlank() && passwordHint.isNotEmpty()) {
                inputView.children.add(Text("Hint: " + passwordHint))
            }
            alert.headerText = "Please enter the password"
            alert.dialogPane.content = inputView
            shouldStop = confirmPassword(alert.showAndWait(), inputField.text, correctPassword)
        }
    }

    fun lockNote() {
        val possiblePassword = model.getCurrSelectedNote()?.getPwd() ?: ""
        if (model.getCurrSelectedNote() != null && possiblePassword.isNotEmpty()) {
            model.lockNote()
        } else {
            val lockNoteView = LockNoteView()
            var shouldStop = false

            while (!shouldStop) {
                val alert = Alert(Alert.AlertType.CONFIRMATION)
                val dialogPane = alert.dialogPane

                dialogPane.content = lockNoteView
                alert.title = "Lock Note"
                alert.isResizable = true
                alert.width = 500.0
                alert.height = 600.0
                shouldStop = checkValidityofPassword(alert.showAndWait(), lockNoteView)
            }
        }
    }

    private fun setupContextMenuForTreeItem() {
        val lockNoteItem = MenuItem("Lock Note")
        val unlockNoteItem = MenuItem("Unlock Note")
        lockNoteItem.setOnAction {
            lockNote()
        }

        unlockNoteItem.setOnAction {
            unlockNote()
        }

        val contextMenu = ContextMenu(lockNoteItem)
        fileListView.contextMenu = contextMenu
        fileListView.setOnContextMenuRequested {
            if (fileListView.selectionModel.selectedIndex >= 0 && fileListView.selectionModel.selectedItem.parent == groupRoot) {
                fileListView.contextMenu.hide()
            } else if (fileListView.selectionModel.selectedIndex >= 0 && fileListView.selectionModel.selectedItem.parent == noteRoot) {
                val currSelectedNote = model.getCurrSelectedNote()
                val noteIsLocked = currSelectedNote?.isLocked ?: false
                contextMenu.items.clear()
                if (noteIsLocked) {
                    contextMenu.items.add(unlockNoteItem)
                } else {
                    contextMenu.items.add(lockNoteItem)
                }
            }
        }
    }

    private fun confirmPassword(
        result: Optional<ButtonType>,
        passwordEntered: String,
        correctPassword: String
    ): Boolean {
        if (result.isPresent && result.get() == ButtonType.OK) {
            if (passwordEntered != correctPassword) {
                val warningAlert = WarningAlertView(
                    "Incorret Password",
                    "The password entered does not match the one we have. Please enter again."
                )
                warningAlert.present()
                return false
            }
            model.unlockNote()
            return true
        }
        return true
    }

    private fun checkValidityofPassword(result: Optional<ButtonType>, lockNoteView: LockNoteView): Boolean {
        if (result.isPresent && result.get() == ButtonType.OK) {
            val password = lockNoteView.getPassword()
            val verifiedPassword = lockNoteView.getVerifiedPassword()
            val passwordHint = lockNoteView.getPasswordHint()
            if (password.isNullOrBlank() || password.isEmpty() || verifiedPassword.isNullOrBlank() || verifiedPassword.isEmpty()) {
                val warningAlert = WarningAlertView(
                    "Blank or Empty Passwords",
                    "You entered blank or empty string for 'Password' or 'Verify'. Please enter again"
                )
                warningAlert.present()
                lockNoteView.clearInputPassword()
                return false
            } else if (password != verifiedPassword) {
                val warningAlert = WarningAlertView(
                    "Passwords Don't Match",
                    "Your 'Password' and 'Verify' does not match. Please enter again"
                )
                warningAlert.present()
                lockNoteView.clearInputPassword()
                return false
            } else {
                model.lockNote(password, passwordHint)
                return true
            }
        }
        lockNoteView.clearHint()
        lockNoteView.clearInputPassword()
        return true
    }


    private fun setupClickAction() {
        fileListView.setOnMouseClicked {
            if (searchFlag) {
                //TODO
            } else {
                val (isUnderNoteRoot, pos) = isUnderNoteRoot()
                if (isUnderNoteRoot) {
                    val dateCreated = dateCreatedList[pos - 1]
                    model.updateSelection(dateCreated)
                } else { // selection is not under "Notes"
                    val selectedItem = fileListView.selectionModel.selectedItem
                    val parentItem = selectedItem?.parent
                    // The selectedItem is a group
                    if (parentItem == groupRoot) {
                        val groupIndex = parentItem.children.indexOf(selectedItem)
                        model.updateSelection(selectedGroupIndex = groupIndex)
                    } else if (parentItem?.parent == groupRoot) {
                        // The selectedItem is a note under a group
                        val groupIndex = parentItem.parent.children.indexOf(parentItem)
                        val noteIndex = parentItem.children.indexOf(selectedItem)
                        model.updateSelection(indices = Pair(groupIndex, noteIndex))
                    } else {
                        // The selectedItem is null or one of "Categories", "Groups" and "Notes"
                        // Select nothing by giving no arguments to updateSelection
                        model.updateSelection()
                    }
                }
            }

        }
    }

    private fun setupCategories() {
        val groupIcon = ImageView(Image("groupIcon.png", 18.0, 18.0, true, true))

        groupRoot.graphic = groupIcon

        val noteIcon = ImageView(Image("noteIcon.png", 18.0, 18.0, true, true))
        noteRoot.graphic = noteIcon

        groupRoot.isExpanded = true
        noteRoot.isExpanded = true

        root.children.addAll(groupRoot, noteRoot)
        fileListView.root = root
        root.isExpanded = true
        fileListView.isFocusTraversable = false
        // Can actually hide the tree root by using:
        // this.isShowRoot = false
        // But need to update all related selections
        this.isFocusTraversable = false

        this.children.add(fileListView)
    }

    private fun setupSearchView() {
        searchByTitle.isExpanded = true
        searchByContent.isExpanded = true
        search.isExpanded = true

        search.children.addAll(searchByTitle, searchByContent)
        searchView.root = search
        searchView.isFocusTraversable = false
    }

    private fun getNoteRootIndex(): Int {
        var retval = noteRoot.parent.children.indexOf(noteRoot) + 1
        if (groupRoot.isExpanded) {
            retval += groupRoot.children.size
            for (group in groupRoot.children) {
                if (group.isExpanded) retval += group.children.size
            }
        }
        return retval
    }

    private fun isUnderNoteRoot(): Pair<Boolean, Int> {
        val noteRootIndex = getNoteRootIndex()
        val selectedIndex = fileListView.selectionModel.selectedIndex
        return Pair(selectedIndex > noteRootIndex, selectedIndex - noteRootIndex)
    }

    // Helper function to return a string that can be put into TreeItem
    private fun getValidName(name: String): String {
        return if (name.length > MAX_CHAR_SHOWN) name.substring(0, MAX_CHAR_SHOWN) + "..."
        else name
    }

    private fun getCurrSelectedIndex():Int {
        val groupIndex = model.getCurrSelectedGroupIndex()
        val dateCreated = model.getCurrSelectedNote()?.dateCreated
        var index = 1 // start counting from "Groups"
        if (groupIndex >= 0) {
            // adding all the previous groups and notes
            // until reached the selected group
            for (i in 0 until groupIndex) {
                index += 1
                if (groupRoot.children[i].isExpanded){
                    index += groupRoot.children[i].children.size
                }
            }
            index += 1 // select the group at groupIndex
            if (dateCreated != null) {
                // current selection is a note under a group
                groupRoot.children[groupIndex].isExpanded = true
                for (note in model.groupList[groupIndex].noteList){
                    index += 1
                    if (note.dateCreated == dateCreated) break
                }
            }
        } else {
            // groupIndex < 0 meaning no group is selected
            index =
                // set index = -1 meaning in model, nothing is selected
                if (dateCreated == null) -1
                // current selection is a note under Notes
                else getNoteRootIndex() + dateCreatedList.indexOf(dateCreated) + 1
        }
        return index
    }
    override fun updateView() {
        this.children.clear()

        if (searchFlag) {
            this.children.add(searchView)
            searchByTitle.children.clear()
            searchByContent.children.clear()
            //TODO: construct the search view
            searchByTitle.children.add(searchResults[0])
            searchByContent.children.add(searchResults[1])

            searchView.refresh()
            return
        }

        this.children.add(fileListView)
        // store the selectedIndex and selectedItem before removing treeItems
        val selectedIndex = fileListView.selectionModel.selectedIndex
        val selectedItem = fileListView.selectionModel.selectedItem

        // store isExpanded field for each groupItem in a hashmap
        val isExpandedMap = HashMap<String, Boolean>()
        for(i in 0 until groupRoot.children.size){
            isExpandedMap[groupNameList[i]]= groupRoot.children[i].isExpanded
        }
        // remove all current content
        groupRoot.children.clear()
        noteRoot.children.clear()
        dateCreatedList.clear()
        groupNameList.clear()

        for (note in model.noteList) {
            val titleShown = getValidName(note.value.title)
            val noteItem = TreeItem(titleShown)
            noteRoot.children.add(noteItem)
            dateCreatedList.add(note.key)
        }

        for (group in model.groupList) {
            groupNameList.add(group.name)
            val nameShown = getValidName(group.name)
            val groupItem = TreeItem(nameShown)
            groupItem.isExpanded = isExpandedMap[group.name] ?: false
            for (note in group.noteList) {
                val titleShown = getValidName(note.title)
                groupItem.children.add(TreeItem(titleShown))
            }
            groupRoot.children.add(groupItem)
        }

        val index = getCurrSelectedIndex()
        if (index == -1) {
            // nothing is selected in model,
            // selection is one of "Categories", "Groups", and "Notes", or nothing
            if (selectedIndex == 0 || selectedIndex == 1){
                // selection is "Categories" or "Groups"
                fileListView.selectionModel.select(selectedIndex)
            } else if (selectedItem?.parent == root) {
                // selection is "Notes"
                fileListView.selectionModel.select(noteRoot)
            } else {
                fileListView.selectionModel.select(index)
            }
        } else {
            fileListView.selectionModel.select(index)
        }
        fileListView.refresh()
    }
}