package business

class Folder(var name: String = "Untitled folder") : Pwd() {
    val noteList: MutableList<Note> = mutableListOf<Note>()
}