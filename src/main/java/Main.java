import data.Constants;
import data.Directory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.show();
    }

    private void setupFiles(){
        FileManager.setupFolders();
        Constants.directory = new Directory(Constants.directoryFiles);
    }

    @Override
    public void stop() throws Exception {

        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
