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

    public static Map<Integer, String> STATE;
    static {
        HashMap<Integer, String> STATE_AUX = new HashMap<Integer, String>();
        STATE_AUX.put(1, "Activo");
        STATE_AUX.put(2, "En curso");
        STATE_AUX.put(3, "Finalizada");
        STATE = Collections.unmodifiableMap(STATE_AUX);
    };
}
