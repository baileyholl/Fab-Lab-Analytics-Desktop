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
    public boolean editMode;
    public String idValue;
    private Person selectedPerson;
    private Controller parentController;

    public void initParentController(Controller controller){
        if(parentController != null) throw new IllegalStateException("Parent Controller already initialized.");
        this.parentController = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notesField.setOnAction(event -> addPersonToDirectory());
        inputField.setOnAction(event -> addPersonToDirectory());
        emailField.setOnAction(event -> addPersonToDirectory());
        idNumberField.setOnAction(event -> addPersonToDirectory());
        nameField.setOnAction(event -> addPersonToDirectory());
        certsField.setOnAction(event -> addPersonToDirectory());
        strikesField.setOnAction(event -> addPersonToDirectory());
        okButton.setOnAction(event -> addPersonToDirectory());
        cancelButton.setOnAction(event -> close());
    }

    private void addPersonToDirectory() {
        if(!idNumberField.getText().isEmpty() && !nameField.getText().isEmpty() && !inputField.getText().isEmpty()){
            Person person = new Person(inputField.getText(), idNumberField.getText(),nameField.getText(), emailField.getText(),
                    certsField.getText(), shopField.getText(), notesField.getText(), strikesField.getText());
            if(editMode && selectedPerson != null){
                if(parentController.checkInModel.contains(selectedPerson)){
                    parentController.checkInModel.get(selectedPerson.getCardNumber()).set(person);
                }
                //if(Controller.checkedInData.contains(selectedPerson)){
                //    Controller.checkedInData.add(person);
              //  }
                parentController.directoryModel.get(selectedPerson.getCardNumber()).set(person);
               // deletePerson(selectedPerson);
              //  Constants.rawDirectoryData.add(person);
              //  Controller.directoryData.add(person);
            }
            else if(!editMode) {
                //Controller.checkedInData.add(person);
                parentController.checkInModel.add(person);
                LogManager.appendLogWithTimeStamp(person.getName() + " was added to the directory and signed in with ID: " + person.getId());
                person.incrementTimesVisited();
            }
            FileManager.saveDirectoryJsonFile(person);
            close();
            if(iCallback != null) iCallback.Callback();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Must have an ID, Input Value, and Name. If user cannot swipe, input their Drivers License as the Input.");
            alert.showAndWait();
        }
    }

    private void deletePerson(Person person){
        //Constants.rawDirectoryData.remove(person);
       // Controller.directoryData.remove(person);

        FileManager.deleteDirectoryFile(person);
      //  Controller.checkedInData.remove(person);
        if(!editMode){
            LogManager.appendLogWithTimeStamp(person.getName() + " with ID: " + person.getId() + " was deleted from the directory.");
        }
    }

    public void open(ICallback iCallback, String idValue, Person selectedPerson){
        this.idValue = idValue;
        this.iCallback = iCallback;
        this.selectedPerson = selectedPerson;
        inputField.setText(idValue);
        strikesField.setText("0");
        if(selectedPerson != null){

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
        editMode = false;
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
