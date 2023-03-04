package business

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ModelTest {
    private var model = Model()

    @BeforeEach
    fun setUp() {
        model = Model()
    }

    @Test
    fun getNoteList() {
        val emptyMap = mutableMapOf<String, Note>()
        assert(emptyMap == model.noteList)

        for (i in 1..4) {
            model.noteList[i.toString()] = Note("note$i", "something")
        }

        assert("note1" == model.noteList["1"]?.title)
        assert("note2" == model.noteList["2"]?.title)
        assert("note3" == model.noteList["3"]?.title)
        assert("note4" == model.noteList["4"]?.title)
    }

    @Test
    fun getGroupList() {
        val empty = mutableListOf<Group>()
        assert(model.groupList == empty)
        val note1 = Note("1")
        val note2 = Note("2")
        val note3 = Note("3")

        val data = mutableListOf<Group>(
            Group(
                "group1",
                mutableListOf(note1)
            ),
            Group(
                "group2",
                mutableListOf(note2, note3)
            )
        )
        model.groupList.add(
            Group(
                "group1",
                mutableListOf(note1)
            )
        )
        model.groupList.add(
            Group(
                "group2",
                mutableListOf(note2, note3)
            )
        )
        assert(data == model.groupList)
    }

    @Test
    fun addNote() {
        assert(model.addNote())
        // can not add multiple new note
        assert(!model.addNote())
        assert(model.getCurrSelected()?.title == "New Note")
        assert(model.getCurrSelected()?.body == "")
    }

    @Test
    fun deleteNote() {
        // store some data into the model
        val note1 = Note("1")
        // let the thread wait for 1 millisecond to make sure
        Thread.sleep(1L)
        val note2 = Note("2")
        Thread.sleep(1L)
        val note3 = Note("3")

        val group1 = Group(
            "1",
            mutableListOf(note1)
        )
        model.groupList.add(group1)

        val group2 = Group(
            "2",
            mutableListOf(note2, note3)
        )
        model.groupList.add(group2)


        model.noteList["3"] = Note("note3", "something")
        model.noteList["4"] = Note("note4", "something")

        this.deleteEmptyNoteTest(model)

        val id1 = "3"
        val id2 = "4"
        val id3 = note1.dateCreated
        val id4 = note2.dateCreated
        val id5 = note3.dateCreated


        model.deleteNote(mutableListOf(id1, id3))
        assert(model.noteList.size == 1)
        assert(model.groupList[0].noteList.size == 0)

        model.deleteNote(mutableListOf(id2, id4))
        assert(model.noteList.isEmpty())
        assert(model.groupList[1].noteList.size == 1)

        model.deleteNote(mutableListOf(id5))
        assert(model.groupList[1].noteList.size == 0)
    }

    private fun modelDataCopy(model: Model): Pair<Map<String, Note>, MutableList<Group>> {
        val noteListCopy = model.noteList.toMap()
        val groupListCopy = mutableListOf<Group>()
        model.groupList.forEach {
            groupListCopy.add(Group(it.name, it.noteList.toMutableList()))
        }
        return Pair(noteListCopy, groupListCopy)
    }

    private fun deleteEmptyNoteTest(model: Model) {
        // make deep copies of the original model
        val (noteListCopy, groupListCopy) = modelDataCopy(model)
        val empty = mutableListOf<String>()
        model.deleteNote(empty)
        assert(model.noteList == noteListCopy)
        assert(model.groupList == groupListCopy)
    }


    @Test
    fun getCurrSelected() {
        val note = model.getCurrSelected()
        assert(note == null)
    }

    @Test
    fun updateSelection() {
        model.updateSelection("not possible")
        val note = model.getCurrSelected()
        assert(note == null)
        val new = Note()
        val id = new.dateCreated
        model.noteList[id] = new
        val strange = "strange key value"
        val new1 = Note("2")
        model.noteList[strange] = new1

        model.updateSelection(id)
        assert(
            model.getCurrSelected()?.title == "New Note"
                    && model.getCurrSelected()?.body == ""
        )

        model.updateSelection(strange)
        assert(
            model.getCurrSelected()?.title == "2"
                    && model.getCurrSelected()?.body == ""
        )
    }

    @Test
    fun changeSelectionContent() {
        model.addNote()
        model.changeSelectionContent("new", "something")
        assert(
            model.getCurrSelected()?.title == "new"
                    && model.getCurrSelected()?.body == "something"
        )

        model.changeSelectionContent("", "")
        assert(
            model.getCurrSelected()?.title == ""
                    && model.getCurrSelected()?.body == ""
        )
    }
    // Group tests
    @Test
    fun addGroup() {
        val newGroupName = "TestGroup1"
        val expectedGroup = Group(newGroupName)
        model.addGroup(newGroupName)
        assert(model.groupList.contains(expectedGroup))
    }

    @Test
    fun deleteGroup() {
        val newGroupName1 = "TestGroup1"
        val expectedGroup1 = Group(newGroupName1)
        model.addGroup(newGroupName1)
        val newGroupName2 = "TestGroup2"
        val expectedGroup2 = Group(newGroupName2)
        model.addGroup(newGroupName2)
        val newGroupName3 = "TestGroup3"
        val expectedGroup3 = Group(newGroupName3)
        model.addGroup(newGroupName3)
        assert(model.groupList.contains(expectedGroup1))
        assert(model.groupList.contains(expectedGroup2))
        assert(model.groupList.contains(expectedGroup3))
        val groupListToDelete = mutableListOf<Group>()
        groupListToDelete.add(expectedGroup1)
        groupListToDelete.add(expectedGroup2)
        groupListToDelete.add(expectedGroup3)
        model.deleteGroup(groupListToDelete)
        assert(model.groupList.isEmpty())
    }

    @Test
    fun renameGroupWithoutNotes() {
        val newGroupName1 = "TestGroup1"
        val renameGroupName = "RenameGroup"
        val group1 = Group()
        group1.name = newGroupName1
        model.groupList.add(group1)
        model.renameGroup(renameGroupName, group1)
        assert(group1.name == renameGroupName)
    }

    @Test
    fun renameGroupWithNotes() {
        val newGroupName1 = "TestGroup1"
        val newGroupName2 = "TestGroup2"
        val renameGroupName = "RenameGroup"
        val note1 = Note()
        note1.title = "note1"
        note1.groupName = newGroupName1
        val note2 = Note()
        note2.title = "note2"
        note2.groupName = newGroupName1
        val group1 = Group()
        group1.name = newGroupName1
        group1.noteList = mutableListOf<Note>(note1,note2)
        val group2 = Group()
        group2.name = newGroupName2
        model.groupList.add(group1)
        model.groupList.add(group2)
        model.renameGroup(renameGroupName,group1)
        assert(group1.name == renameGroupName)
        for (item in group1.noteList) {
            assert(item.groupName == renameGroupName)
        }
    }

    @Test
    fun lockNoteFirstTime() {
        // Arrange
        model.addNote()
        assert(model.getCurrSelected() != null)
        assert(model.getCurrSelected()?.getPwd() == "")
        assert(model.getCurrSelected()?.passwordHint == "")

        // Act
        model.lockNote("password", "hint")

        // Assert
        assert(model.getCurrSelected()?.getPwd() == "password")
        assert(model.getCurrSelected()?.passwordHint == "hint")
        assert(model.getCurrSelected()?.isLocked == true)
    }

    @Test
    fun lockNoteNotForTheFirstTime() {
        // Arrange
        model.addNote()
        model.lockNote("password", "hint")

        // Act
        model.getCurrSelected()?.isLocked = false
        model.lockNote()

        // Assert
        assert(model.getCurrSelected()?.getPwd() == "password")
        assert(model.getCurrSelected()?.passwordHint == "hint")
        assert(model.getCurrSelected()?.isLocked == true)
    }
    @Test
    fun unlockNote() {
        // Arrange
        model.addNote()
        model.lockNote("password", "hint")
        assert(model.getCurrSelected()?.isLocked == true)

        // Act
        model.unlockNote()

        // Assert
        assert(model.getCurrSelected()?.getPwd() == "password")
        assert(model.getCurrSelected()?.passwordHint == "hint")
        assert(model.getCurrSelected()?.isLocked == false)
    }
}