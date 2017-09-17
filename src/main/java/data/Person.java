package data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.joda.time.DateTime;
import util.FileManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class Person{

    private StringProperty cardNumber;
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty email;
    /*Lab Certification*/
    private SimpleStringProperty certifications;
    private SimpleStringProperty shopCertification;
    private SimpleStringProperty notes;
    private transient SimpleStringProperty timestamp;
    private SimpleStringProperty timesVisited;
    private SimpleStringProperty strikes;
    private ArrayList<Timestamp> timeStampHistory;

    public Person(String cardNumber, String ID, String name, String email, String certifications, String shopCertification, String notes, String strikes){
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.id = new SimpleStringProperty(ID);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.certifications = new SimpleStringProperty(certifications);
        this.shopCertification  = new SimpleStringProperty(shopCertification);
        this.notes = new SimpleStringProperty(notes);
        this.timestamp = new SimpleStringProperty(Timestamp.getCurrentTime());
        this.timesVisited = new SimpleStringProperty("0");
        this.strikes = new SimpleStringProperty(strikes);
        this.timeStampHistory = new ArrayList<>();
    }

    public void incrementTimesVisited(){
        timesVisited = new SimpleStringProperty(String.valueOf(Integer.valueOf(getTimesVisited()) + 1));
        FileManager.saveDirectoryJsonFile(this);
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

    public StringProperty cardNumberProperty() {
        return cardNumber;
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public SimpleStringProperty timestampProperty() {
        return timestamp;
    }
    public void setTimestampProperty(SimpleStringProperty simpleStringProperty){
        this.timestamp = simpleStringProperty;
    }
    public void setTimestampProperty(String string){
        this.timestamp = new SimpleStringProperty(string);
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

    public void setTimesVisitedProperty(String string){
        this.timesVisited = new SimpleStringProperty(string);
    }

    public void setStrikesProperty(String string){
        this.strikes = new SimpleStringProperty(string);
    }

    public String getStrikes() {
        return strikes.get();
    }

    public SimpleStringProperty strikesProperty() {
        return strikes;
    }

    public String getShopCertification() {
        return shopCertification.get();
    }

    public SimpleStringProperty shopCertificationProperty() {
        return shopCertification;
    }

    public void setShopCertification(String shopCertification) {
        if(this.shopCertification == null){
            this.shopCertification = new SimpleStringProperty();
        }
        this.shopCertification.set(shopCertification);
    }

    public ArrayList<Timestamp> getTimeStampHistory() {
        return timeStampHistory;
    }

    public void setTimeStampHistory(ArrayList<Timestamp> timeStampHistory) {
        this.timeStampHistory = timeStampHistory;
    }

    @Override
    public String toString() {
        return getId() + getStrikes() + getTimesVisited() + getName() + getCardNumber() + getCertifications() + getEmail() + getShopCertification() + getTimeStampHistory();
    }

    public void set(Person p) {
        this.cardNumber = p.cardNumber;
        this.id = p.id;
        this.name = p.name;
        this.email = p.email;
        this.certifications = p.certifications;
        this.shopCertification  = p.shopCertification;
        this.notes = p.notes;
        this.timestamp = p.timestamp;
        this.timesVisited = p.timesVisited;
        this.strikes = p.strikes;
        this.timeStampHistory = p.timeStampHistory;
    }

    @Override
    public int hashCode() {
        int result = cardNumber != null ? cardNumber.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (certifications != null ? certifications.hashCode() : 0);
        result = 31 * result + (shopCertification != null ? shopCertification.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (timesVisited != null ? timesVisited.hashCode() : 0);
        result = 31 * result + (strikes != null ? strikes.hashCode() : 0);
        result = 31 * result + (timeStampHistory != null ? timeStampHistory.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (getCardNumber() != null ? !getCardNumber().equals(person.getCardNumber()) : person.getCardNumber() != null)
            return false;
        if (getId() != null ? !getId().equals(person.getId()) : person.getId() != null) return false;
        if (getName() != null ? !getName().equals(person.getName()) : person.getName() != null) return false;
        if (getEmail() != null ? !getEmail().equals(person.getEmail()) : person.getEmail() != null) return false;
        if (getCertifications() != null ? !getCertifications().equals(person.getCertifications()) : person.getCertifications() != null)
            return false;
        if (getShopCertification() != null ? !getShopCertification().equals(person.getShopCertification()) : person.getShopCertification() != null)
            return false;
        if (getNotes() != null ? !getNotes().equals(person.getNotes()) : person.getNotes() != null) return false;
        if (getTimestamp() != null ? !getTimestamp().equals(person.getTimestamp()) : person.getTimestamp() != null)
            return false;
        if (getTimesVisited() != null ? !getTimesVisited().equals(person.getTimesVisited()) : person.getTimesVisited() != null)
            return false;
        if (getStrikes() != null ? !getStrikes().equals(person.getStrikes()) : person.getStrikes() != null)
            return false;
        return getTimeStampHistory() != null ? getTimeStampHistory().equals(person.getTimeStampHistory()) : person.getTimeStampHistory() == null;
    }
}
