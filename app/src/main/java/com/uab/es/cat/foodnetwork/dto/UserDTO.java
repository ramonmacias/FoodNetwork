package com.uab.es.cat.foodnetwork.dto;

/**
 * Created by ramonmacias on 18/10/15.
 */
public class UserDTO extends BaseDTO{

    private long idUser;
    private String userName;
    private String mail;
    private String name;
    private String lastName;
    private long idLocation;
    private String password;
    private String idTypeUser;
    private String typeOfVehicle;
    private int actionRadio;
    private String initialHour;
    private String finalHour;
    private long phoneNumber;

    public UserDTO(){

    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(long idLocation) {
        this.idLocation = idLocation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdTypeUser() {
        return idTypeUser;
    }

    public void setIdTypeUser(String idTypeUser) {
        this.idTypeUser = idTypeUser;
    }

    public String getTypeOfVehicle() {
        return typeOfVehicle;
    }

    public void setTypeOfVehicle(String typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
    }

    public int getActionRadio() {
        return actionRadio;
    }

    public void setActionRadio(int actionRadio) {
        this.actionRadio = actionRadio;
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

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



}
