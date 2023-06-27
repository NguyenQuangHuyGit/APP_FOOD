package com.example.appfood.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionUser {
    private static final String PREF_NAME = "User";

    private static final String USER_ID = "id";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionUser(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUserId(String userId){
        editor.putString(USER_ID,userId);
        editor.apply();
    }

    public String getUserId(){
        return sharedPreferences.getString(USER_ID,null);
    }

    public void clear(){
        editor.clear();
        editor.apply();
    }
}
