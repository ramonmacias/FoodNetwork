package com.uab.es.cat.foodnetwork.database;

import com.uab.es.cat.foodnetwork.dto.BaseDTO;

/**
 * Created by ramonmacias on 18/10/15.
 */
public interface DatabaseHandler {

    public void insert(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper);

    public void update(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper);

    public void delete(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper);
}
