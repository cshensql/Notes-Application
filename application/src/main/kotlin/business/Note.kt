package business

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Note(
    var title: String = "",
    var body: String = "",
) : Pwd() {
    // creation date of the note
    private val dateCreated: String? = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))

    // modification date of the note
    private var lastModified: String? = dateCreated

    fun getCreationDate(): String? = this.dateCreated

    fun getLastModified(): String? = this.lastModified
    fun updateModified() {
        lastModified = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
    }
}
