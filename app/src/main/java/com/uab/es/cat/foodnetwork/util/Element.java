package com.uab.es.cat.foodnetwork.util;

/**
 * Created by ramonmacias on 22/12/15.
 */
public class Element {

    private double longitude;
    private int weight;
    private long idDonation;

    public Element(double longitude, int weight, long idDonation){
        this.longitude = longitude;
        this.weight = weight;
        this.idDonation = idDonation;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getIdDonation() {
        return idDonation;
    }

    public void setIdDonation(long idDonation) {
        this.idDonation = idDonation;
    }
}
