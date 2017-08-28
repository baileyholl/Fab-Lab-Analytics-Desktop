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
    static ICallback iCallback;
    @FXML
    TextField inputField;
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
    private String oldInput;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputField.setText(Controller.idValue);
        if(Controller.selectedPerson != null){
            Person p = Controller.selectedPerson;
            inputField.setText(p.getCardNumber());
            oldInput = p.getCardNumber();
            nameField.setText(p.getName());
            idNumberField.setText(p.getId());
            certsField.setText(p.getCertifications());
            notesField.setText(p.getNotes());
            emailField.setText(p.getEmail());
        }
        okButton.setOnAction(event -> addPersonToDirectory());
        cancelButton.setOnAction(event -> close());
    }

    private void addPersonToDirectory() {
        if(!idNumberField.getText().isEmpty() && !nameField.getText().isEmpty()){
            Person person = new Person(inputField.getText(), idNumberField.getText(),nameField.getText(), emailField.getText(), certsField.getText(), notesField.getText());
            if(Controller.editMode && Controller.selectedPerson != null){
                Controller.rawDirectoryData.remove(Controller.selectedPerson);
                Controller.directoryData.remove(Controller.selectedPerson);
                Controller.checkedInData.remove(Controller.selectedPerson);
                System.out.println(Constants.directory.remove(oldInput));
                FileManager.deleteDirectoryFile(Controller.selectedPerson);
            }
            Controller.rawDirectoryData.add(person);
            Controller.directoryData.add(person);
            Controller.checkedInData.add(person);
            FileManager.createNewDirectoryFile(person);
            close();
            if(iCallback != null) iCallback.Callback();
        }
    }
    private void close(){
        idNumberField.clear();
        nameField.clear();
        emailField.clear();
        certsField.clear();
        notesField.clear();
        stage.close();
        if(iCallback != null) iCallback.Callback();
    }
}
