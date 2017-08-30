import data.Constants;
import data.Person;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.joda.time.DateTime;
import util.LogManager;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable, ICallback{
    public static ObservableList<Person> checkedInData = FXCollections.observableArrayList();
    public static ObservableList<Person> directoryData = FXCollections.observableArrayList();
    static ArrayList<Person> rawDirectoryData;
    @FXML
    TableView<Person> CheckinTable;
    @FXML
    TableView<Person> DirectoryTable;
    @FXML
    TableColumn<Person, String> CIDColumn;
    @FXML
    TableColumn<Person, String> CNameColumn;
    @FXML
    TableColumn<Person, String> CTimestampColumn;
    @FXML
    TableColumn<Person, String> CCertificationsColumn;
    @FXML
    TableColumn<Person, String> CNotesColumn;
    @FXML
    TableColumn<Person, String> DIDColumn;
    @FXML
    TableColumn<Person, String> DNameColumn;
    @FXML
    TableColumn<Person, String> DEmailColumn;
    @FXML
    TableColumn<Person, String> DCertificationsColumn;
    @FXML
    TableColumn<Person, String> DNotesColumn;
    @FXML
    Button signInButton;
    @FXML
    TextField idField;
    @FXML
    Tab directoryTab;
    @FXML
    Tab checkedInTab;
    @FXML
    MenuItem openFolderMenuButton;
    @FXML
    MenuItem addMenuItem;
    @FXML
    MenuItem deleteMenuItem;
    @FXML
    MenuItem editMenuItem;
    @FXML
    MenuItem forceSignInOutMenuItem;
    @FXML
    TextArea logTextArea;

    static String idValue;
    static Person selectedPerson;
    static boolean editMode;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rawDirectoryData = new ArrayList<>();
        CIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        CNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        CCertificationsColumn.setCellValueFactory(new PropertyValueFactory<>("certifications"));
        CTimestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        CNotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        DIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        DNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        DCertificationsColumn.setCellValueFactory(new PropertyValueFactory<>("certifications"));
        DEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        DNotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        rawDirectoryData.addAll(Constants.directory.getAllPersons());
        directoryData.setAll(rawDirectoryData);
        signInButton.setOnAction(event -> handleSwipe(false));
        idField.setOnAction(event -> handleSwipe(false));
        openFolderMenuButton.setOnAction(event -> openFolderExplorer());
        addMenuItem.setOnAction(event -> openAddWindow(""));
        editMenuItem.setOnAction(event-> editSelected());
        deleteMenuItem.setOnAction(event -> deleteSelected());
        forceSignInOutMenuItem.setOnAction(event -> forceSignInOut());
        directoryTab.setOnSelectionChanged(event -> refocusIdField(true));
        checkedInTab.setOnSelectionChanged(event -> refocusIdField(true));
        CheckinTable.setItems(checkedInData);
        DirectoryTable.setItems(directoryData);
        logTextArea.setText(Constants.logContents);
        Platform.runLater(() -> idField.requestFocus());
    }

    private void deleteSelected() {
        if(DirectoryTable.isFocused()){
            int index = DirectoryTable.getSelectionModel().getFocusedIndex();
            selectedPerson = DirectoryTable.getItems().get(index);
        }else if(CheckinTable.isFocused()){
            int index = CheckinTable.getSelectionModel().getFocusedIndex();
            selectedPerson = CheckinTable.getItems().get(index);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selectedPerson.getName() + " from directory and check in permanently?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK){
            System.out.println("Attempting delete");
            AddController.deletePerson(selectedPerson);
        }
    }

    private void editSelected() {
        if(DirectoryTable.isFocused()){
            int index = DirectoryTable.getSelectionModel().getFocusedIndex();
            selectedPerson = DirectoryTable.getItems().get(index);
            editMode = true;
            openAddWindow(selectedPerson.getCardNumber());
        }else if(CheckinTable.isFocused()){
            int index = CheckinTable.getSelectionModel().getFocusedIndex();
            selectedPerson = CheckinTable.getItems().get(index);
            editMode = true;
            openAddWindow(selectedPerson.getCardNumber());
        }
    }


    private void handleSwipe(boolean wasForced){
        String idText = idField.getText();
        idField.setText("");
        if(!idText.isEmpty()) {
            System.out.println(idText);
            selectedPerson = null;
            editMode = false;
            for(Person p : rawDirectoryData){
                if(p.getCardNumber().equals(idText)){
                    if(checkedInData.contains(p)){
                        signOut(p, wasForced);
                    }else{
                        signIn(p, wasForced);
                    }
                    loadLogText();
                    return;
                }
            }
            System.out.println("Person not found");
            openAddWindow(idText);
        }else{
            refocusIdField(false);
        }
    }

    private void signOut(Person p, boolean forced){
        checkedInData.remove(p);
        LogManager.appendLogWithTimeStamp(forced ? p.getName() + " was signed out(MANUAL) with " + "ID: " + p.getId() : p.getName() + " was signed out with " + "ID: " + p.getId());
    }
    private void signIn(Person p, boolean forced){
        checkedInData.add(p);
        p.setTimestampProperty(Constants.dateTimeFormatter.print(DateTime.now()));
        LogManager.appendLogWithTimeStamp(forced ? p.getName() + " was signed in(MANUAL) with " + "ID: " + p.getId() : p.getName() + " was signed in with " + "ID: " + p.getId());
    }

    private void openAddWindow(String input){
        try {
            idValue = input;
            AddController.root = FXMLLoader.load(getClass().getResource("entry.fxml"));
            AddController.stage.setTitle("Add New User");
            AddController.stage.setScene(new Scene(AddController.root, 250  , 250));
            AddController.stage.setResizable(false);
            AddController.stage.show();
            AddController.iCallback = this;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openFolderExplorer() {
        try {
            Desktop.getDesktop().open(Constants.mainFolder);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void forceSignInOut() {
        if(CheckinTable.isFocused()){
            Person person = CheckinTable.getItems().get(CheckinTable.getSelectionModel().getFocusedIndex());
            idField.setText(person.getCardNumber());
            handleSwipe(true);
        }else if(DirectoryTable.isFocused()){
            Person person = DirectoryTable.getItems().get(DirectoryTable.getSelectionModel().getFocusedIndex());
            idField.setText(person.getCardNumber());
            handleSwipe(true);
        }
    }

    @Override
    public void Callback() {
        refocusIdField(false);
        selectedPerson = null;
        editMode = false;
        idField.setText("");
    }

    private void refocusIdField(boolean runLater){
        if(runLater){
            Platform.runLater(()-> idField.requestFocus());
            return;
        }
        System.out.println("Attempt refocus");
        idField.requestFocus();
    }
    private void loadLogText(){
        logTextArea.setText(Constants.logContents);
    }
}
