package aut.bme.hu.friendsplus.ui.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.BaseActivity;

public class PermissionChecker {

    private static final String TAG = "PermissionChecker";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    public static boolean checkPermissions(Context context) {
        int permissionState = ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(final BaseActivity activity) {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);


        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            activity.showSnackbar(activity.getString(R.string.permission_rationale),
                    activity.getString(android.R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ActivityCompat.requestPermissions( activity,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");

            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

}
