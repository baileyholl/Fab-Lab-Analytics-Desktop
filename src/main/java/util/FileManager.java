package util;

import com.google.gson.Gson;
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

public class FileManager {

    private static String OSDirectoryPath;
    private static String OSTextPath;

    public static void setupFolders() {
        System.out.println("Searching for resource folder");
        Constants.mainFolder = new File(getFilePath());
        Constants.directoryFolder = new File(Constants.mainFolder.toString() + OSDirectoryPath);
        if (!(Constants.mainFolder.exists() && Constants.directoryFolder.exists())) {
            if (!(Constants.mainFolder.mkdir() && Constants.directoryFolder.mkdir())) {
                System.out.println("FAILED MAKING PROPER DIRECTORIES.");
                System.exit(0);
            }
            System.out.println("Created missing files.");
        }
        List<File> list= Arrays.asList((FileManager.getAllFilesAtPath(Constants.directoryFolder)));
        Constants.directoryFiles = new ArrayList<>();
        Constants.directoryFiles.addAll(list);
    }


    private static String getFilePath(){
        String FileFolder = System.getenv("APPDATA") + "\\" + "FabLabAnalytics";
        System.out.println("Searching for system");
        String os = System.getProperty("os.name").toUpperCase();
        if (os.contains("WIN")) {
            FileFolder = System.getenv("APPDATA") + "\\" + "FabLabAnalytics";
            OSDirectoryPath = ("\\directory");
            System.out.println("Found windows");
        }
        if (os.contains("MAC")) {
            FileFolder = System.getProperty("user.home") + "/Library/Application Support" + "/FabLabAnalytics";
            OSDirectoryPath = ("/directory");
            System.out.println("Found mac");
        }
        if (os.contains("NUX")) {
            FileFolder = System.getProperty("user.dir") + ".FabLabAnalytics";
            OSDirectoryPath = (".directory");
            System.out.println("Found linux");
        }
        System.out.println(FileFolder);
        return FileFolder;
    }
    @Deprecated
    public static boolean createNewDirectoryFile(Person person){
        List<String> lines = Arrays.asList(person.getCardNumber(), person.getName(), person.getId(), person.getEmail(), person.getCertifications(), person.getNotes(), person.getTimestamp());
        Path path = Paths.get(Constants.directoryFolder.toString(), person.getName().replace(" ", "_")+person.getId()+".txt");
        try{
            Files.write(path, lines, Charset.forName("UTF-8"));
        }catch (IOException e){
            System.out.println(e.toString());
            return false;
        }
        return  true;
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