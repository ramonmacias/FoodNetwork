package com.uab.es.cat.foodnetwork.dto;

/**
 * Created by ramonmacias on 13/12/15.
 */
public class RankingDTO extends BaseDTO {

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

    public String getNumberOfDonationsCompleted() {
        return numberOfDonationsCompleted;
    }

    public void setNumberOfDonationsCompleted(String numberOfDonationsCompleted) {
        this.numberOfDonationsCompleted = numberOfDonationsCompleted;
    }

    private String neighborhood;
    private String district;
    private String numberOfDonationsCompleted;

    public RankingDTO(){

    }
}
