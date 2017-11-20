import data.Constants;
import data.Person;
import data.PersonModel;
import data.Timestamp;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import util.FileManager;
import util.LogManager;
import util.WebUtil;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

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
    TableColumn<Person, String> CWaiverColumn;
    @FXML
    TableColumn<Person, String> DWaiverColumn;


    @FXML
    Button signInButton;
    @FXML
    TextField idField;
    @FXML
    TextField searchField;
    @FXML
    Tab directoryTab;
    @FXML
    Tab checkedInTab;
    @FXML
    TabPane tabPane;
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

    private PersonModel checkInModel;
    protected PersonModel directoryModel;
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
        DWaiverColumn.setCellValueFactory(new PropertyValueFactory<>("signedWaiver"));
        CWaiverColumn.setCellValueFactory(new PropertyValueFactory<>("signedWaiver"));
        DEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        DNotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        DStrikesColumn.setCellValueFactory(new PropertyValueFactory<>("strikes"));
        DVisitColumn.setCellValueFactory(new PropertyValueFactory<>("timesVisited"));

        setupCellFactories();
        signInButton.setOnAction(event -> handleSwipe(false));
        idField.setOnAction(event -> handleSwipe(false));

        searchField.setOnKeyTyped(event -> focusSearch());
        openFolderMenuButton.setOnAction(event -> FileManager.openFolderExplorer());
        addMenuItem.setOnAction(event -> openAddWindow("", false));
        editMenuItem.setOnAction(event-> editSelected());
        deleteMenuItem.setOnAction(event -> deleteSelected());
        forceSignInOutMenuItem.setOnAction(event -> forceSignInOut());
        //directoryTab.setOnSelectionChanged(event -> refocusIdField(true));
        //checkedInTab.setOnSelectionChanged(event -> refocusIdField(true));
        logTab.setOnSelectionChanged(event -> {
            Platform.runLater(()->logTextArea.setScrollTop(Double.MAX_VALUE));
            refocusIdField(true);
        });
        exportToCSV.setOnAction(event ->  FileManager.getDirectoryAsCSV(directoryModel.getObservableList()));
        aboutButton.setOnAction(event -> WebUtil.openWebpage(Constants.aboutLink));
        logTextArea.setText(Constants.logContents);
        Platform.runLater(() -> idField.requestFocus());
    }

    private void setupCellFactories() {
        DshopCertColumn.setCellFactory(column -> new TableCell<Person, String>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null || empty){
                    setText(null);
                    setStyle("");
                }else {
                    setText(getString());
                    if (getString().toLowerCase().equals("red")) {
                        setStyle("-fx-background-color: #C14242");
                    } else if (getString().toLowerCase().equals("yellow")) {
                        setStyle("-fx-background-color: #E8E868");
                    } else if (getString().toLowerCase().equals("green")) {
                        setStyle("-fx-background-color: #4BE64B");
                    }else{
                        setStyle("");
                    }
                }
            }
            private String getString() {
                return getItem() == null ? "" : getItem();
            }
        });
        DWaiverColumn.setCellFactory(column -> new TableCell<Person, String>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null || empty){
                    setText(null);
                    setStyle("");
                }else {
                    setText(getString());
                    if (getString().toLowerCase().equals("no")) {
                        setStyle("-fx-text-fill: #C14242");
                    }
                }
            }
            private String getString() {
                return getItem() == null ? "" : getItem();
            }
        });
        CWaiverColumn.setCellFactory(DWaiverColumn.getCellFactory());
        CshopCertColumn.setCellFactory(DshopCertColumn.getCellFactory());
        CCertificationsColumn.setCellFactory(DshopCertColumn.getCellFactory());
        DCertificationsColumn.setCellFactory(DshopCertColumn.getCellFactory());
    }

    private void focusSearch() {
        for(int i = 0; i < DirectoryTable.getItems().size(); i++){
            if(DNameColumn.getCellData(i).toLowerCase().contains(searchField.getText().toLowerCase())){
                tabPane.getSelectionModel().select(directoryTab);
                DirectoryTable.getFocusModel().focus(i);
                DirectoryTable.scrollTo(i);
                break;
            }
        }
    }

    private void deleteSelected() {
        TableView<Person> tableView = getFocusedTableView();
        if(tableView != null) {
            int index = tableView.getSelectionModel().getFocusedIndex();
            selectedPerson = tableView.getItems().get(index);
            if(selectedPerson != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selectedPerson.getName() + " from directory and check in permanently?");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    checkInModel.remove(selectedPerson);
                    directoryModel.remove(selectedPerson);
                    FileManager.deleteDirectoryFile(selectedPerson);
                    LogManager.appendLogWithTimeStamp(selectedPerson.getName() + " with ID: " + selectedPerson.getId() + " was deleted from the directory.");
                }
            }
        }
    }

    private void editSelected() {
        TableView<Person> tableView = getFocusedTableView();
        if(tableView != null){
            int index = tableView.getSelectionModel().getFocusedIndex();
            selectedPerson = tableView.getItems().get(index);
            openAddWindow(selectedPerson.getCardNumber(), true);
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
        String cardInput = idField.getText();
        Person directoryPerson = directoryModel.getByCardNumber(cardInput);
        System.out.println(directoryPerson);
        if(!directoryModel.contains(directoryPerson)){
            openAddWindow(cardInput, false);
            return;
        }
        if(!checkInModel.contains(directoryPerson)){
            signIn(directoryPerson, wasForced);
            refocusIdField(true);
            return;
        }
        signOut(directoryPerson, wasForced);
        refocusIdField(true);
    }

    public void signOut(Person p, boolean forced){
        if(!p.getTimeStampHistory().isEmpty()) p.getTimeStampHistory().get(p.getTimeStampHistory().size() - 1).setEnd(Timestamp.getCurrentTime());
        FileManager.saveDirectoryJsonFile(p);
        checkInModel.remove(p);
        LogManager.appendLogWithTimeStamp(forced ? p.getName() + " was signed out(MANUAL) with " + "ID: " + p.getId() : p.getName() + " was signed out with " + "ID: " + p.getId());
        idField.clear();
    }

    public void signIn(Person p, boolean forced){
        p.incrementTimesVisited();
        checkInModel.add(p);
        p.setTimestampProperty(Timestamp.getCurrentTime());
        p.getTimeStampHistory().add(Timestamp.Now());
        LogManager.appendLogWithTimeStamp(forced ? p.getName() + " was signed in(MANUAL) with " + "ID: " + p.getId() : p.getName() + " was signed in with " + "ID: " + p.getId());
        invalidateViews();
        idField.clear();
    }

    private void openAddWindow(String input, boolean isEditMode){
        addController.open(input, getSelectedPerson(), isEditMode);
    }

    public void signOutAll(){
        //Stop concurrent modifications
        List<Person> list = new ArrayList<>();
        list.addAll(checkInModel.getObservableList());
        for (Person p :list) {
            signOut(p, false);
        }
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

    public void refocusIdField(boolean runLater){
        updateLogDisplay();
        if(runLater){
            Platform.runLater(()-> idField.requestFocus());
            return;
        }
        idField.requestFocus();
    }

    public void invalidateViews(){
        DirectoryTable.refresh();
        CheckinTable.refresh();
        updateLogDisplay();
    }

    private void updateLogDisplay(){
        logTextArea.setText(Constants.logContents);
    }
}
