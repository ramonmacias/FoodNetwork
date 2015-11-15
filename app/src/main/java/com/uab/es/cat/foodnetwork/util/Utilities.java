package com.uab.es.cat.foodnetwork.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ramonmacias on 28/10/15.
 */
public class Utilities {

    private static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public static String getFullAddress(String streetName, String buildingNumber, String floor, String door){
        String fullAddress = "";
        if(streetName != null && !"".equals(streetName)){
            fullAddress += "C/" + streetName;
        }
        if(buildingNumber != null && !"".equals(buildingNumber)){
            fullAddress += " , nº " + buildingNumber;
        }
        if(floor != null && !"".equals(floor)){
            fullAddress += " " + floor;
        }
        if(door != null && !"".equals(door)){
            fullAddress += " - " + door;
        }
        return fullAddress;
    }

    public static String dateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
        return dateFormat.format(date);
    }
}
