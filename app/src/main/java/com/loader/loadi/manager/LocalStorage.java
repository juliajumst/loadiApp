package com.loader.loadi.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Wendy on 10/22/2016.
 */
public class LocalStorage {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public static final String WEBSERVICE = "webservice";

    public static final String FIRST_LOADED = "first_loaded";

    public LocalStorage(Context context){
        preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public SharedPreferences getLocalStorage(){
        return this.preferences;
    }

    public SharedPreferences.Editor getLocalStorageEditor(){
        return this.editor;
    }

    public void saveChanges(){
        editor.apply();
    }

    public void saveIntegerOnLocalStorage(String key, int value){
        editor.putInt(key, value);
        editor.apply();
    }

    public void saveStringOnLocalStorage(String key, boolean value){
        editor.putBoolean(key, value);
        editor.apply();
    }
    public void saveStringOnLocalStorage(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }
}
