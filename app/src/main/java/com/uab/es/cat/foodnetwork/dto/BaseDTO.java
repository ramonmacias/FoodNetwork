package com.uab.es.cat.foodnetwork.dto;

import java.io.Serializable;

/**
 * Created by ramonmacias on 18/10/15.
 */
public class BaseDTO implements Serializable{

    public String insertDate;
    public String lastUpdate;
    public String updateUser;

    public BaseDTO(){

    }


    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }


}
