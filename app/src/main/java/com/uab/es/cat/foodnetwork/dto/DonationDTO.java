package com.uab.es.cat.foodnetwork.dto;

/**
 * Created by ramonmacias on 18/10/15.
 */
public class DonationDTO extends BaseDTO {



    public long idDonation;
    public long idUser;
    public long idLocation;
    public int state;
    public String initialHour;
    public String finalHour;

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

    public String getInitialHour() {
        return initialHour;
    }

    public void setInitialHour(String initialHour) {
        this.initialHour = initialHour;
    }

    public String getFinalHour() {
        return finalHour;
    }

    public void setFinalHour(String finalHour) {
        this.finalHour = finalHour;
    }

}
