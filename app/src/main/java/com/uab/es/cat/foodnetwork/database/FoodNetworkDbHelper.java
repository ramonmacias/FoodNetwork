package com.uab.es.cat.foodnetwork.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ramonmacias on 13/10/15.
 */
public class FoodNetworkDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES_USER =
            "create table if not exists " + UserContract.UserEntry.TABLE_NAME + " (" +
                    UserContract.UserEntry.COLUMN_NAME_USER_ID + " INTEGER PRIMARY KEY," +
                    UserContract.UserEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    UserContract.UserEntry.COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
                    UserContract.UserEntry.COLUMN_NAME_USER_NAME + TEXT_TYPE + COMMA_SEP +
                    UserContract.UserEntry.COLUMN_NAME_MAIL + TEXT_TYPE + COMMA_SEP +
                    UserContract.UserEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    UserContract.UserEntry.COLUMN_NAME_PHONE_NUMBER + TEXT_TYPE + COMMA_SEP +
                    UserContract.UserEntry.COLUMN_NAME_ID_LOCATION + " INTEGER " + COMMA_SEP +
                    UserContract.UserEntry.COLUMN_NAME_USER_TYPE + TEXT_TYPE +
                    " );";
    private static final String SQL_CREATE_ENTRIES_LOCATION =
            "create table if not exists " + LocationContract.LocationEntry.TABLE_NAME + " (" +
                    LocationContract.LocationEntry.COLUMN_NAME_LOCATION_ID + " INTEGER PRIMARY KEY," +
                    LocationContract.LocationEntry.COLUMN_NAME_STREET_NAME + TEXT_TYPE + COMMA_SEP +
                    LocationContract.LocationEntry.COLUMN_NAME_BUILDING_NUMBER + TEXT_TYPE + COMMA_SEP +
                    LocationContract.LocationEntry.COLUMN_NAME_FLOOR + TEXT_TYPE + COMMA_SEP +
                    LocationContract.LocationEntry.COLUMN_NAME_DOOR + TEXT_TYPE + COMMA_SEP +
                    LocationContract.LocationEntry.COLUMN_NAME_CITY + TEXT_TYPE + COMMA_SEP +
                    LocationContract.LocationEntry.COLUMN_NAME_NEIGHBORHOOD + TEXT_TYPE + COMMA_SEP +
                    LocationContract.LocationEntry.COLUMN_NAME_DISTRICT + TEXT_TYPE +
                    " );";
    private static final String SQL_CREATE_ENTRIES_FOODS =
            " create table if not exists " + FoodsContract.FoodEntry.TABLE_NAME + " (" +
                    FoodsContract.FoodEntry.COLUMN_NAME_FOOD_ID + " INTEGER PRIMARY KEY," +
                    FoodsContract.FoodEntry.COLUMN_NAME_FOOD_NAME + TEXT_TYPE + COMMA_SEP +
                    FoodsContract.FoodEntry.COLUMN_NAME_FOOD_TYPE + TEXT_TYPE + COMMA_SEP +
                    FoodsContract.FoodEntry.COLUMN_NAME_EXPIRATION_DATE + TEXT_TYPE + COMMA_SEP +
                    FoodsContract.FoodEntry.COLUMN_NAME_QUANTITY + TEXT_TYPE +
                    " );";
    private static final String SQL_CREATE_ENTRIES_DONATION =
            " create table if not exists " + DonationContract.DonationEntry.TABLE_NAME + " (" +
                    DonationContract.DonationEntry.COLUMN_NAME_DONATION_ID + " INTEGER," +
                    DonationContract.DonationEntry.COLUMN_NAME_FOOD_ID + " INTEGER," +
                    DonationContract.DonationEntry.COLUMN_NAME_LOCATION_ID + " INTEGER," +
                    DonationContract.DonationEntry.COLUMN_NAME_USER_ID + " INTEGER," +
                    DonationContract.DonationEntry.COLUMN_NAME_STATE + TEXT_TYPE +
                    " ,PRIMARY KEY (" +
                    DonationContract.DonationEntry.COLUMN_NAME_DONATION_ID +
                    ", " +
                    DonationContract.DonationEntry.COLUMN_NAME_FOOD_ID +
                    ", " +
                    DonationContract.DonationEntry.COLUMN_NAME_USER_ID +
                    ") );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME +
            "; DROP TABLE IF EXISTS " + LocationContract.LocationEntry.TABLE_NAME +
            "; DROP TABLE IF EXISTS " + FoodsContract.FoodEntry.TABLE_NAME +
            "; DROP TABLE IF EXISTS " + DonationContract.DonationEntry.TABLE_NAME;

    private static final String SQL_DELETE_USER = "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;
    private static final String SQL_DELETE_LOCATION = "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;
    private static final String SQL_DELETE_FOODS = "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;
    private static final String SQL_DELETE_DONATION = "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 14;
    public static final String DATABASE_NAME = "FoodNetwork.db";

    public FoodNetworkDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_LOCATION);
        db.execSQL(SQL_CREATE_ENTRIES_DONATION);
        db.execSQL(SQL_CREATE_ENTRIES_FOODS);
        db.execSQL(SQL_CREATE_ENTRIES_USER);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_USER);
        db.execSQL(SQL_DELETE_LOCATION);
        db.execSQL(SQL_DELETE_FOODS);
        db.execSQL(SQL_DELETE_DONATION);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
