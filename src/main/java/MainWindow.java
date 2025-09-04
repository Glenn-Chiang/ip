import glendon.Glendon;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Glendon glendon;

    private Image userAvatar = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image glendonAvatar = new Image(this.getClass().getResourceAsStream("/images/glendon.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Glendon instance */
    public void setGlendon(Glendon glendon) {
        this.glendon = glendon;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = glendon.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userAvatar),
                DialogBox.getGlendonDialog(response, glendonAvatar)
        );
        userInput.clear();
    }

    @FXML
    public void sendGreeting() {
        String greeting = glendon.getGreeting();
        dialogContainer.getChildren().addAll(DialogBox.getGlendonDialog(greeting, glendonAvatar));
    }
}
