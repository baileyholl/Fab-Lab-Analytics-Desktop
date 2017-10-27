package util;

import data.Person;
import data.Timestamp;
import java.util.Collection;

/**
 * Provides a set of classes for specific data points like time spent, unique visitors, average time, etc.
 */
public class AnalyticUtil {

    public static final int DAYS_IN_MILLIS = 86400000; //86 million
    public static final int HOUR_IN_MILLIS = 3600000; //3.6 million
    public static final int MINUTE_IN_MILLIS = 60000; // 60 thousand
    /**
     * Fetches the total time spent in the lab from all persons in the directory. An O(P*T) operation that should be used sparingly.
     * @param collection Collection of person objects
     * @return Returns the total time spent from all visits in the collection of persons in milliseconds.
     */
    public static long getTotalTimeSpent(Collection<Person> collection){
        long millis = 0;
        for(Person p : collection){
             millis += getTotalTimeSpent(p);
        }
        return millis;
    }

    public static long getTotalTimeSpent(Person p){
        long millis = 0;
        for( Timestamp t : p.getTimeStampHistory()){
            millis += t.getTimeLength().getMillis();
        }
        return millis;
    }

    /**
     * Expensive operation, should only be used for collecting analytics on demand.
     * @param personCollection Collection of person objects
     * @return Returns a string formatted with the number of days, hours, and minutes.
     */
    public static String getTotalTimeSpentInText(Collection<Person> personCollection){
        int days, hours, minutes;
        long millis = getTotalTimeSpent(personCollection);
        days =(int) (millis / DAYS_IN_MILLIS);
        millis = millis % DAYS_IN_MILLIS;
        hours = (int)(millis / HOUR_IN_MILLIS);
        millis = millis % HOUR_IN_MILLIS;
        minutes = (int) (millis/MINUTE_IN_MILLIS);
        return String.format("Days: %1$d Hours: %2$d Minutes: %3$d", days, hours, minutes);
    }

    public static String getTotalTimeSpentInText(long millis){
        int days, hours, minutes;
        days =(int) (millis / DAYS_IN_MILLIS);
        millis = millis % DAYS_IN_MILLIS;
        hours = (int)(millis / HOUR_IN_MILLIS);
        millis = millis % HOUR_IN_MILLIS;
        minutes = (int) (millis/MINUTE_IN_MILLIS);
        return String.format("Days: %1$d Hours: %2$d Minutes: %3$d", days, hours, minutes);
    }

    public static String getAverageTimeSpent(Collection<Person> personCollection){
        long millis = 0;
        for(Person p : personCollection){
            millis += getTotalTimeSpent(p);
        }
        return getTotalTimeSpentInText(millis / personCollection.size());
    }
}
