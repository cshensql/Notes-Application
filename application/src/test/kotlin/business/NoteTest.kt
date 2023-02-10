package business

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteTest {

//    @Test
//    fun noteCreation() {
//        val note1 = Note()
//        val currDate = LocalDateTime.now()
//            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
//            .take(10)
//        assert(currDate == "2023-02-10")
//        assert("" == note1.title)
//
//        assert("" == note1.body)
//
//        // assertions for password functions
//        assert("" == note1.getPwd())
//        note1.changePwd("custom password")
//        assert("custom password" == note1.getPwd())
//        assert(note1.requiresPwd)
//        note1.changePwd("new pwd", "custom password")
//        assert("new pwd" == note1.getPwd())
//        note1.removePwd("new pwd")
//        assert("" == note1.getPwd())
//        assert(!note1.isLocked)
//
//        assert(currDate == note1.dateCreated?.take(currDate.length))
//
//        assert(currDate == note1.lastModified?.take(currDate.length))
//        // wait for one second to update the lastModified field
//        Thread.sleep(1000L)
//        note1.updateModified()
//        assert(note1.dateCreated != note1.lastModified)
//
//        assert(!note1.isRecentlyDeleted)
//    }

//    @Test
//    fun noteCreatedWithContents() {
//        val note2 = Note("My title", "My body here")
//        assert("My title" == note2.title)
//        assert("My body here" == note2.body)
//    }
}