package business

import presentation.IView
import persistence.LocalSaving
import presentation.WarningAlertView

class Model {

    private val views = ArrayList<IView>()

    // LinkedHashMap: Hash table based implementation of the MutableMap interface,
    // which additionally preserves the insertion order of entries during the iteration.
    var noteList = LinkedHashMap<String, Note>()
    var groupList = mutableListOf<Group>()
    // Only contains 5 recently deleted notes
    var recentlyDeletedNoteList = LinkedHashMap<String, Note>()
    var testFlag: Boolean = false

    // currSelectedGroupIndex represents the index of the current group in groupList
    // if selected note is not null and index >= 0, a note under a group is selected
    private var currSelectedNote: Note? = null
    private var currSelectedGroupIndex: Int = -1

    // searchFlag to toggle search view
    private var searchFlag: Boolean = false

    // sort settings
    // mutableListOf indices in the following three lists:
    // sortOptions: by title, by date modified, by date created
    // sortOrders: ascending, descending
    // sortRanges: Categories(All notes), Groups, Notes, ...
    val sortSettings = mutableListOf<Int> (0, 0, 0)

    // note specific functions

    // Check if a new note is already created
    private fun containsNewNote(): Boolean {
        var containsNewNote = false

        // check if there is a new note under groups
        for (group in groupList) {
            for (note in group.noteList) {
                if (note.title == "New Note" && note.body == "") {
                    containsNewNote = true
                    break
                }
            }
            if (containsNewNote) break
        }
        if (containsNewNote) return true
        else {
            // check if there is a new note in noteList
            for (entry in noteList) {
                val note = entry.value
                if (note.title == "New Note" && note.body == "") {
                    containsNewNote = true
                    break
                }
            }
        }
        return containsNewNote
    }

    // Add a new note inside noteList
    fun addNote(): Boolean {
        var isAdded = false
        if (!containsNewNote()) {
            val newNote = Note()
            noteList[newNote.dateCreated] = newNote

            // update selection to newNote
            currSelectedNote = newNote
            currSelectedGroupIndex = -1

            isAdded = true
            saveData()
            notifyViews()
        }
        return isAdded
    }

    // Add a new note under currently selected group
    fun addNoteUnderGroup(): Boolean {
        var isAdded = false
        if (!containsNewNote() && currSelectedGroupIndex != -1) {
            val newNote = Note()
            val group = groupList[currSelectedGroupIndex]
            // modify groupName field of the new note and add it to the group
            newNote.groupName = group.name
            group.noteList.add(newNote)

            // update currSelectedNote
            currSelectedNote = newNote

            isAdded = true
            saveData()
            notifyViews()
        }
        return isAdded
    }

    fun deleteNote(dateCreatedList: MutableList<String>) {
        val selectedNote = currSelectedNote?.dateCreated
        dateCreatedList.forEach {
            if (noteList.containsKey(it)) {
                // make currSelected field points to an empty note if removed
                if (it == selectedNote) currSelectedNote = null
                // put note deleted into recently deleted notes
                if (noteList[it] != null) {
                    updateRecentlyDeletedNote(noteList[it]!!)
                }
                noteList.remove(it)
            } else {
                var flag = false
                for (group in groupList) {
                    val noteList = group.noteList
                    for (i in 0 until noteList.size) {
                        val note = noteList[i]
                        if (note.dateCreated == it) {
                            // make currSelected field points to an empty note if removed
                            if (it == selectedNote) currSelectedNote = null
                            // put a copy of note deleted into recently deleted notes
                            updateRecentlyDeletedNote(noteList[i])
                            noteList.removeAt(i)
                            flag = true
                            break
                        }
                    }
                    if (flag) break
                }
            }
        }
        saveData()
        notifyViews()
    }

    private fun updateRecentlyDeletedNote(noteAdded: Note) {
        recentlyDeletedNoteList.put(noteAdded.dateCreated, noteAdded)
        if (recentlyDeletedNoteList.size > 5) {
            val keys = recentlyDeletedNoteList.keys
            recentlyDeletedNoteList.remove(keys.first())
        }
        saveData(saveRecentlyDeleted = true)
    }

    fun recoverNote(notesSelected: MutableList<Note>) {
        notesSelected.forEach{
            recentlyDeletedNoteList.remove(it.dateCreated)

            if (it.groupName == "") {
                // If the note never belongs to any group
                noteList[it.dateCreated] = it
            } else {
                var addedBack = false
                for (group in groupList) {
                    if (group.name == it.groupName) {
                        group.noteList.add(it)
                        addedBack = true
                        break
                    }
                }

                // If the group that the note initially belongs to does not exist any more
                // just put it back to the notes section
                if (!addedBack) {
                    noteList[it.dateCreated] = it
                }
            }
        }
        notifyViews()
        saveData(saveRecentlyDeleted = true)
    }

