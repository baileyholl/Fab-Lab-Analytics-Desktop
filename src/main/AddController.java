package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    static Stage stage = new Stage();
    static Parent root;
    @FXML
    TextField idNumberField;
    @FXML
    TextField nameField;
    @FXML
    TextField emailField;
    @FXML
    TextField certsField;
    @FXML
    TextField notesField;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;
    @FXML
    CheckBox remainOpenCheck;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                addPersonToDirectory();
            }
        });
    }

    private void addPersonToDirectory() {
        if(!idNumberField.getText().isEmpty() && !nameField.getText().isEmpty()){
            Person person = new Person(Controller.idValue, idNumberField.getText(),nameField.getText(), emailField.getText(), certsField.getText(), notesField.getText());
            Controller.rawDirectoryData.add(person);
            Controller.directoryData.add(person);
            Controller.checkedInData.add(person);
            FileManager.createNewDirectoryFile(person);
            if(!remainOpenCheck.isSelected()){
                idNumberField.clear();
                nameField.clear();
                emailField.clear();
                certsField.clear();
                notesField.clear();
                stage.close();
                System.out.println("Closing");
            }
        }
    }
}
