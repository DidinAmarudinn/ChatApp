package com.example.chatapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefHelper {
    private static final String HELPER="helper";
    private static final String UID="uid";
    private static final String USERNAME="username";
    private SharedPreferences app_prefs;
    private Context context;

    public PrefHelper(Context context) {
        app_prefs = context.getSharedPreferences("shared",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putUID(String uid){
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(UID, uid);
        edit.commit();
    }
    public String getUID() {
        return app_prefs.getString(UID, "");

    }
    public void putUsername(String username){
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(USERNAME, username);
        edit.commit();
    }
    public String getUsername() {
        return app_prefs.getString(USERNAME, "");
    }

}