    fun getCurrSelectedNote() = currSelectedNote
    fun getCurrSelectedGroupIndex() = currSelectedGroupIndex

    // The user needs to input only one of the following:
    // dateCreated (String): to select a note under Notes
    // selectedGroupIndex (Int): to select a group
    // indices (Pair<Int, Int>): to select a note under Groups
    //  indices.first: index of the group inside groupList
    //  indices.second: index of the note in the group
    //      (i.e., index in groupList[indices.first].noteList)
    // If no arguments given, select nothing
    // If inputs are invalid, keep the current selection
    fun updateSelection(
        dateCreated: String = "",
        selectedGroupIndex: Int = -1,
        // default values set to Pair(-1,-1) meaning empty selection
        indices: Pair<Int, Int> = Pair(-1, -1)
    ) {
        if (dateCreated == "" && selectedGroupIndex == -1 && indices == Pair(-1,-1)){
            // no arguments given, select nothing
            currSelectedNote = null
            currSelectedGroupIndex = -1
            notifyViews()
        }
        // Check if a user tries to select a group
        else if (selectedGroupIndex >= 0) {
            // change selection only if the inputs are valid
            if (selectedGroupIndex < groupList.size && dateCreated == "" && indices == Pair(-1, -1)) {
                currSelectedNote = null
                currSelectedGroupIndex = selectedGroupIndex
                notifyViews()
            }
        } else if (indices != Pair(-1, -1)) {
            // if indices are given, the note is under groups
            val (groupIndex, noteIndex) = indices
            // make sure the inputs are valid
            if (dateCreated == "" && groupIndex < groupList.size
                && noteIndex < groupList[groupIndex].noteList.size) {
                currSelectedNote = groupList[groupIndex].noteList[noteIndex]
                currSelectedGroupIndex = groupIndex
                notifyViews()
            }
        } else {
            // groupIndex and indices are not given, then note is under Notes
            val newSelection = noteList[dateCreated]
            if (newSelection != null) {
                // newSelection is inside noteList
                currSelectedNote = newSelection
                currSelectedGroupIndex = -1
                notifyViews()
            }
        }
    }

    fun changeSelectionContent(title: String, body: String) {
        currSelectedNote?.title = title
        currSelectedNote?.body = body
        saveData()
        notifyViews()
    }

    // group specific functions
    fun addGroup(groupName: String) {
        val newGroup = Group()
        newGroup.name = groupName
        groupList.add(newGroup)

        // update selection to the newGroup
        currSelectedNote = null
        currSelectedGroupIndex = groupList.size - 1
        saveData()
        notifyViews()
    }

    fun deleteGroup(groupSelectedList: MutableList<Group>) {
        // Need to check if currently selected group is removed
        val currGroupName =
            if (currSelectedGroupIndex >= 0) groupList[currSelectedGroupIndex].name
            else ""
        var removed = false
        for (group in groupSelectedList) {
            groupList.remove(group)
            group.noteList.forEach {
                it.groupName = ""
                updateRecentlyDeletedNote(it)
            }

            if (group.name == currGroupName) {
                currSelectedGroupIndex = -1
                if (currSelectedNote != null) currSelectedNote = null
                removed = true
            }
        }

        if (!removed) {
            for (i in 0 until groupList.size) {
                if (groupList[i].name == currGroupName) {
                    currSelectedGroupIndex = i
                    break
                }
            }
        }
        saveData()
        notifyViews()
    }

    fun renameGroup(newName: String, groupRenamed: Group) {
        for (entry in groupList) {
            if (entry == groupRenamed) {
                entry.name = newName
                for (item in entry.noteList) {
                    item.groupName = newName
                }
                break
            }
        }
        saveData()
        notifyViews()
    }

    // If the note has been locked before, then relock it does not require the user to enter pwd/hint again
    fun lockNote(password: String = "", hint: String = "") {
        if (currSelectedNote != null && password.isNotEmpty()) {
            // The user sets up the password for the first time
            currSelectedNote?.setPwd(password)
            currSelectedNote?.passwordHint = hint
            currSelectedNote?.isLocked = true
        } else {
            // Old password exists
            currSelectedNote?.isLocked = true
        }
        saveData()
        notifyViews()
    }

    fun unlockNote() {
        if (currSelectedNote != null) {
            currSelectedNote?.isLocked = false
            saveData()
            notifyViews()
        }
    }

