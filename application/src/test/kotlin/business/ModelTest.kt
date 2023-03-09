package business

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ModelTest {
    private var model = Model()

    @BeforeEach
    fun setUp() {
        model = Model()
        model.testFlag = true
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
        assert(model.getCurrSelectedNote()?.title == "New Note")
        assert(model.getCurrSelectedNote()?.body == "")
        assert(model.getCurrSelectedGroupIndex() == -1)
    }

    @Test
    fun addNoteUnderGroup() {
        assert(!model.addNoteUnderGroup())
        // Add two groups
        model.addGroup("Group 1")
        model.addGroup("Group 2")
        // Add a new note under Group 2
        assert(model.addNoteUnderGroup())
        // Cannot add another new note
        assert(!model.addNoteUnderGroup())
        assert(model.groupList[1].noteList[0].title == "New Note")
        // Modify the note just added
        model.getCurrSelectedNote()?.title = "new title"
        // Select Group 1
        model.updateSelection(selectedGroupIndex = 0)
        // Add a new note under Group 1
        assert(model.addNoteUnderGroup())
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
        val note = model.getCurrSelectedNote()
        assert(note == null)

        val groupIndex = model.getCurrSelectedGroupIndex()
        assert(groupIndex == -1)
    }

    @Test
    fun updateSelectionForInvalidInputs() {
        model.addNote()
        model.addGroup("Group 1")

        // try invalid inputs
        model.updateSelection(selectedGroupIndex = 1)
        model.updateSelection("Not possible")
        model.updateSelection(indices = Pair(1,1))

        assert(model.getCurrSelectedGroupIndex() == 0)
    }
    @Test
    fun updateSelectionWhenSelectingNote() {
        // manually add a note
        val note = Note("Note")
        model.noteList["note1"] = note
        // add a new note
        model.addNote()
        assert(model.getCurrSelectedNote()?.title == "New Note"
                && model.getCurrSelectedNote()?.body == "")

        model.updateSelection("note1")

        assert(model.getCurrSelectedNote()?.title == "Note"
                && model.getCurrSelectedNote()?.body == "")
    }

    @Test
    fun updateSelectionWhenSelectingGroup() {
        model.addGroup("Group1")
        model.addGroup("Group2")

        model.updateSelection("", 0)
        assert(
            model.getCurrSelectedGroupIndex() == 0
                    && model.getCurrSelectedNote() == null
        )
        model.updateSelection("", 1)
        assert(
            model.getCurrSelectedGroupIndex() == 1
                    && model.getCurrSelectedNote() == null
        )
    }

    @Test
    fun updateSelectionWhenSelectingNoteUnderGroup() {
        model.addGroup("Group1")
        assert(model.addNoteUnderGroup())
        // manually add a second note
        model.groupList[0].noteList.add(Note("note"))

        model.updateSelection("", -1, Pair(0,0))
        assert(model.getCurrSelectedNote()?.title == "New Note"
                && model.getCurrSelectedNote()?.body == ""
                && model.getCurrSelectedGroupIndex() == 0)

        model.updateSelection(indices = Pair(0,1))
        assert(model.getCurrSelectedNote()?.title == "note"
                && model.getCurrSelectedNote()?.body == ""
                && model.getCurrSelectedGroupIndex() == 0)
    }
    @Test
    fun changeSelectionContent() {
        model.addNote()
        model.changeSelectionContent("new", "something")
        assert(
            model.getCurrSelectedNote()?.title == "new"
                    && model.getCurrSelectedNote()?.body == "something"
        )

        model.changeSelectionContent("", "")
        assert(
            model.getCurrSelectedNote()?.title == ""
                    && model.getCurrSelectedNote()?.body == ""
        )
    }

    // Group tests
    @Test
    fun addGroup() {
        val newGroupName = "TestGroup1"
        val expectedGroup = Group(newGroupName)
        model.addGroup(newGroupName)
        assert(model.groupList.contains(expectedGroup))
        assert(model.getCurrSelectedNote() == null && model.getCurrSelectedGroupIndex() == 0)
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
        assert(model.getCurrSelectedGroupIndex() == -1 && model.getCurrSelectedNote() == null)
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
        group1.noteList = mutableListOf<Note>(note1, note2)
        val group2 = Group()
        group2.name = newGroupName2
        model.groupList.add(group1)
        model.groupList.add(group2)
        model.renameGroup(renameGroupName, group1)
        assert(group1.name == renameGroupName)
        for (item in group1.noteList) {
            assert(item.groupName == renameGroupName)
        }
    }

    @Test
    fun lockNoteFirstTime() {
        // Arrange
        model.addNote()
        assert(model.getCurrSelectedNote() != null)
        assert(model.getCurrSelectedNote()?.getPwd() == "")
        assert(model.getCurrSelectedNote()?.passwordHint == "")

        // Act
        model.lockNote("password", "hint")

        // Assert
        assert(model.getCurrSelectedNote()?.getPwd() == "password")
        assert(model.getCurrSelectedNote()?.passwordHint == "hint")
        assert(model.getCurrSelectedNote()?.isLocked == true)
    }

    @Test
    fun lockNoteNotForTheFirstTime() {
        // Arrange
        model.addNote()
        model.lockNote("password", "hint")

        // Act
        model.getCurrSelectedNote()?.isLocked = false
        model.lockNote()

        // Assert
        assert(model.getCurrSelectedNote()?.getPwd() == "password")
        assert(model.getCurrSelectedNote()?.passwordHint == "hint")
        assert(model.getCurrSelectedNote()?.isLocked == true)
    }

    @Test
    fun unlockNote() {
        // Arrange
        model.addNote()
        model.lockNote("password", "hint")
        assert(model.getCurrSelectedNote()?.isLocked == true)

        // Act
        model.unlockNote()

        // Assert
        assert(model.getCurrSelectedNote()?.getPwd() == "password")
        assert(model.getCurrSelectedNote()?.passwordHint == "hint")
        assert(model.getCurrSelectedNote()?.isLocked == false)
    }

    @Test
    fun moveNotesToGroupWithNotes() {
        // Arrange
        val note1 = Note("notes1")
        val note2 = Note("notes2", groupName = "group")
        val group = Group("group", mutableListOf<Note>(note2))
        model.groupList.add(group)
        model.noteList = LinkedHashMap<String, Note>()
        model.noteList.put("date", note1)

        // Act
        model.moveNotes(mutableListOf<String>("date"), "group")

        // Assert
        assert(model.noteList.count() == 0)
        assert(model.groupList.count() == 1)
        assert(model.groupList[0].name == "group")
        assert(model.groupList[0].noteList.count() == 2)
        assert(model.groupList[0].noteList.contains(note1))
        assert(model.groupList[0].noteList.contains(note2))
    }

    @Test
    fun moveNotesToGroupWithoutNotes() {
        // Arrange
        val note1 = Note("notes1")
        val group = Group("group", mutableListOf<Note>())
        model.groupList.add(group)
        model.noteList = LinkedHashMap<String, Note>()
        model.noteList.put("date", note1)

        // Act
        model.moveNotes(mutableListOf<String>("date"), "group")

        // Assert
        assert(model.noteList.count() == 0)
        assert(model.groupList.count() == 1)
        assert(model.groupList[0].name == "group")
        assert(model.groupList[0].noteList.count() == 1)
        assert(model.groupList[0].noteList.contains(note1))
    }
}