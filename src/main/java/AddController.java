import data.Person;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.FileManager;

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
    private static String oldInput;
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
        notesField.setOnAction(event -> addPersonToDirectory());
        inputField.setOnAction(event -> addPersonToDirectory());
        emailField.setOnAction(event -> addPersonToDirectory());
        idNumberField.setOnAction(event -> addPersonToDirectory());
        nameField.setOnAction(event -> addPersonToDirectory());
        certsField.setOnAction(event -> addPersonToDirectory());
        okButton.setOnAction(event -> addPersonToDirectory());
        cancelButton.setOnAction(event -> close());
    }

    private void addPersonToDirectory() {
        if(!idNumberField.getText().isEmpty() && !nameField.getText().isEmpty()){
            Person person = new Person(inputField.getText(), idNumberField.getText(),nameField.getText(), emailField.getText(), certsField.getText(), notesField.getText());
            if(Controller.editMode && Controller.selectedPerson != null){
                deletePerson(Controller.selectedPerson);
            }
            Controller.rawDirectoryData.add(person);
            Controller.directoryData.add(person);
            Controller.checkedInData.add(person);
            FileManager.createDirectoryJsonFile(person);
            close();
            if(iCallback != null) iCallback.Callback();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Must have an ID and Name.");
            alert.showAndWait();
        }
    }

    public static void deletePerson(Person person){
        Controller.rawDirectoryData.remove(person);
        Controller.directoryData.remove(person);
        Controller.checkedInData.remove(person);
        FileManager.deleteDirectoryFile(person, ".json");
        FileManager.deleteDirectoryFile(person, ".txt");
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
