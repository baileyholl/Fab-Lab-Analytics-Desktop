import data.Constants;
import data.Directory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.FileManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        setupFiles();
        Parent root = FXMLLoader.load(getClass().getResource("/signin.fxml"));
        primaryStage.setTitle("Fab Lab Analytics");
        primaryStage.setScene(new Scene(root, 1060  , 650));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to sign out all users?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {

                }else {
                    Platform.exit();
                }
            }
        });
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.show();
    }

    private void setupFiles(){
        FileManager.setupFolders();
        Constants.directory = new Directory(Constants.directoryFiles);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
