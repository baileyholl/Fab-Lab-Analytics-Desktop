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
    private SimpleStringProperty signedWaiver;
    private transient SimpleStringProperty timestamp;
    private SimpleStringProperty timesVisited;
    private SimpleStringProperty strikes;
    private ArrayList<Timestamp> timeStampHistory;

    public Person(String cardNumber, String ID, String name, String email, String certifications, String shopCertification, String notes, String strikes, String signedWaiver){
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
        this.signedWaiver = new SimpleStringProperty(signedWaiver);
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
        if(shopCertification == null){
            setShopCertification("");
        }
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

    public String getSignedWaiver() {
        if(signedWaiver == null){
            setSignedWaiver("No");
        }
        return signedWaiver.get();
    }

    public SimpleStringProperty signedWaiverProperty() {
        return signedWaiver;
    }

    public void setSignedWaiver(String signedWaiver) {
        if(this.signedWaiver == null){
            this.signedWaiver = new SimpleStringProperty();
        }
        this.signedWaiver.set(signedWaiver);
    }

    @Override
    public String toString() {
        return getId() + getStrikes() + getTimesVisited() + getName() + getCardNumber() + getCertifications() + getEmail() + getShopCertification() + getTimeStampHistory() + getSignedWaiver();
    }

    /**
     * Sets the data of this object to the data from the given person.
     * @param p Set of data to set this person object to.
     */
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
        this.signedWaiver = p.signedWaiver;
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
        result = 31 * result + (signedWaiver != null ? signedWaiver.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (cardNumber != null ? !cardNumber.equals(person.cardNumber) : person.cardNumber != null) return false;
        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (email != null ? !email.equals(person.email) : person.email != null) return false;
        if (certifications != null ? !certifications.equals(person.certifications) : person.certifications != null)
            return false;
        if (shopCertification != null ? !shopCertification.equals(person.shopCertification) : person.shopCertification != null)
            return false;
        if (notes != null ? !notes.equals(person.notes) : person.notes != null) return false;
        if (signedWaiver != null ? !signedWaiver.equals(person.signedWaiver) : person.signedWaiver != null)
            return false;
        if (timestamp != null ? !timestamp.equals(person.timestamp) : person.timestamp != null) return false;
        if (timesVisited != null ? !timesVisited.equals(person.timesVisited) : person.timesVisited != null)
            return false;
        if (strikes != null ? !strikes.equals(person.strikes) : person.strikes != null) return false;
        return timeStampHistory != null ? timeStampHistory.equals(person.timeStampHistory) : person.timeStampHistory == null;
    }
}
