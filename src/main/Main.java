package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        setupFiles();
        Parent root = FXMLLoader.load(getClass().getResource("signin.fxml"));
        primaryStage.setTitle("Fab Lab Analytics");
        primaryStage.setScene(new Scene(root, 1060  , 650));
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
