package com.uab.es.cat.foodnetwork.dto;

/**
 * Created by ramonmacias on 18/10/15.
 */
public class LocationDTO extends BaseDTO {

    private long idLocation;
    private String streetName;
    private String buildingNumber;
    private String floor;
    private String door;
    private String city;
    private String neighborhood;
    private String district;
    private String latitude;
    private String longitude;
    private String completeAdrressFromGeocoder;


    public LocationDTO(){

    }

    public long getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(long idLocation) {
        this.idLocation = idLocation;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public String getCompleteAdrressFromGeocoder() {
        return completeAdrressFromGeocoder;
    }

    public void setCompleteAdrressFromGeocoder(String completeAdrressFromGeocoder) {
        this.completeAdrressFromGeocoder = completeAdrressFromGeocoder;
    }

}
