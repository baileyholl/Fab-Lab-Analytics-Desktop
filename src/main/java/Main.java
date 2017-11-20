import data.Constants;
import data.PersonModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import util.FileManager;

import java.util.ConcurrentModificationException;
import java.util.Timer;

public class Main extends Application {

    public static final String VERSION = "1.3.2";


    @Override
    public void start(Stage primaryStage) throws Exception{
        setupFiles();
        PersonModel directoryModel = new PersonModel();
        PersonModel checkinModel = new PersonModel();
        directoryModel.loadFromFiles(Constants.directoryFiles);

        //Set up resources and model structure
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/signin.fxml"));
        Parent root = mainLoader.load();
        Controller mainController = mainLoader.getController();
        mainController.initModel(checkinModel, directoryModel);

        FXMLLoader addLoader = new FXMLLoader(getClass().getResource("/entry.fxml"));
        Parent addRoot = addLoader.load();
        //.load() MUST be called before getting the controller.
        AddController addController = addLoader.getController();
        addController.setRoot(addRoot);
        addController.setupStage();
        mainController.initControllers(addController);
        addController.initParentController(mainController);
        //Parent root = FXMLLoader.load(getClass().getResource("/signin.fxml"));
        primaryStage.setTitle("Fab Lab Analytics "  + getVersion());
        primaryStage.setScene(new Scene(root, 1060  , 650));
        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to sign out all users?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                try {
                    mainController.signOutAll();
                }catch (ConcurrentModificationException e){
                    e.printStackTrace();
                    Platform.exit();
                }
                Platform.exit();
            }else{
                event.consume();
            }
        });
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.show();
        //APIBackgroundService apiBackgroundService = new APIBackgroundService();
        //Timer timer = new Timer();
        //timer.scheduleAtFixedRate(apiBackgroundService, 0, 10*1000);
    }

    private void setupFiles(){
        FileManager.setupFolders();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String getVersion(){
        return VERSION;
    }
}