    // notesToBeMoved will be a list of dateCreated of notes
    // toGroup will be the group name that we want to move the notes into
    // fromGroup is optional. If you are grouping notes, then no need to set this value
    // if you are moving notes between groups, then you need to pass the group name where the notes are moved from
    fun moveNotes(notesToBeMoved: MutableList<String>, toGroup: String, fromGroup: String = "") {
        val notesMoved = mutableListOf<Note>()
        if (fromGroup.isEmpty()) {
            // If we are grouping notes
            for (date in notesToBeMoved) {
                val note  = noteList[date]
                if (note != null) {
                    note.groupName = toGroup
                    notesMoved.add(note)
                }
            }

            for (date in notesToBeMoved) {
                noteList.remove(date)
            }
        } else {
            // If we are moving notes between groups
            val fromGroupIndex = groupList.indexOfFirst { it.name == fromGroup }
            val group = groupList[fromGroupIndex]
            for (note in group.noteList) {
                if (notesToBeMoved.contains(note.dateCreated)) {
                    note.groupName = toGroup
                    notesMoved.add(note)
                }
            }
            group.noteList.removeAll(notesMoved)

        }


        // the given toGroup is guaranteed to be a valid group name
        // by how the UI is set up
        for (group in groupList) {
            if (group.name == toGroup) {
                group.noteList.addAll(notesMoved)
                break
            }
        }
        saveData()
        notifyViews()
    }



    fun getSearchFlag() = searchFlag

    fun changeSearchFlag(input:Boolean) {
        searchFlag = input
        notifyViews()
    }

    // inputs are indices of the following three lists:
    // sortOptions: by title, by date modified, by date created
    // sortOrders: ascending, descending
    // sortRanges: Categories(All notes), Groups, Notes, ...
    fun sort(sortOption:Int, sortOrder:Int, sortRange:Int){
        sortSettings.clear()
        sortSettings.addAll(listOf(sortOption, sortOrder, sortRange))

        if (sortRange == 0 || sortRange == 1) {
            for (groupIndex in 0 until groupList.size){
                sortListOfNotes(groupList[groupIndex].noteList, sortOption, sortOrder)
            }
        }
        if (sortRange == 0 || sortRange == 2) {
            sortNoteListInModel(sortOption, sortOrder)
        }
        if (sortRange > 2) {
            sortListOfNotes(groupList[sortRange - 3].noteList, sortOption, sortOrder)
        }
        saveData()
        notifyViews()
    }

    private fun getNoteFieldBySortOption(note:Note, sortOption: Int): String {
        return when (sortOption) {
            0 -> note.title
            1 -> note.lastModified
            else -> note.dateCreated
        }
    }
    private fun sortListOfNotes(listOfNote: MutableList<Note>, sortOption: Int, sortOrder: Int){
        if (sortOrder == 0)
            listOfNote.sortBy { getNoteFieldBySortOption(it, sortOption) }
        else
            listOfNote.sortByDescending { getNoteFieldBySortOption(it, sortOption) }
    }

    private fun sortNoteListInModel(sortOption: Int, sortOrder: Int){
        val sortedList =
            if (sortOrder == 0) noteList.values.sortedBy { getNoteFieldBySortOption(it, sortOption) }
            else noteList.values.sortedByDescending { getNoteFieldBySortOption(it, sortOption) }
        noteList.clear()
        sortedList.forEach { noteList[it.dateCreated] = it }
    }

    // general functions
    fun addView(view: IView) {
        views.add(view)
    }

    fun removeView(view: IView) {
        views.remove(view)
    }

    fun notifyViews() {
        for (view in views) {
            view.updateView()
        }
    }

    // Used by other views to check if the group name is valid or not
    fun isAllowedGroupName(inputGroupName: String): Boolean {
        if (inputGroupName == "") {
            val warningAlert = WarningAlertView("Empty Group Name", "Empty Group names are not allowed")
            warningAlert.present()
            return false
        }
        for (group in groupList) {
            if (group.name == inputGroupName) {
                val warningAlert = WarningAlertView("Duplicate Group Name", "Duplicate group names are not allowed")
                warningAlert.present()
                return false
            }
        }
        return true
    }

    private fun saveData(saveRecentlyDeleted: Boolean = false) {
        val localSaving = LocalSaving()
        localSaving.testFlag = testFlag
        val notesToBeSaved = mutableListOf<Note>()
        val groupNamesToBeSaved = mutableListOf<String>()
        val recentlyDeletedNoteToBeSaved = mutableListOf<Note>()

        // Get notes to be saved
        notesToBeSaved.addAll(noteList.values)
        for (group in groupList) {
            notesToBeSaved.addAll(group.noteList)
        }

        // Get group names to be saved
        for (group in groupList) {
            groupNamesToBeSaved.add(group.name)
        }

        localSaving.saveNotes(notesToBeSaved)
        localSaving.saveGroupNames(groupNamesToBeSaved)

        if (saveRecentlyDeleted) {
            recentlyDeletedNoteToBeSaved.addAll(recentlyDeletedNoteList.values)
            localSaving.saveRecentlyDeletedNotes(recentlyDeletedNoteToBeSaved)
        }
    }
}