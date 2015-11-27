package com.uab.es.cat.foodnetwork.database;

import android.provider.BaseColumns;

/**
 * Created by ramonmacias on 13/10/15.
 */
public class UserContract {

    public UserContract(){}

    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USER_ID = "userid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LAST_NAME = "lastname";
        public static final String COLUMN_NAME_USER_NAME = "username";
        public static final String COLUMN_NAME_MAIL = "mail";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_USER_TYPE = "usertype";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phonenumber";
        public static final String COLUMN_NAME_ID_LOCATION = "idlocation";
        public static final String COLUMN_NAME_TYPE_VEHICLE = "typeVehicle";
        public static final String COLUMN_NAME_FINAL_HOUR = "finalHour";
        public static final String COLUMN_NAME_INITIAL_HOUR = "initialHour";
        public static final String COLUMN_NAME_ACTION_RADIO = "actionRadio";
        public static final String COLUMN_NAME_INSERT_DATE = "insertDate";
        public static final String COLUMN_NAME_LAST_UPDATE = "lastUpdate";
        public static final String COLUMN_NAME_UPDATE_USER = "updateUser";
    }
}
