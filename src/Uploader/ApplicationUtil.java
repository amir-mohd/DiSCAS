package Uploader;

import java.util.Calendar;

public class ApplicationUtil {

    public static String getRemoteFolderName()
    {
        String folderName="";
        Calendar now=Calendar.getInstance();
        Integer year=now.get(Calendar.YEAR);
        Integer month=now.get(Calendar.MONTH)+1;
        Integer day=now.get(Calendar.DAY_OF_MONTH);
        Integer hour=now.get(Calendar.HOUR_OF_DAY);
        folderName=year.toString();
        if(month<10){
            folderName=folderName+"0"+month;
        }
        else{
            folderName=folderName+month;
        }
        if(day<10){
            folderName=folderName+"0"+day.toString();
        }
        else {
            folderName=folderName+day.toString();
        }
        if(hour<10){
            folderName=folderName+"0"+hour;
        }
        else {
            folderName=folderName+hour;
        }
        folderName=folderName+"00000";
        return folderName;
    }
}
