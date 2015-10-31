package com.uab.es.cat.foodnetwork.database;

import android.provider.BaseColumns;

/**
 * Created by ramonmacias on 31/10/15.
 */
public class FoodsContract {

    public FoodsContract(){}

    public static abstract class FoodEntry implements BaseColumns{
        public static final String TABLE_NAME = "foods";
        public static final String COLUMN_NAME_FOOD_ID = "foodid";
        public static final String COLUMN_NAME_FOOD_NAME = "foodname";
        public static final String COLUMN_NAME_FOOD_TYPE = "foodtype";
        public static final String COLUMN_NAME_EXPIRATION_DATE = "expirationdate";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
    }
}
