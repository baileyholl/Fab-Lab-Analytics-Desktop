package main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

    static String idValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rawDirectoryData = new ArrayList<Person>();
        CIDColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("id"));
        CNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        CCertificationsColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("certifications"));
        CTimestampColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("timestamp"));
        CNotesColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("notes"));

        DIDColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("id"));
        DNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        DCertificationsColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("certifications"));
        DEmailColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));
        DNotesColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("notes"));
        rawDirectoryData.addAll(Constants.directory.getAllPersons());
        directoryData.setAll(rawDirectoryData);
        signInButton.setOnAction(event -> handleSignIn());
        openFolderMenuButton.setOnAction(event -> openFolderExplorer());
        addMenuItem.setOnAction(event -> openAddWindow(""));
        directoryTab.setOnSelectionChanged(event -> refocusIdField());
        CheckinTable.setItems(checkedInData);
        DirectoryTable.setItems(directoryData);
        Platform.runLater(() -> idField.requestFocus());
    }


    private void handleSignIn(){
        if(!idField.getText().isEmpty()) {
            System.out.println(idField.getText());
            for(Person p : rawDirectoryData){
                if(p.getCardNumber().equals(idField.getText())){
                    checkedInData.add(p);
                    System.out.println("Person found");
                    return;
                }
            }
            System.out.println("Person not found");
            openAddWindow(idField.getText());
        }else{
            refocusIdField();
        }
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

    @Override
    public void Callback() {
        refocusIdField();
    }
    public void test(){
        System.out.println("test");
    }

    public void refocusIdField(){
        idField.requestFocus();
    }
}
