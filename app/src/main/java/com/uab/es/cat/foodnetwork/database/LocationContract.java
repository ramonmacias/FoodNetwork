package com.uab.es.cat.foodnetwork.database;

import android.provider.BaseColumns;

/**
 * Created by ramonmacias on 25/10/15.
 */
public class LocationContract {

    public LocationContract(){}

    public static abstract class LocationEntry implements BaseColumns{
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_NAME_LOCATION_ID = "locationid";
        public static final String COLUMN_NAME_STREET_NAME = "streetname";
        public static final String COLUMN_NAME_BUILDING_NUMBER = "buildingnumber";
        public static final String COLUMN_NAME_FLOOR = "floor";
        public static final String COLUMN_NAME_DOOR = "door";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_NEIGHBORHOOD = "neighborhood";
        public static final String COLUMN_NAME_DISTRICT = "district";
        public static final String COLUMN_NAME_INSERT_DATE = "insertDate";
        public static final String COLUMN_NAME_LAST_UPDATE = "lastUpdate";
        public static final String COLUMN_NAME_UPDATE_USER = "updateUser";
    }
}
