package com.driveu.driveutest.Helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.driveu.driveutest.R;

import java.util.ArrayList;

/**
 * Created by akhil on 1/12/18.
 */

public class PermissionsHelper  {
        public static boolean isMarshMallow(){
    return(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M);
}

        /**Manifest import used here is android and not app **/

        public static final String LOCATION_FINE= Manifest.permission.ACCESS_FINE_LOCATION;
        public static final String LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
        public static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
        public static final int REQUEST_PERMISSIONS = 165;
        public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        public static final String ACCESS_CAMERA = Manifest.permission.CAMERA;
        public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;

        public static  String KEY = "DEFAULT";


        @TargetApi(Build.VERSION_CODES.M)
        public static Boolean requestPermissionAccess(Activity mActivity) {

            if (isMarshMallow()) {
                Boolean isAccessDenied = false;
                ArrayList<String> permissions = new ArrayList<>();
                if ((ContextCompat.checkSelfPermission(mActivity,
                        PermissionsHelper.LOCATION_FINE)
                        != PackageManager.PERMISSION_GRANTED)) {
                    permissions.add(PermissionsHelper.LOCATION_FINE);
                    if (mActivity.shouldShowRequestPermissionRationale(PermissionsHelper.LOCATION_FINE)) {
                        isAccessDenied = true;
                    }
                }

                if ((ContextCompat.checkSelfPermission(mActivity,
                        PermissionsHelper.LOCATION_COARSE)
                        != PackageManager.PERMISSION_GRANTED)) {
                    permissions.add(PermissionsHelper.LOCATION_COARSE);
                    if (mActivity.shouldShowRequestPermissionRationale(PermissionsHelper.LOCATION_COARSE)) {
                        isAccessDenied = true;
                    }
                }

                if (!isAccessDenied) {
                    if (permissions.size() == 0)
                        return true;
                    else {
                        String[] mStringArray = new String[permissions.size()];
                        mStringArray = permissions.toArray(mStringArray);
                        ActivityCompat.requestPermissions(mActivity, mStringArray,
                                PermissionsHelper.REQUEST_PERMISSIONS);
                        return false;
                    }
                } else {
                    //   TODO: Needed for mandatory permissions
                    PermissionsHelper.showSettingsSnackbar(mActivity,"Please provide required permission");
                    return false;
                }

            } else {
                return true;
            }
        }

        public static void showSettingsSnackbar(final Activity mActivity, final String msg) {

            final int REQUEST_PERMISSION_SETTING = 101;


            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View parentLayout = mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
                    Snackbar mSnackbar = Snackbar.make(parentLayout, msg, Snackbar.LENGTH_INDEFINITE)

                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Intent i = new Intent();
                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    i.addCategory(Intent.CATEGORY_DEFAULT);
                                    i.setData(Uri.parse("package:" + "in.stjhons"));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    mActivity.startActivity(i);
                                }
                            })
                            .setDuration(Snackbar.LENGTH_LONG);
                    mSnackbar.getView().setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
                    mSnackbar.setActionTextColor(ContextCompat.getColor(mActivity, R.color.white));
                    TextView textView = ((TextView) mSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text));
                    textView.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                    textView.setMaxLines(5);
                    mSnackbar.show();
                }
            });
        }

}