package view;

import data.Person;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.FileManager;
import util.LogManager;
import java.net.URL;
import java.util.ResourceBundle;

public class AddController implements Initializable {
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
    TextField shopField;
    @FXML
    TextField notesField;
    @FXML
    TextField strikesField;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;
    @FXML
    CheckBox waiverBox;

    private Stage stage;
    private Parent root;
    private Person selectedPerson;
    private MainController parentController;
    private boolean isEditMode;

    public void initParentController(MainController mainController){
        if(parentController != null) throw new IllegalStateException("Parent MainController already initialized.");
        this.parentController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notesField.setOnAction(event -> okButton.fire());
        inputField.setOnAction(event -> okButton.fire());
        emailField.setOnAction(event -> okButton.fire());
        idNumberField.setOnAction(event -> okButton.fire());
        nameField.setOnAction(event -> okButton.fire());
        certsField.setOnAction(event -> okButton.fire());
        shopField.setOnAction(event -> okButton.fire());
        strikesField.setOnAction(event -> okButton.fire());
        okButton.setOnAction(event -> completeAction());
        cancelButton.setOnAction(event -> close());
    }

    private void completeAction(){
        if(!idNumberField.getText().isEmpty() && !nameField.getText().isEmpty() && !inputField.getText().isEmpty()){
            if(!isEditMode){
                addPersonToDirectory();
            }else{
                editPerson(selectedPerson);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Must have an ID, Input Value, and Name. If user cannot swipe, input their Drivers License as the Input.");
            alert.showAndWait();
        }
    }

    private void addPersonToDirectory() {
        Person person = new Person(inputField.getText(), idNumberField.getText(),nameField.getText(), emailField.getText(),
                certsField.getText(), shopField.getText(), notesField.getText(), strikesField.getText(), getStringFromCheck(waiverBox));
        parentController.directoryModel.add(person);
        parentController.signIn(person, false);
        LogManager.appendLogWithTimeStamp(person.getName() + " was added to the directory and signed in with ID: " + person.getId());
        FileManager.saveDirectoryJsonFile(person);
        close();
    }

    private void editPerson(Person person){
        LogManager.appendLogWithTimeStamp(person.getName() + " with ID: " + person.getId() + " was edited.");
        FileManager.deleteDirectoryFile(person);
        person.set(new Person(inputField.getText(), idNumberField.getText(),nameField.getText(), emailField.getText(),
                certsField.getText(), shopField.getText(), notesField.getText(), strikesField.getText(), getStringFromCheck(waiverBox)));
        FileManager.saveDirectoryJsonFile(person);
        close();
    }

    public void open(String idValue, Person selectedPerson, boolean editMode){
        this.selectedPerson = selectedPerson;
        inputField.setText(idValue);
        if(idValue.length() == 17) idNumberField.setText(idValue.substring(6, 15)); //Parse card swipe junk to real ID number
        strikesField.setText("0");
        this.isEditMode = editMode;
        if(selectedPerson != null && isEditMode){
            inputField.setText(selectedPerson.getCardNumber());
            nameField.setText(selectedPerson.getName());
            idNumberField.setText(selectedPerson.getId());
            certsField.setText(selectedPerson.getCertifications());
            shopField.setText(selectedPerson.getShopCertification());
            notesField.setText(selectedPerson.getNotes());
            emailField.setText(selectedPerson.getEmail());
            strikesField.setText(selectedPerson.getStrikes());
            if(selectedPerson.getSignedWaiver().equals("Yes"))
                waiverBox.setSelected(true);
        }
        stage.show();
    }
    private String getStringFromCheck(CheckBox checkBox){
        return checkBox.isSelected() ? "Yes" : "No";
    }

    private void close(){
        idNumberField.clear();
        nameField.clear();
        emailField.clear();
        certsField.clear();
        notesField.clear();
        strikesField.clear();
        shopField.clear();
        waiverBox.setSelected(false);
        stage.close();
        selectedPerson = null;
        parentController.invalidateViews();
        parentController.refocusIdField(true);
    }

    public void setupStage(){
        stage = new Stage();
        stage.setTitle(!isEditMode ? "Add New User" : "Edit User");
        stage.setScene(new Scene(root,300  , 400));
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> close());
    }

    public void setRoot(Parent root){
        this.root = root;
    }
}
