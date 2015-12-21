package com.uab.es.cat.foodnetwork.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ramonmacias on 22/11/15.
 */
public class Constants {

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final String LOCATION_ADDRES_TEXT = PACKAGE_NAME + ".LOCATION_ADDRES_TEXT";
    public static final int ACTIVE_STATE = 1;
    public static final int CURRENT_STATE = 2;
    public static final int COMPLETED_STATE = 3;

    public static Map<Integer, String> STATE;
    static {
        HashMap<Integer, String> STATE_AUX = new HashMap<Integer, String>();
        STATE_AUX.put(1, "Activo");
        STATE_AUX.put(2, "En curso");
        STATE_AUX.put(3, "Finalizada");
        STATE = Collections.unmodifiableMap(STATE_AUX);
    };

    public static Map<String, Integer> HOURS;
    static {
      HashMap<String, Integer>  HOURS_AUX = new HashMap<String, Integer>();
        HOURS_AUX.put("00:00", 0);
        HOURS_AUX.put("01:00", 1);
        HOURS_AUX.put("02:00", 2);
        HOURS_AUX.put("03:00", 3);
        HOURS_AUX.put("04:00", 4);
        HOURS_AUX.put("05:00", 5);
        HOURS_AUX.put("06:00", 6);
        HOURS_AUX.put("07:00", 7);
        HOURS_AUX.put("08:00", 8);
        HOURS_AUX.put("09:00", 9);
        HOURS_AUX.put("10:00", 10);
        HOURS_AUX.put("11:00", 11);
        HOURS_AUX.put("12:00", 12);
        HOURS_AUX.put("13:00", 13);
        HOURS_AUX.put("14:00", 14);
        HOURS_AUX.put("15:00", 15);
        HOURS_AUX.put("16:00", 16);
        HOURS_AUX.put("17:00", 17);
        HOURS_AUX.put("18:00", 18);
        HOURS_AUX.put("19:00", 19);
        HOURS_AUX.put("20:00", 20);
        HOURS_AUX.put("21:00", 21);
        HOURS_AUX.put("22:00", 22);
        HOURS_AUX.put("23:00", 23);
        HOURS = Collections.unmodifiableMap(HOURS_AUX);

    };
}
