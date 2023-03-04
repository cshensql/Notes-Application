package presentation
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class LockNoteView(): VBox() {
    private val passwordEnterField = PasswordField()
    private val passwordVerifyField = PasswordField()
    private val passwordHintInputField = TextField()
    private val INPUT_FIELD_WIDTH: Double = 360.0
    private val INPUT_FIELD_HEIGHT: Double = 100.0

    init {
        this.alignment = Pos.TOP_LEFT
        setupView()
    }

    private fun setupView() {

        // Set up title view
        val title = Label("Create a password for this locked notes")
        title.font = Font.font("Verdana", FontWeight.BOLD, 20.0)

        val labelFont = Font.font("Verdana", FontWeight.NORMAL, 12.0)

        // Set up the view for entering the password
        val passwordInput = HBox()
        val passwordLabel = Label("       Password: ")
        passwordLabel.font = labelFont
        passwordInput.children.add(passwordLabel)
        passwordInput.children.add(passwordEnterField)
        passwordEnterField.prefWidth = INPUT_FIELD_WIDTH
        passwordEnterField.promptText = "required"

        // Set up the view for verifying the password
        val passwordVerify = HBox()
        val verifyLabel = Label("            Verify: ")
        verifyLabel.font = labelFont
        passwordVerify.children.add(verifyLabel)
        passwordVerify.children.add(passwordVerifyField)
        passwordVerifyField.prefWidth = INPUT_FIELD_WIDTH
        passwordVerifyField.promptText = "required"

        // Set up the view for entering password hint
        val passwordHint = HBox()
        val hintLabel = Label("Password Hint: ")
        hintLabel.font = labelFont
        passwordHint.children.add(hintLabel)
        passwordHintInputField.prefWidth = INPUT_FIELD_WIDTH
        passwordHintInputField.prefHeight = INPUT_FIELD_HEIGHT
        passwordHint.children.add(passwordHintInputField)
        passwordHintInputField.promptText = "recommended"
        passwordHintInputField.alignment = Pos.TOP_LEFT

        // Set up proper width for subviews
        passwordInput.prefWidthProperty().bind(this.widthProperty())
        passwordVerify.prefWidthProperty().bind(this.widthProperty())
        passwordHint.prefWidthProperty().bind(this.widthProperty())

        // Render all the views
        this.spacing = 15.0
        this.children.add(title)
        this.children.add(passwordInput)
        this.children.add(passwordVerify)
        this.children.add(passwordHint)
    }

    fun getPassword(): String {
            return passwordEnterField.text
    }

    fun getVerifiedPassword(): String {
            return passwordVerifyField.text

    }

    fun getPasswordHint(): String {
        return passwordHintInputField.text
    }

    fun clearInputPassword() {
        passwordEnterField.text = ""
        passwordVerifyField.text = ""
    }

    fun clearHint() {
        passwordHintInputField.text = ""
    }
}