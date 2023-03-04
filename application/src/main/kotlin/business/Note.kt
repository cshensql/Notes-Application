package business

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Note(
    var title: String = "New Note",
    var body: String = "",
    // flag
    var isRecentlyDeleted: Boolean = false,
    var password: String = "",
    var isLocked: Boolean = false,
    var passwordHint: String = "",
    var groupName: String = ""
) {
    private var pwd = ""

    // creation date of the note
    val dateCreated: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))

    // modification date of the note
    var lastModified: String = dateCreated

    fun updateModified() {
        lastModified = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
    }

    fun getPwd(): String = this.pwd

    fun setPwd(password: String) {
        pwd = password
    }
}
