package data;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 *
 */
public class PersonMap {
    /*Stores a map of people where the key is the Card Number and the value is the person object.*/
    private LinkedHashMap<String, Person> personLinkedHashMap;

    public PersonMap(){
        personLinkedHashMap = new LinkedHashMap<>();
    }

    /**
     * Instantiates the map with all values in the collection.
     * @param collection Collection of person objects.
     */
    public PersonMap(Collection<Person> collection){
        this();
        for(Person p : collection){
            this.personLinkedHashMap.put(p.getCardNumber(), p);
        }
    }

    public boolean containsValue(Person person){
        return this.personLinkedHashMap.containsValue(person);
    }

    public boolean containsKey(String string){
        return this.personLinkedHashMap.containsKey(string);
    }

    public Person put(Person person){
        return this.personLinkedHashMap.put(person.getCardNumber(), person);
    }

    public Person remove(Person person){
        return this.personLinkedHashMap.remove(person.getCardNumber());
    }
}
