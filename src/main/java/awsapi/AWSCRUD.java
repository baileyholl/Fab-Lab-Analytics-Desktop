package awsapi;


import com.amazonaws.services.dynamodbv2.model.*;
import data.Person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;

public class AWSCRUD {
    private String tableName = "Ihub_Visitors";



    public boolean createVisitor(Person person){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);




        Table table = dynamoDB.getTable(tableName);

        String hashedID = person.getHashedID();

        final Map<String, Object> certMap;
        final Map<String, Object> timeMap;



        int year = 2015;
        String title = "The Big New Movie";

        final Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("plot", "Nothing happens at all.");
        infoMap.put("rating", 0);

        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                    .putItem(new Item()
                            .withPrimaryKey("id", person.getHashedID(), "name", person.getName())
                            .with("email",person.getEmail())
                            .withList("certifications", person.getCertifications())
                            .with("notes", person.getNotes())
                            .with("timesVisited", person.getTimesVisited())
                            .with("strikes", person.getStrikes())
                            .with("signedWaiver", person.getSignedWaiver())
                            .withList("timestamps", person.getTimeStampHistory())
                    );

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

        }
        catch (Exception e) {
            System.err.println("Unable to add item: " + year + " " + title);
            System.err.println(e.getMessage());
        }

        return false;
    }

    public boolean updateVisitor(Person person){
        return false;
    }

    public Person getVisitorFromID(String id){
        return null;
    }

    public boolean deleteVisitorWithID(String id) {
        return false;

    }


}
