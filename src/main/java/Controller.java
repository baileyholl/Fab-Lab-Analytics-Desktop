import com.google.gson.Gson;
import data.Constants;
import data.Directory;
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
import org.hildan.fxgson.FxGson;
import org.joda.time.DateTime;
import util.FileManager;
import util.LogManager;
import util.WebUtil;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Controller implements Initializable, ICallback{
    public static ObservableList<Person> checkedInData = FXCollections.observableArrayList();
    public static ObservableList<Person> directoryData = FXCollections.observableArrayList();
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

    static String idValue;
    static Person selectedPerson;
    static boolean editMode;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Constants.rawDirectoryData = new ArrayList<>();
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
        DEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        DNotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        DStrikesColumn.setCellValueFactory(new PropertyValueFactory<>("strikes"));
        DVisitColumn.setCellValueFactory(new PropertyValueFactory<>("timesVisited"));
        Constants.rawDirectoryData.addAll(Constants.directory.getAllPersons());
        directoryData.setAll(Constants.rawDirectoryData);
        signInButton.setOnAction(event -> handleSwipe(false));
        idField.setOnAction(event -> handleSwipe(false));
        openFolderMenuButton.setOnAction(event -> openFolderExplorer());
        addMenuItem.setOnAction(event -> openAddWindow(""));
        editMenuItem.setOnAction(event-> editSelected());
        deleteMenuItem.setOnAction(event -> deleteSelected());
        forceSignInOutMenuItem.setOnAction(event -> forceSignInOut());
        directoryTab.setOnSelectionChanged(event -> refocusIdField(true));
        checkedInTab.setOnSelectionChanged(event -> refocusIdField(true));
        logTab.setOnSelectionChanged(event -> refocusIdField(true));
        exportToCSV.setOnAction(event ->  exportToCSV());
        aboutButton.setOnAction(event -> WebUtil.openWebpage(Constants.aboutLink));
        conversionButton.setOnAction(event -> convertOldGsons());
        CheckinTable.setItems(checkedInData);
        DirectoryTable.setItems(directoryData);
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
                AddController.deletePerson(selectedPerson);
            }
        }
    }

    private void editSelected() {
        TableView<Person> tableView = getFocusedTableView();
        if(tableView != null){
            int index = tableView.getSelectionModel().getFocusedIndex();
            selectedPerson = tableView.getItems().get(index);
            editMode = true;
            AddController.editMode = true;
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
            editMode = false;
            for(Person p : Constants.rawDirectoryData){
                if(p.getCardNumber().equals(idText)){
                    if(checkedInData.contains(p)){
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
        checkedInData.remove(p);
        LogManager.appendLogWithTimeStamp(forced ? p.getName() + " was signed out(MANUAL) with " + "ID: " + p.getId() : p.getName() + " was signed out with " + "ID: " + p.getId());
    }
    private void signIn(Person p, boolean forced){
        p.incrementTimesVisited();
        directoryData.removeAll(p);
        directoryData.add(p);
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
        TableView<Person> tableView = getFocusedTableView();
        if(tableView != null) {
            Person person = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex());
            idField.setText(person.getCardNumber());
            handleSwipe(true);
        }
    }

    @Override
    public void Callback() {
        if(editMode && selectedPerson != null){
            LogManager.appendLogWithTimeStamp(selectedPerson.getName() + " with ID: " + selectedPerson.getId() + " was edited.");
        }
        selectedPerson = null;
        editMode = false;
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

    //Used to convert gson files from version 1.1.x to 1.2.x.
    @Deprecated
    private void convertOldGsons(){
        ArrayList<Person> directory = new ArrayList<>();
        for(File f : Constants.directoryFiles){
            if(!f.isHidden() && f.exists()){
                try(BufferedReader br = new BufferedReader(new FileReader(f))){
                    Gson gson = new Gson();
                    //convert the json string back to object
                    Person person = gson.fromJson(br, Person.class);
                    Directory.validateUpToDateJson(person);
                    Constants.directory.put(person.getCardNumber(), person);
                    directory.add(person);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        Gson gson = FxGson.create();
        //Save directory files in fxgson
        for(Person person : directory) {
            Path path = Paths.get(Constants.directoryFolder.toString(), person.getName().replace(" ", "_") + person.getId() + ".json");
            FileManager.deleteFile(path);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
                writer.write(gson.toJson(person));
                writer.flush();
                writer.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    private void exportToCSV() {
        FileManager.getDirectoryAsCSV();
    }
}
