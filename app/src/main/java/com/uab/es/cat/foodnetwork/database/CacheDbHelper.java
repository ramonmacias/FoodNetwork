package com.uab.es.cat.foodnetwork.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.uab.es.cat.foodnetwork.dto.BaseDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;

/**
 * Created by ramonmacias on 18/10/15.
 */
public class CacheDbHelper implements DatabaseHandler{

    public CacheDbHelper(){

    }

    @Override
    public void insert(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper) {

        if(baseDTO instanceof UserDTO){
            UserDTO userDTO = (UserDTO) baseDTO;

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_ID, userDTO.getIdUser());
            values.put(UserContract.UserEntry.COLUMN_NAME_NAME, userDTO.getName());
            values.put(UserContract.UserEntry.COLUMN_NAME_LAST_NAME, userDTO.getLastName());
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_NAME, userDTO.getUserName());
            values.put(UserContract.UserEntry.COLUMN_NAME_MAIL, userDTO.getMail());
            values.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, userDTO.getPassword());
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_TYPE, userDTO.getIdTypeUser());

            long newRowId;
            newRowId = db.insert(
                    UserContract.UserEntry.TABLE_NAME,
                    null,
                    values);
        }
    }

    @Override
    public void update(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper) {

    }

    @Override
    public void delete(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper) {

    }
}
