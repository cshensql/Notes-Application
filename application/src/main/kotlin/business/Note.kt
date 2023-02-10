package business

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Note(
    var title: String = "",
    var body: String = "",
    // flag
    var isRecentlyDeleted: Boolean = false,
    var requiresPwd: Boolean = false,
    var isLocked: Boolean = false,
)  {
    private var pwd = ""

    // creation date of the note
    val dateCreated: String? = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))

    // modification date of the note
    var lastModified: String? = dateCreated

    fun updateModified() {
        lastModified = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
    }
    fun getPwd(): String = this.pwd

    fun changePwd(newPwd: String, verify: String = "") {
        if (pwd == "") {
            requiresPwd = true
            pwd = newPwd
        } else if (verify == pwd) {
            pwd = newPwd
        }
    }
    fun removePwd(verify: String) {
        if (verify == pwd) {
            pwd = ""
            requiresPwd = false
        }
    }
}
