package main;

import javafx.beans.property.SimpleStringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Person {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private final SimpleStringProperty cardNumber;
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty email;
    private final SimpleStringProperty certifications;
    private final SimpleStringProperty notes;
    private final SimpleStringProperty timestamp;

    public Person(String cardNumber, String ID, String name, String email, String certifications, String notes){
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.id = new SimpleStringProperty(ID);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.certifications = new SimpleStringProperty(certifications);
        this.notes = new SimpleStringProperty(notes);
        this.timestamp = new SimpleStringProperty(dateFormat.format(new Date()));
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
}
