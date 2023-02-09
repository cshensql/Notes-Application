package business

import org.junit.jupiter.api.Test

class NoteTest {

    @Test
    fun noteCreation() {
        val note1 = Note()
        val currDate = "2023-02-09"
        assert("" == note1.title)

        assert("" == note1.body)

        assert("" == note1.getPwd())
        note1.changePwd("custom password")
        assert("custom password" == note1.getPwd())
        note1.changePwd("new pwd", "custom password")
        assert("new pwd" == note1.getPwd())

        assert(currDate == note1.getCreationDate()?.take(currDate.length))

        assert(currDate == note1.getLastModified()?.take(currDate.length))
        // wait for one second to update the lastModified field
        Thread.sleep(1000L)
        note1.updateModified()
        assert(note1.getCreationDate() != note1.getLastModified())
    }

    @Test
    fun noteCreatedWithContents() {
        val note2 = Note("My title", "My body here")
        assert("My title" == note2.title)
        assert("My body here" == note2.body)
    }
}