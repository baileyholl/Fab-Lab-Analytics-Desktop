import data.Constants;
import data.Person;
import data.PersonModel;
import data.Timestamp;
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
import util.FileManager;
import util.ICallback;
import util.LogManager;
import util.WebUtil;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable, ICallback {

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
    TableColumn<Person, String> CStrikesColumn;
    @FXML
    TableColumn<Person, String> CVisitColumn;
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
    TableColumn<Person, String> DStrikesColumn;
    @FXML
    TableColumn<Person, String> DVisitColumn;
    @FXML
    TableColumn<Person, String> DlabCertColumn;
    @FXML
    TableColumn<Person, String> ClabCertColumn;
    @FXML
    TableColumn<Person, String> DshopCertColumn;
    @FXML
    TableColumn<Person, String> CshopCertColumn;
    @FXML
    Button signInButton;
    @FXML
    TextField idField;
    @FXML
    Tab directoryTab;
    @FXML
    Tab checkedInTab;
    @FXML
    Tab logTab;
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
    MenuItem conversionButton;
    @FXML
    MenuItem exportToCSV;
    @FXML
    MenuItem aboutButton;
    @FXML
    TextArea logTextArea;

    private Person selectedPerson;

    public PersonModel checkInModel;
    public PersonModel directoryModel;
    private AddController addController;

    public void initModel(PersonModel checkInModel, PersonModel directoryModel){
        this.checkInModel = checkInModel;
        this.directoryModel = directoryModel;
        CheckinTable.setItems(checkInModel.getObservableList());
        DirectoryTable.setItems(directoryModel.getObservableList());

    }

    public void initControllers(AddController controller){
        if(addController != null){
            throw new IllegalStateException("Controllers already initialized");
        }
        this.addController = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        CNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        CCertificationsColumn.setCellValueFactory(new PropertyValueFactory<>("certifications"));
        CTimestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        CNotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        CStrikesColumn.setCellValueFactory(new PropertyValueFactory<>("strikes"));
        CVisitColumn.setCellValueFactory(new PropertyValueFactory<>("timesVisited"));
        DIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        DNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        DCertificationsColumn.setCellValueFactory(new PropertyValueFactory<>("certifications"));
        ClabCertColumn.setCellValueFactory(new PropertyValueFactory<>("certifications"));
        DlabCertColumn.setCellValueFactory(new PropertyValueFactory<>("certifications"));
        CshopCertColumn.setCellValueFactory(new PropertyValueFactory<>("shopCertification"));
        DshopCertColumn.setCellValueFactory(new PropertyValueFactory<>("shopCertification"));
        DEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        DNotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        DStrikesColumn.setCellValueFactory(new PropertyValueFactory<>("strikes"));
        DVisitColumn.setCellValueFactory(new PropertyValueFactory<>("timesVisited"));
        signInButton.setOnAction(event -> handleSwipe(false));
        idField.setOnAction(event -> handleSwipe(false));
        openFolderMenuButton.setOnAction(event -> FileManager.openFolderExplorer());
        addMenuItem.setOnAction(event -> openAddWindow(""));
        editMenuItem.setOnAction(event-> editSelected());
        deleteMenuItem.setOnAction(event -> deleteSelected());
        forceSignInOutMenuItem.setOnAction(event -> forceSignInOut());
        directoryTab.setOnSelectionChanged(event -> refocusIdField(true));
        checkedInTab.setOnSelectionChanged(event -> refocusIdField(true));
        logTab.setOnSelectionChanged(event -> refocusIdField(true));
        exportToCSV.setOnAction(event ->  FileManager.getDirectoryAsCSV(directoryModel.toCollection()));
        aboutButton.setOnAction(event -> WebUtil.openWebpage(Constants.aboutLink));
        conversionButton.setOnAction(event -> FileManager.convertOldGsons());
        logTextArea.setText(Constants.logContents);
        Platform.runLater(() -> idField.requestFocus());
    }

    private void deleteSelected() {
        TableView<Person> tableView = getFocusedTableView();
        if(tableView != null) {
            int index = tableView.getSelectionModel().getFocusedIndex();
            selectedPerson = tableView.getItems().get(index);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selectedPerson.getName() + " from directory and check in permanently?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                checkInModel.remove(selectedPerson);
                directoryModel.remove(selectedPerson);
                FileManager.deleteDirectoryFile(selectedPerson);
            }
        }
    }

    private void editSelected() {
        TableView<Person> tableView = getFocusedTableView();
        if(tableView != null){
            int index = tableView.getSelectionModel().getFocusedIndex();
            selectedPerson = tableView.getItems().get(index);
            addController.editMode = true;
            openAddWindow(selectedPerson.getCardNumber());
        }
    }

    private TableView<Person> getFocusedTableView(){
        TableView<Person> tableView = null;
        if(DirectoryTable.isFocused()){
            tableView = DirectoryTable;
        }else if(CheckinTable.isFocused()){
            tableView = CheckinTable;
        }
        return tableView;
    }

    private void handleSwipe(boolean wasForced){
        String idText = idField.getText();
        idField.setText("");
        if(!idText.isEmpty()) {
            System.out.println(idText);
            selectedPerson = null;
            addController.editMode = false;
            //TODO this is redundant iterations when using a set type.
            for(Person p : directoryModel.toCollection()){
                if(p.getCardNumber().equals(idText)){
                    if(checkInModel.contains(p)){
                        signOut(p, wasForced);
                    }else{
                        signIn(p, wasForced);
                    }
                    updateLogDisplay();
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
        if(!p.getTimeStampHistory().isEmpty()) p.getTimeStampHistory().get(p.getTimeStampHistory().size() - 1);
        checkInModel.remove(p);
        LogManager.appendLogWithTimeStamp(forced ? p.getName() + " was signed out(MANUAL) with " + "ID: " + p.getId() : p.getName() + " was signed out with " + "ID: " + p.getId());
    }
    private void signIn(Person p, boolean forced){
        p.incrementTimesVisited();
        //Todo: Get using set logic. Persons also maintain their reference in the list so removing and adding them is not necesssary.
        directoryModel.remove(p);
        directoryModel.add(p);
        checkInModel.add(p);
        p.setTimestampProperty(Timestamp.getCurrentTime());
        p.getTimeStampHistory().add(Timestamp.Now());
        LogManager.appendLogWithTimeStamp(forced ? p.getName() + " was signed in(MANUAL) with " + "ID: " + p.getId() : p.getName() + " was signed in with " + "ID: " + p.getId());
    }

    private void openAddWindow(String input){
        addController.open(this, input, getSelectedPerson());
    }

    private Person getSelectedPerson(){
        TableView<Person> tableView = getFocusedTableView();
        Person person = null;
        if(tableView != null) {
            person = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex());
        }
        return person;
    }

    private void forceSignInOut() {
        TableView<Person> tableView = getFocusedTableView();
        if(tableView != null) {
            Person person = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex());
            idField.setText(person.getCardNumber());
            handleSwipe(true);
        }
    }

    @Override
    public void Callback() {
        if(addController.editMode && selectedPerson != null){
            LogManager.appendLogWithTimeStamp(selectedPerson.getName() + " with ID: " + selectedPerson.getId() + " was edited.");
            if(addController.editMode){
                addController.editMode = false;
            }
        }
        selectedPerson = null;
        idField.setText("");
        refocusIdField(false);
    }

    private void refocusIdField(boolean runLater){
        updateLogDisplay();
        if(runLater){
            Platform.runLater(()-> idField.requestFocus());
            return;
        }
        idField.requestFocus();
    }

    private void updateLogDisplay(){
        logTextArea.setText(Constants.logContents);
    }

}
