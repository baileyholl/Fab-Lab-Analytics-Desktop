package main;

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
        loadFilesIntoDirectory(directoryFiles);
        System.out.println("Put: " + directory.size() + " elements into directory");
    }

    public void loadFilesIntoDirectory(ArrayList<File> directoryFiles){
        for(File f : directoryFiles){
            if(!f.isHidden() && f.exists()){
                try(BufferedReader br = new BufferedReader(new FileReader(f))){
                    String line;
                    ArrayList<String> lineList = new ArrayList<String>();
                    while((line = br.readLine())!= null){
                        lineList.add(line);
                    }
                    directory.put(lineList.get(0), new Person(lineList.get(0), lineList.get(2), lineList.get(1), lineList.get(3), lineList.get(4), lineList.get(5)));
                }catch (Exception e){
                    System.out.println(e.getStackTrace());
                }
            }
        }
    }
    public Collection<Person> getAllPersons(){
        return directory.values();
    }
}
