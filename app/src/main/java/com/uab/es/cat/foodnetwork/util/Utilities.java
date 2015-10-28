package com.uab.es.cat.foodnetwork.util;

/**
 * Created by ramonmacias on 28/10/15.
 */
public class Utilities {

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
}
