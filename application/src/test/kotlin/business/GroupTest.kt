package business

import org.junit.jupiter.api.Test

class GroupTest {

    @Test
    fun pwdTest() {
        val group = Group("my title")
        assert("" == group.getPwd())
        group.changePwd("new password")
        assert("new password" == group.getPwd())
        assert(group.isLocked)
        group.changePwd("another password", "new password")
        assert("another password" == group.getPwd())
        assert(group.isLocked)
        group.removePwd("another password")
        assert(!group.isLocked)
    }
}