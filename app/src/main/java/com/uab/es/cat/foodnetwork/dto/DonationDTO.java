package com.uab.es.cat.foodnetwork.dto;

/**
 * Created by ramonmacias on 18/10/15.
 */
public class DonationDTO extends BaseDTO {



    public long idDonation;
    public long idUser;
    public long idFood;
    public long idLocation;
    public int state;

    public DonationDTO(){

    }

    public long getIdDonation() {
        return idDonation;
    }

    public void setIdDonation(long idDonation) {
        this.idDonation = idDonation;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdFood() {
        return idFood;
    }

    public void setIdFood(long idFood) {
        this.idFood = idFood;
    }

    public long getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(long idLocation) {
        this.idLocation = idLocation;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
