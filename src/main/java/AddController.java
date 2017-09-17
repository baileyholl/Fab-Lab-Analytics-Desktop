import data.Constants;
import data.Person;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.FileManager;
import util.ICallback;
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

    private Stage stage;
    private Parent root;
    private ICallback iCallback;
    private String idValue;
    private Person selectedPerson;
    private Controller parentController;
    private boolean isEditMode;

    public void initParentController(Controller controller){
        if(parentController != null) throw new IllegalStateException("Parent Controller already initialized.");
        this.parentController = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notesField.setOnAction(event -> okButton.fire());
        inputField.setOnAction(event -> okButton.fire());
        emailField.setOnAction(event -> okButton.fire());
        idNumberField.setOnAction(event -> okButton.fire());
        nameField.setOnAction(event -> okButton.fire());
        certsField.setOnAction(event -> okButton.fire());
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
                certsField.getText(), shopField.getText(), notesField.getText(), strikesField.getText());
        parentController.checkInModel.add(person);
        parentController.directoryModel.add(person);
        LogManager.appendLogWithTimeStamp(person.getName() + " was added to the directory and signed in with ID: " + person.getId());
        person.incrementTimesVisited();
        FileManager.saveDirectoryJsonFile(person);
        close();
        if(iCallback != null) iCallback.Callback();
    }

    private void editPerson(Person person){
        LogManager.appendLogWithTimeStamp(person.getName() + " with ID: " + person.getId() + " was edited.");
    }

    private void deletePerson(Person person){
        parentController.checkInModel.remove(person);
        parentController.directoryModel.remove(person);
        FileManager.deleteDirectoryFile(person);
        LogManager.appendLogWithTimeStamp(person.getName() + " with ID: " + person.getId() + " was deleted from the directory.");
    }

    public void open(ICallback iCallback, String idValue, Person selectedPerson, boolean editMode){
        this.idValue = idValue;
        this.iCallback = iCallback;
        this.selectedPerson = selectedPerson;
        inputField.setText(idValue);
        strikesField.setText("0");
        this.isEditMode = editMode;
        if(selectedPerson != null && isEditMode){
            inputField.setText(selectedPerson.getCardNumber());
            nameField.setText(selectedPerson.getName());
            idNumberField.setText(selectedPerson.getId());
            certsField.setText(selectedPerson.getCertifications());
            notesField.setText(selectedPerson.getNotes());
            emailField.setText(selectedPerson.getEmail());
            strikesField.setText(selectedPerson.getStrikes());
        }
        stage.show();
    }

    private void close(){
        idNumberField.clear();
        nameField.clear();
        emailField.clear();
        certsField.clear();
        notesField.clear();
        strikesField.clear();
        stage.close();
        selectedPerson = null;
        if(iCallback != null) iCallback.Callback();
    }

    public void setupStage(){
        stage = new Stage();
        stage.setTitle("Add New User");
        stage.setScene(new Scene(root,325  , 400));
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> close());
    }

    public void setRoot(Parent root){
        this.root = root;
    }
}
