package data;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Constants {
    public static File mainFolder;
    public static File directoryFolder;
    public static File logFolder;
    public static ArrayList<File> directoryFiles;
    public static File logFile;
    public static Directory directory;
    public static String logContents;
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM-dd-yyyy hh:mm a");

}
