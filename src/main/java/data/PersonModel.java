package data;

import com.google.gson.Gson;
import javafx.collections.ObservableSet;
import org.hildan.fxgson.FxGson;
import util.FileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;

public class PersonModel{
    
    private PersonMap personMap;

    public PersonModel(){
        personMap = new PersonMap();
    }
    
    public Person add(Person p){
        return this.personMap.put(p);
    }

    public Person get(String key){
        return this.personMap.get(key);
    }
    
    public Person remove(Person p){
        return this.personMap.remove(p);
    }

    /**
     * Loads person objects from files into their Gson model and adds them to the map.
     * @param files Collection of files to add.
     */
    public void loadFromFiles(Collection<File> files){
        for(File f : files){
            if(!f.isHidden() && f.exists()){
                try(BufferedReader br = new BufferedReader(new FileReader(f))){
                    Gson gson = FxGson.create();
                    Person person = gson.fromJson(br, Person.class);
                    FileManager.validateUpToDateJson(person);
                    personMap.put(person);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
}
