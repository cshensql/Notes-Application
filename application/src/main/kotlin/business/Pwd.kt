package business

abstract class Pwd {
    private var pwd: String = ""
    var isLocked: Boolean = false
    fun getPwd(): String = this.pwd
    fun changePwd(newPwd: String, verify: String = "") {
        if (pwd == "") {
            isLocked = true
            pwd = newPwd
        } else if (verify == pwd) {
            pwd = newPwd
        }
    }

    fun removePwd(verify: String) {
        if (verify == pwd) {
            pwd = ""
            isLocked = false
        }
    }
}