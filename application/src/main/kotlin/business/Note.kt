package business

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Note(
    var title: String = "",
    var body: String = "",
    // flag
    var isRecentlyDeleted: Boolean = false
) : Pwd() {
    // creation date of the note
    val dateCreated: String? = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))

    // modification date of the note
    var lastModified: String? = dateCreated

    fun updateModified() {
        lastModified = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
    }
}
