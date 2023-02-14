package business

import presentation.IView

class Model {

    private val views = ArrayList<IView>()
    val noteList = mutableMapOf<String, Note>()
    val groupList = mutableListOf<Group>()
    private var currSelected = Note("", "")   // represent empty selection

    // note specific function
    fun addNote() : Boolean {
        val newNote = Note()
        var containsNewNote = false
        var isAdded = false
        noteList.forEach {
            if (it.value.title == "New Note" && it.value.body == "") {
                // a new note has already been created
                containsNewNote = true
            }
        }
        if (!containsNewNote) {
            noteList[newNote.dateCreated] = newNote
            currSelected = newNote
            isAdded = true
            notifyViews()
        }
        return isAdded
    }

    fun addNoteUnderGroup() {
        // TODO
    }

    fun getCurrSelected() = currSelected

    fun updateSelection(dateCreated: String) {
        val newSelection = noteList[dateCreated]
        if (newSelection != null) {
            currSelected = newSelection
            notifyViews()
        }
    }

    fun changeSelectionContent(title: String, body: String) {
        currSelected.title = title
        currSelected.body = body
        notifyViews()
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

}