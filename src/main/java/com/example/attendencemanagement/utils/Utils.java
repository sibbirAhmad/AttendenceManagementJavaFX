package com.example.attendencemanagement.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class Utils {

    public static String ConvertSecondToHHMMSSString(int nSecondTime) {
        return LocalTime.MIN.plusSeconds(nSecondTime).toString();
    }
    public static void saveAsText(String fileName,String text){
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
   public static String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String s = dateFormat.format(date);
        return s;
    }
    public static String getFormatted(String date){
        return "p"+getDate().replace("-","");
    }
    public static String getFormatted(){
        return "p"+getDate().replace("-","");
    }
}
