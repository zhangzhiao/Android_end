package com.zza.service;

import android.content.Context;
import android.content.SharedPreferences;

public class SUtil {
    SharedPreferences sharedPreferences;
    public SUtil(Context context){
        sharedPreferences =context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }
    public String getString (String key){
        return sharedPreferences.getString(key,"no");
    }
    public void putString(String key,String value){
        sharedPreferences.edit().putString(key, value).apply();
    }
}
