package com.uab.es.cat.foodnetwork.dto;

/**
 * Created by ramonmacias on 18/10/15.
 */
public class LiteralDTO extends BaseDTO {

    public int idLiteral;
    public String description;

    public LiteralDTO(){

    }

    public int getIdLiteral() {
        return idLiteral;
    }

    public void setIdLiteral(int idLiteral) {
        this.idLiteral = idLiteral;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
