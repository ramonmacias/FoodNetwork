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

    public static String getGoogleAddress(String streetName, String buildingNumber, String city){
        String googleAddress = "";
        if(streetName != null && !"".equals(streetName)){
            googleAddress += "C/" + streetName;
        }
        if(buildingNumber != null && !"".equals(buildingNumber)){
            googleAddress += " , " + buildingNumber;
        }
        if(city != null && !"".equals(city)){
            googleAddress += " , " + city;
        }
        return googleAddress;
    }

    public static String dateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
        return dateFormat.format(date);
    }

    /**
     * Calculate de distance between two points using the Haversine formula
     * @param initialCoordenateX
     * @param initialCoordenateY
     * @param finalCoordenateX
     * @param finalCoordenateY
     * @return
     */
    public static double calculateDistanceBetweenTwoPoints(double initialCoordenateX, double initialCoordenateY, double finalCoordenateX, double finalCoordenateY){

        double earthRadius = 6371.0; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(finalCoordenateY-initialCoordenateY);
        double dLng = Math.toRadians(finalCoordenateX-initialCoordenateX);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(initialCoordenateY)) * Math.cos(Math.toRadians(finalCoordenateY));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;



    }
}
