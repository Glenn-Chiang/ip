import glendon.Glendon;
import glendon.GlendonException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {
    private static final String dataPath = "./data/tasks.txt";

    private Glendon glendon;

    @Override
    public void start(Stage stage) {
        try {
            glendon = new Glendon(dataPath);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            MainWindow mainWindow = fxmlLoader.<MainWindow>getController();
            mainWindow.setGlendon(glendon);  // inject the Glendon instance
            mainWindow.sendGreeting();
            stage.show();
        } catch (IOException | GlendonException e) {
            e.printStackTrace();
        }
    }
}
