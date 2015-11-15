package com.uab.es.cat.foodnetwork.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.uab.es.cat.foodnetwork.LoginActivity;

/**
 * Created by ramonmacias on 30/10/15.
 * Class to manage the userSession based on Singleton design pattern.
 */
public class UserSession {

    private static UserSession userSession = null;
    long userId;
    String userType;
    private String userTypeLoggin;
    boolean logged;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private Context context;

    private String sessionManagerKey = "sessionManager";
    private String userIdKey = "userId";
    private String userTypeKey = "userType";
    private String loggedKey = "loggedKey";
    private String userTypeLogginKey = "userTypeLogginKey";

    protected UserSession(Context context){
        sharedPref = context.getSharedPreferences(sessionManagerKey, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        context = context;
    }

    public static UserSession getInstance(Context context){
        if(userSession == null){
            userSession = new UserSession(context);
        }
        return userSession;
    }

    public void logIn(long newUserId, String newUserType, String userTypeLoggin){
        this.userId = newUserId;
        this.userType = newUserType;
        this.userTypeLoggin = userTypeLoggin;

        editor.putString(userTypeKey, this.userType);
        editor.putLong(userIdKey, this.userId);
        editor.putBoolean(loggedKey, true);
        editor.putString(userTypeLogginKey, this.userTypeLoggin);
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

    public String getUserTypeLoggin(){
        String defaultValue = "";
        this.userTypeLoggin = sharedPref.getString(userTypeLogginKey, defaultValue);
        return this.userTypeLoggin;
    }

    public void logOut(Context context){
        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }
}
