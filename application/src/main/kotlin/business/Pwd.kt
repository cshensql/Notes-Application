package business

abstract class Pwd {
    private var pwd: String = ""
    fun getPwd(): String = this.pwd
    fun changePwd(newPwd: String, verify: String = "") {
        if (pwd == "") {
            pwd = newPwd
        } else if (verify == pwd) {
            pwd = newPwd
        }
    }
}