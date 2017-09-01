package data;

import com.google.gson.Gson;
import org.hildan.fxgson.FxGson;

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
        loadJsonsIntoDirectory(directoryFiles);
        System.out.println("Put: " + directory.size() + " elements into directory");
    }

    public void loadJsonsIntoDirectory(ArrayList<File> directoryFiles){
        for(File f : directoryFiles){
            if(!f.isHidden() && f.exists()){
                try(BufferedReader br = new BufferedReader(new FileReader(f))){
                    Gson gson = FxGson.create();
                    //convert the json string back to object
                    Person person = gson.fromJson(br, Person.class);
                    validateUpToDateJson(person);
                    directory.put(person.getCardNumber(), person);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Used to bring older versions of the json database up to date.
     * @param person Person object to be validated
     */
    public static void validateUpToDateJson(Person person){
        if(person != null) {
            if ((person.strikesProperty() == null || person.getStrikes() == null))
                person.setStrikesProperty("0");
            if(person.timesVisitedProperty() == null || person.getTimesVisited() == null){
                person.setTimesVisitedProperty("0");
            }
        }
    }
    public Person put(String string , Person p){
        return directory.put(string, p);
    }
    public Collection<Person> getAllPersons(){
        return directory.values();
    }
    public Person remove(Object key){
        return directory.remove(key);
    }
}
