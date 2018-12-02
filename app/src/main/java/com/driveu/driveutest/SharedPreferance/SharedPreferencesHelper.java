package com.driveu.driveutest.SharedPreferance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by akhil on 21/9/18.
 */

public class SharedPreferencesHelper { private static Context mContext;
    private static final String PREF_NAME = "drive_interface";
    int PRIVATE_MODE = 0;


    public SharedPreferencesHelper(Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        this.mContext = context;
        preferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        edit = preferences.edit();
    }

    public static void putBoolean(Context mContext, String key, boolean val) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, val);
        //edit.clear();
        edit.apply();
    }

    public static boolean getBoolean(Context mContext, String key, boolean _default) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        return preferences.getBoolean(key, _default);
    }

    public static void clearPreferences(Context mContext) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        preferences.edit().clear().apply();
    }
}