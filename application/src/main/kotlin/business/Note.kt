package business

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
class Note(
    var title: String = "New Note",
    var body: String = "",
    // flag
    var isRecentlyDeleted: Boolean = false,
    var requiresPwd: Boolean = false,
    var isLocked: Boolean = false,
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

    // return true when pwd successfully changed
    fun changePwd(newPwd: String, verify: String = ""): Boolean{
        if (!requiresPwd) {
            pwd = newPwd
            requiresPwd = true
            return true
        } else if (verify == pwd) {
            pwd = newPwd
            return true
        }
        return false
    }

    // return true when pwd is successfully removed
    fun removePwd(verify: String): Boolean {
        if (verify == pwd) {
            pwd = ""
            requiresPwd = false
            return true
        }
        return false
    }
}
