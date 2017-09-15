package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.*;

/**
 * Stores a set of person objects in a map stored by key value of the card input. Used to
 * manage seperate sets in directory and current checked in list in the UI.
 */
public class PersonMap {
    /*Stores a map of people where the key is the Card Number and the value is the person object.*/

    private ObservableMap<String, Person> observableMap;
    private ArrayList<Person> mapKeys;

    public PersonMap(){
        observableMap = FXCollections.observableHashMap();
        mapKeys = new ArrayList<>(observableMap.values());
    }

    /**
     * Instantiates the map with all values in the collection.
     * @param collection Collection of person objects.
     */
    public PersonMap(Collection<Person> collection){
        this();
        for(Person p : collection){
            this.observableMap.put(p.getCardNumber(), p);
        }
    }

    public boolean containsValue(Person person){
        return this.observableMap.containsValue(person);
    }

    public boolean containsKey(String string){
        return this.observableMap.containsKey(string);
    }

    public Person get(String key){
        return this.observableMap.get(key);
    }

    public Person put(Person person){
        return this.observableMap.put(person.getCardNumber(), person);
    }

    public Person remove(Person person){
        return this.observableMap.remove(person.getCardNumber());
    }

    /**
     * @return Returns a list of person objects from the hashmap.
     */
    public List<Person> toList(){
        return mapKeys;
    }
}
