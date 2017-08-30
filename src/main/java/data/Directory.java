package data;

import com.google.gson.Gson;
import util.FileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class Directory {
    private LinkedHashMap<String, Person> directory;

    public Directory(){
        directory = new LinkedHashMap<>();
    }
    public Directory(ArrayList<File> directoryFiles){
        this();
        //loadFilesIntoDirectory(directoryFiles);
        loadJsonsIntoDirectory(directoryFiles);
        System.out.println("Put: " + directory.size() + " elements into directory");
    }
    @Deprecated
    public void loadFilesIntoDirectory(ArrayList<File> directoryFiles){
        for(File f : directoryFiles){
            if(!f.isHidden() && f.exists()){
                try(BufferedReader br = new BufferedReader(new FileReader(f))){
                    String line;
                    ArrayList<String> lineList = new ArrayList<>();
                    while((line = br.readLine())!= null){
                        lineList.add(line);
                    }
                    directory.put(lineList.get(0), new Person(lineList.get(0), lineList.get(2), lineList.get(1), lineList.get(3), lineList.get(4), lineList.get(5)));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadJsonsIntoDirectory(ArrayList<File> directoryFiles){
        convertTextToJson(directoryFiles);
        for(File f : directoryFiles){
            if(!f.isHidden() && f.exists()){
                try(BufferedReader br = new BufferedReader(new FileReader(f))){
                    Gson gson = new Gson();
                    //convert the json string back to object
                    Person person = gson.fromJson(br, Person.class);
                    directory.put(person.getCardNumber(), person);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Converts old text files into json files
     */
    public void convertTextToJson(ArrayList<File> directoryFiles){
        //Create jsons out of every text file
        for(File f : directoryFiles){
            if(!f.isHidden() && f.exists()){
                try(BufferedReader br = new BufferedReader(new FileReader(f))){
                    String line;
                    ArrayList<String> lineList = new ArrayList<>();
                    while((line = br.readLine())!= null){
                        lineList.add(line);
                    }
                    Person person = new Person(lineList.get(0), lineList.get(2), lineList.get(1), lineList.get(3), lineList.get(4), lineList.get(5));
                    FileManager.createDirectoryJsonFile(person);
                }catch (IndexOutOfBoundsException ignored){
                    //Attempting to read depreacted text file system
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        //Delete text files
        for(File f : directoryFiles){
            if(!f.isHidden() && f.exists()){
                if(f.getPath().contains(".txt")) {
                    FileManager.deleteFile(f.toPath());
                }
            }
        }
    }

    public Collection<Person> getAllPersons(){
        return directory.values();
    }
    public Person remove(Object key){
        return directory.remove(key);
    }
}
