package view;

import data.Certification;
import data.Person;
import data.Timestamp;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    @FXML
    TextField idNumberField;
    @FXML
    TextField nameField;
    @FXML
    TextField emailField;
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
    @FXML
    Button certsButton;

    private Stage stage;
    private Parent root;
    private Person selectedPerson;
    private MainController parentController;
    private boolean isEditMode;
    private CertController certController;
    private ArrayList<Certification> certs;

    public void initParentController(MainController mainController){
        if(parentController != null) throw new IllegalStateException("Parent MainController already initialized.");
        this.parentController = mainController;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notesField.setOnAction(event -> okButton.fire());
        emailField.setOnAction(event -> okButton.fire());
        idNumberField.setOnAction(event -> okButton.fire());
        nameField.setOnAction(event -> okButton.fire());
        strikesField.setOnAction(event -> okButton.fire());
        okButton.setOnAction(event -> completeAction());
        cancelButton.setOnAction(event -> close());
        certsButton.setOnAction(event -> openCertWindow());

        certs = new ArrayList<>();
    }

    private void openCertWindow() {
        certController = parentController.getCertController();
        if (selectedPerson==null) {
            System.out.println("Creating person to send to cert controller:");
            System.out.println("selected person - " + selectedPerson);
            certController.open(new Person(idNumberField.getText(), nameField.getText(), emailField.getText(),
                    notesField.getText(), strikesField.getText(), getStringFromCheck(waiverBox)));
        } else {
            certController.open(selectedPerson);
        }
    }

    private void completeAction(){
        if(!idNumberField.getText().isEmpty() && !nameField.getText().isEmpty()){
            if(!isEditMode){
                addPersonToDirectory();
            }else{
                editPerson(selectedPerson);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Must have an ID and Name. If user cannot swipe, input their Drivers License as the ID.");
            alert.showAndWait();
        }
    }

    public void setCerts(ArrayList<Certification> certs){
        this.certs = certs;
    }

    private void addPersonToDirectory() {
        Person person = new Person(idNumberField.getText(),nameField.getText(), emailField.getText(),
                notesField.getText(), strikesField.getText(), getStringFromCheck(waiverBox));
        person.setCertifications(certs);
        parentController.directoryModel.add(person);
        parentController.signIn(person, false);
        LogManager.appendLogWithTimeStamp(person.getName() + " was added to the directory and signed in with ID: " + person.getId());
        FileManager.saveDirectoryJsonFile(person);
        close();
    }

    private void editPerson(Person person){
        LogManager.appendLogWithTimeStamp(person.getName() + " with ID: " + person.getId() + " was edited.");
        FileManager.deleteDirectoryFile(person);
        person.set(new Person(idNumberField.getText(),nameField.getText(), emailField.getText(),
                 notesField.getText(), strikesField.getText(), getStringFromCheck(waiverBox)));
        person.setCertifications(certs);
        FileManager.saveDirectoryJsonFile(person);
        close();
    }

    public void open(String idValue, Person selectedPerson, boolean editMode){
        this.selectedPerson = selectedPerson;
        idNumberField.setText(idValue);
        if(idValue.length() == 17) idNumberField.setText(idValue.substring(6, 15)); //Parse card swipe junk to real ID number
        strikesField.setText("0");
        this.isEditMode = editMode;
        if(selectedPerson != null && isEditMode){
            nameField.setText(selectedPerson.getName());
            idNumberField.setText(selectedPerson.getId());
            notesField.setText(selectedPerson.getNotes());
            emailField.setText(selectedPerson.getEmail());
            strikesField.setText(selectedPerson.getStrikes());
            if(selectedPerson.getSignedWaiver().equals("Yes"))
                waiverBox.setSelected(true);
            this.certs = selectedPerson.getCertifications();
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
        notesField.clear();
        strikesField.clear();
        waiverBox.setSelected(false);
        stage.close();
        selectedPerson = null;
        parentController.invalidateViews();
        parentController.refocusIdField(true);
    }

    public void setupStage(){
        stage = new Stage();
        stage.setTitle(!isEditMode ? "Add New User" : "Edit User");
        //Bailey hardcoded the values below like a total jerk ugh
        stage.setScene(new Scene(root,316  , 260));
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> close());
    }

    public void setRoot(Parent root){
        this.root = root;
    }
}
