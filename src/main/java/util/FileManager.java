package util;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import data.Constants;
import data.Person;

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

    public static void setupFolders() {
        System.out.println("Searching for resource folder");
        Constants.mainFolder = new File(getFilePath());
        Constants.directoryFolder = new File(Constants.mainFolder.toString() + OSDirectoryPath);
        Constants.logFolder = new File(Constants.mainFolder.toString() + OSLogPath);
        if (!(Constants.mainFolder.exists() && Constants.directoryFolder.exists() && Constants.logFolder.exists())) {
            if (!(Constants.mainFolder.mkdir() && Constants.directoryFolder.mkdir() && Constants.logFolder.mkdir())) {
                System.out.println("FAILED MAKING PROPER DIRECTORIES.");
            }
            System.out.println("Created missing files.");
        }
        List<File> list= Arrays.asList((FileManager.getAllFilesAtPath(Constants.directoryFolder)));
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
                if(Constants.logFile.createNewFile())
                    System.out.println("Created log file");
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
            System.out.println("Found windows");
        }
        if (os.contains("MAC")) {
            FileFolder = System.getProperty("user.home") + "/Library/Application Support" + "/FabLabAnalytics";
            OSDirectoryPath = ("/directory");
            OSLogPath = ("/log");
            System.out.println("Found mac");
        }
        if (os.contains("NUX")) {
            FileFolder = System.getProperty("user.dir") + ".FabLabAnalytics";
            OSDirectoryPath = (".directory");
            OSLogPath = (".log");
            System.out.println("Found linux");
        }
        System.out.println(FileFolder);
        return FileFolder;
    }

    public static void createDirectoryJsonFile(Person person) {
        Gson gson = new Gson();
        Path path = Paths.get(Constants.directoryFolder.toString(), person.getName().replace(" ", "_")+person.getId()+".json");
        deleteFile(path);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))){
            writer.write(gson.toJson(person));
            writer.flush();
            writer.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static File[] getAllFilesAtPath(File path){
        File[] allFiles = path.listFiles();
        if(allFiles == null){
            allFiles = new File[0];
            System.out.println("File array returned null. Returning array of no contents.");
        }
        return allFiles;
    }

    public static boolean deleteFile(Path path){
        try{
            if(path != null && path.toFile().exists()){
                Files.delete(path);
            }else{
                System.out.println("File does not exist");
                System.out.println(path);
            }
            return true;
        } catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static boolean deleteDirectoryFile(Person selectedPerson, String extension) {
        Path path = Paths.get(Constants.directoryFolder.toString(), selectedPerson.getName().replace(" ", "_")+selectedPerson.getId()+ extension);
        try{
            if(path != null) Files.delete(path);
        } catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }
}