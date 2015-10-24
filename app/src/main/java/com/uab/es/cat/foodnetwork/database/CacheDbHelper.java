package com.uab.es.cat.foodnetwork.database;

import android.content.ContentValues;
import android.database.Cursor;
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

    @Override
    public BaseDTO getById(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper) {
        if(baseDTO instanceof UserDTO){
            UserDTO userDTO = (UserDTO) baseDTO;
            int userId = userDTO.getIdUser();
            SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();

            Cursor mCount= dbRead.rawQuery("select name, lastname, username, mail, password, usertype from users where userid = " + userId, null);
            mCount.moveToFirst();
            String name = mCount.getString(0);
            String lastName = mCount.getString(1);
            String userName = mCount.getString(2);
            String mail = mCount.getString(3);
            String password = mCount.getString(4);
            String userType = mCount.getString(5);

            userDTO.setName(name);
            userDTO.setLastName(lastName);
            userDTO.setUserName(userName);
            userDTO.setMail(mail);
            userDTO.setPassword(password);
            userDTO.setIdTypeUser(userType);

            mCount.close();

            return userDTO;
        }
        return null;
    }
}
