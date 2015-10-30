package com.uab.es.cat.foodnetwork.dto;

/**
 * Created by ramonmacias on 18/10/15.
 */
public class UserDTO extends BaseDTO{

    public long idUser;
    public String userName;
    public String mail;
    public String name;
    public String lastName;
    public long idLocation;
    public String password;
    public String idTypeUser;

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


}
