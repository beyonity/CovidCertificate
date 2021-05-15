package com.bogarsoft.covidcertificate.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageUtility {

    private String STORAGE;
    private SharedPreferences preferences;
    private Context context;

    public StorageUtility(Context context) {
        this.context = context;
        this.STORAGE = context.getPackageName()+".STORAGE";
    }

    public void setDistrict(String name){
        preferences = context.getSharedPreferences(STORAGE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("district",name);
        editor.apply();
    }

    public String getDistrict(){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getString("district", "None");//return -1 if no data found
    }

    public void setState(String name){
        preferences = context.getSharedPreferences(STORAGE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("state",name);
        editor.apply();
    }

    public String getState(){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getString("state", "None");//return -1 if no data found
    }

    public void subscribeToTopic(String topic, boolean result) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(topic, result);
        editor.apply();
    }

    public boolean issubscribeToTopic(String topic){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getBoolean(topic, false);//return -1 if no data found
    }




    public void clear(){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
