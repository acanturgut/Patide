package com.wrexsoft.canturgut.patide;

import java.util.Date;

/**
 * Created by canta on 5/24/2017.
 */

public class ApplicationCalculations {
 
    public static Date getTime(String date){
        String[] splited = date.split("\\s+");
        String s1 = splited[0];
        String[] dateValues = s1.split("/");
        String s2 = splited[1];
        String[] timeValues = s2.split(":");
        Date mydate = new Date();
        mydate.setYear( Integer.parseInt(dateValues[2]));
        mydate.setMonth(Integer.parseInt(dateValues[1]));
        mydate.setDate(Integer.parseInt(dateValues[0]));
        mydate.setHours(Integer.parseInt(timeValues[0]));
        mydate.setMinutes(Integer.parseInt(timeValues[1]));
        return mydate;
    }



}
