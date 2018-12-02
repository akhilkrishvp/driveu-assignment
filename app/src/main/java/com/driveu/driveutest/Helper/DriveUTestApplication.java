package com.driveu.driveutest.Helper;

import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.util.LruCache;

/**
 * Created by akhil on 1/12/18.
 */

public class DriveUTestApplication extends MultiDexApplication

    {
        private static DriveUTestApplication instance;
        private static LruCache<String, Typeface> sTypefaceCache;


        @Override
        public void onCreate() {
        super.onCreate();
        instance = this;

    }

        @Override
        protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return instance;
    }
}
