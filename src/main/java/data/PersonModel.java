package data;

import com.google.gson.Gson;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.hildan.fxgson.FxGson;
import util.FileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;

public class PersonModel {
    
    private PersonMap personMap;
    /*An observable version of the values in personMap. Changes in the personMap are reflected in this list and vice-versa.*/
    private ObservableList<Person> observablePersonList;

    public PersonModel(){
        personMap = new PersonMap();
        observablePersonList = FXCollections.observableList( personMap.toList(), param -> new Observable[]{
                param.idProperty(),
                param.nameProperty(),
                param.shopCertificationProperty(),
                param.strikesProperty(),
                param.timesVisitedProperty(),
                param.certificationsProperty(),
                param.cardNumberProperty(),
                param.emailProperty(),
                param.notesProperty()
        });
        observablePersonList.addListener((ListChangeListener<Person>) c -> System.out.println("List changed"));
        observablePersonList.addAll(personMap.toList());

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

    public Boolean contains(Person p){
        return this.personMap.containsValue(p);
    }

    public Collection<Person> toCollection(){
        return personMap.toList();
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
                    add(person);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public ObservableList<Person> getObservableList(){
        return observablePersonList;
    }
}
