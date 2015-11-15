package com.uab.es.cat.foodnetwork.database;

import android.provider.BaseColumns;

/**
 * Created by ramonmacias on 31/10/15.
 */
public class DonationContract {

    public DonationContract(){}

    public static abstract class DonationEntry implements BaseColumns {
        public static final String TABLE_NAME = "donation";
        public static final String COLUMN_NAME_DONATION_ID = "donatioid";
        public static final String COLUMN_NAME_USER_ID = "userid";
        public static final String COLUMN_NAME_LOCATION_ID = "locationid";
        public static final String COLUMN_NAME_STATE = "state";
        public static final String COLUMN_NAME_INITIAL_HOUR = "initialHour";
        public static final String COLUMN_NAME_FIINAL_HOUR = "finalHour";
        public static final String COLUMN_NAME_INSERT_DATE = "insertDate";
        public static final String COLUMN_NAME_LAST_UPDATE = "lastUpdate";
        public static final String COLUMN_NAME_UPDATE_USER = "updateUser";

    }
}
