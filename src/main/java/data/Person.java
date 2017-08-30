package data;

import javafx.beans.property.SimpleStringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Person {
    private transient DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private SimpleStringProperty cardNumber;
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty email;
    private SimpleStringProperty certifications;
    private SimpleStringProperty notes;
    private SimpleStringProperty timestamp;
    private SimpleStringProperty timesVisited;

    public Person(String cardNumber, String ID, String name, String email, String certifications, String notes){
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.id = new SimpleStringProperty(ID);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.certifications = new SimpleStringProperty(certifications);
        this.notes = new SimpleStringProperty(notes);
        this.timestamp = new SimpleStringProperty(dateFormat.format(new Date()));
    }

    private void incrementTimesVisited(){
        timesVisited.set(String.valueOf(Integer.valueOf(getTimesVisited()) + 1));
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public String getCertifications() {
        return certifications.get();
    }

    public SimpleStringProperty certificationsProperty() {
        return certifications;
    }

    public String getNotes() {
        return notes.get();
    }

    public SimpleStringProperty notesProperty() {
        return notes;
    }


    public String getCardNumber() {
        return cardNumber.get();
    }

    public SimpleStringProperty cardNumberProperty() {
        return cardNumber;
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public SimpleStringProperty timestampProperty() {
        return timestamp;
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getTimesVisited() {
        return timesVisited.get();
    }

    public SimpleStringProperty timesVisitedProperty() {
        return timesVisited;
    }
}
