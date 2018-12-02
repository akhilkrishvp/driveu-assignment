package com.driveu.driveutest.SharedPreferance;

import com.driveu.driveutest.Helper.Constants;
import com.driveu.driveutest.Helper.DriveUTestApplication;

/**
 * Created by akhil on 1/12/18.
 */

public class DriveUPref {
    public static boolean isServiceStarted() {
        return SharedPreferencesHelper.
                getBoolean(DriveUTestApplication.getContext(), Constants.IS_SERVICE_STARTED, false);
    }
    public static void setStartService(boolean flag){
        SharedPreferencesHelper.putBoolean(DriveUTestApplication.getContext(), Constants.IS_SERVICE_STARTED, flag);

    }
}
