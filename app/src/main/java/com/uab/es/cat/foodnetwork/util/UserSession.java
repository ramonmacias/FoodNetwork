package com.uab.es.cat.foodnetwork.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.uab.es.cat.foodnetwork.R;

/**
 * Created by ramonmacias on 30/10/15.
 * Class to manage the userSession based on Singleton design pattern.
 */
public class UserSession {

    private static UserSession userSession = null;
    long userId;
    String userType;
    boolean logged;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private String sessionManagerKey = "sessionManager";
    private String userIdKey = "userId";
    private String userTypeKey = "userType";
    private String loggedKey = "loggedKey";

    protected UserSession(Context context){
        sharedPref = context.getSharedPreferences(sessionManagerKey, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public static UserSession getInstance(Context context){
        if(userSession == null){
            userSession = new UserSession(context);
        }
        return userSession;
    }

    public void logIn(long newUserId, String newUserType){
        this.userId = newUserId;
        this.userType = newUserType;

        editor.putString(userTypeKey, this.userType);
        editor.putLong(userIdKey, this.userId);
        editor.putBoolean(loggedKey, true);
        editor.commit();
    }

    public long getUserId(){
        long defaultValue = 0;
        this.userId = sharedPref.getLong(userIdKey, defaultValue);
        return this.userId;
    }

    public String getUserType(){
        String defaultValue = "";
        this.userType = sharedPref.getString(userTypeKey, defaultValue);
        return this.userType;
    }
}
