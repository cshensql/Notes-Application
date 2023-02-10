package business
import presentation.IView

class Model {

    private val views = ArrayList<IView>()
    val noteList = mutableMapOf<String, Note>()
    val groupList = mutableListOf<Group>()

    // note specific function
    fun addNote() {
        val newNote = Note()
        noteList.put(newNote.dateCreated, newNote)
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