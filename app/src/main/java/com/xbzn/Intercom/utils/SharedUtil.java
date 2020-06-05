package com.xbzn.Intercom.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.Key;

public class SharedUtil {
    private SharedPreferences sharedPreferences;
    public SharedUtil(Context context){
        sharedPreferences =context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }
    public String getString(String key){
      return   sharedPreferences.getString(key,"null");
    }
    public void putString(String key,String value){
        sharedPreferences.edit().putString(key, value).apply();
    }

}
