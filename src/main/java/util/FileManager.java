package util;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import data.Constants;
import data.Person;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hildan.fxgson.FxGson;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.security.auth.login.LoginContext;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FileManager {

    private static String OSDirectoryPath;
    private static String OSLogPath;
    private static String OSAnalyticsPath;
    private static Log log = LogFactory.getLog(FileManager.class);

    public static void setupFolders() {
        System.out.println("Searching for resource folder");
        Constants.mainFolder = new File(getFilePath());
        Constants.directoryFolder = new File(Constants.mainFolder.toString() + OSDirectoryPath);
        Constants.logFolder = new File(Constants.mainFolder.toString() + OSLogPath);
        Constants.analyticsFolder = new File(Constants.mainFolder.toString() + OSAnalyticsPath);
        if (!(Constants.mainFolder.exists() && Constants.directoryFolder.exists() && Constants.logFolder.exists() && Constants.analyticsFolder.exists())) {
            if (!(Constants.mainFolder.mkdir() && Constants.directoryFolder.mkdir() && Constants.logFolder.mkdir() && Constants.analyticsFolder.mkdir())) {
                System.out.println("FAILED MAKING PROPER DIRECTORIES.");
            }
            System.out.println("Created missing files.");
        }
        List<File> list;
        if(Constants.directoryFolder.listFiles() == null) {
            list = Arrays.asList(new File[0]);
            log.debug("Array returned null. Setting contents of zero.");
        }else{
            list = Arrays.asList((Constants.directoryFolder.listFiles()));
        }
        Constants.directoryFiles = new ArrayList<>();
        Constants.directoryFiles.addAll(list);
        setupLogger();
    }

    public static void setupLogger(){
        System.out.println("Seaching for log file");
        Path path = Paths.get(Constants.logFolder.toString(), "Log_File.txt");
        Constants.logFile = new File(path.toString());
        if(!Constants.logFile.exists()){
            try {
                if(Constants.logFile.createNewFile()) {
                    System.out.println("Created log file");
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Log file failed creating");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Log file found");
        }
        LogManager.readFileIntoContents();
    }


    private static String getFilePath(){
        String FileFolder = System.getenv("APPDATA") + "\\" + "FabLabAnalytics";
        System.out.println("Searching for system");
        String os = System.getProperty("os.name").toUpperCase();
        if (os.contains("WIN")) {
            FileFolder = System.getenv("APPDATA") + "\\" + "FabLabAnalytics";
            OSDirectoryPath = ("\\directory");
            OSLogPath = ("\\log");
            OSAnalyticsPath = "\\analytics";
            System.out.println("Found windows");
        }
        if (os.contains("MAC")) {
            FileFolder = System.getProperty("user.home") + "/Library/Application Support" + "/FabLabAnalytics";
            OSDirectoryPath = ("/directory");
            OSLogPath = ("/log");
            OSAnalyticsPath = "/analytics";
            System.out.println("Found mac");
        }
        if (os.contains("NUX")) {
            FileFolder = System.getProperty("user.dir") + ".FabLabAnalytics";
            OSDirectoryPath = (".directory");
            OSLogPath = (".log");
            OSAnalyticsPath = ".analytics";
            System.out.println("Found linux");
        }
        System.out.println(FileFolder);
        return FileFolder;
    }

    public static void saveDirectoryJsonFile(Person person) {
        Gson gson = FxGson.create();
        Path path = Paths.get(Constants.directoryFolder.toString(), person.getName().replace(" ", "_")+person.getId()+".json");
        deleteFile(path);
        try {
            FileUtils.writeStringToFile(path.toFile(), gson.toJson(person), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File[] getAllFilesAtPath(File path){
        return path.listFiles();
    }

    public static boolean deleteFile(Path path){
        try{
            if(path != null && path.toFile().exists()){
                Files.delete(path);
            }else{
                System.out.println("File does not exist");
                System.out.println(path);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    public static boolean deleteDirectoryFile(Person selectedPerson) {
        Path path = Paths.get(Constants.directoryFolder.toString(), selectedPerson.getName().replace(" ", "_")+selectedPerson.getId()+ ".json");
        return deleteFile(path);
    }
    public static void getDirectoryAsCSV(){
        String CSVContents = "Card Input, ID, Name, Email, Certifications, Strikes, Notes, Visit Count" + "\n";
        for(Person p : Constants.rawDirectoryData){
            String row = p.getCardNumber() + "," + p.getId() + "," +p.getName() + "," + p.getEmail() + "," + p.getCertifications() + "," + p.getStrikes() + "," + p.getNotes() + "," + p.getTimesVisited() + "\n";
            CSVContents += row;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM-dd-yyyy");
        Path path = Paths.get(Constants.analyticsFolder.toString(), "Directory" + dateTimeFormatter.print(DateTime.now()) + ".csv");
        try {
            FileUtils.writeStringToFile(path.toFile(), CSVContents, Charset.defaultCharset());
            Desktop.getDesktop().open(Constants.analyticsFolder);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed creating file." + e.getMessage());
            alert.showAndWait();
        }
    }
}