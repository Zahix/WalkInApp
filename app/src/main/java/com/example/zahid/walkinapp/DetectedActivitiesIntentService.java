package com.example.zahid.walkinapp;

import android.app.IntentService;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 * Created by Zahid on 29/05/2019.
 */

public class DetectedActivitiesIntentService extends IntentService {



    protected static final String TAG = DetectedActivitiesIntentService.class.getSimpleName();

    DevicePolicyManager policyManager;
    ComponentName adminReceiver;
    boolean admin;

    public DetectedActivitiesIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(Intent intent) {
        policyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);

        adminReceiver = new ComponentName(getApplicationContext(),ScreenOffAdminReceiver.class);
        admin = policyManager.isAdminActive(adminReceiver);

        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

        for (DetectedActivity activity : detectedActivities) {
            Log.e(TAG, "Detected activity: " + activity.getType() + ", " + activity.getConfidence());
            broadcastActivity(activity);
            handleUserActivity(activity.getType(),activity.getConfidence());


        }
    }

    private void handleUserActivity(int type, int confidence) {
        String label = "Unkwons";
        // int icon = R.drawable.ic_still;

        switch (type) {
            case DetectedActivity.IN_VEHICLE: {
                label = "Vehicle";
                // icon = R.drawable.ic_driving;
                if (admin) {

                    policyManager.lockNow();
                }
                break;
            }
            case DetectedActivity.ON_BICYCLE: {
                label = "Bicycle";
                //icon = R.drawable.ic_on_bicycle;
                if (admin) {

                    policyManager.lockNow();
                }
                break;
            }
            case DetectedActivity.ON_FOOT: {
                label = "On foot";
                //icon = R.drawable.ic_walking;
                if (admin) {

                    policyManager.lockNow();
                }
                break;
            }
            case DetectedActivity.RUNNING: {
                label = "Running";
                //icon = R.drawable.ic_running;
                if (admin) {

                    policyManager.lockNow();
                }
                break;
            }
            case DetectedActivity.STILL: {
                label = "No Move";



                break;
            }
            case DetectedActivity.TILTING: {
                label = "Tilting Move";
                //icon = R.drawable.ic_tilting;
                if (admin) {

                    policyManager.lockNow();
                }
                break;
            }
            case DetectedActivity.WALKING: {
                label = "Walking";
                // icon = R.drawable.ic_walking;
                if (admin) {

                    policyManager.lockNow();
                }
                break;
            }
            case DetectedActivity.UNKNOWN: {
                label = "Unknow";
                break;
            }
        }

        Log.e(TAG, "User activity: " + label + ", Confidence: " + confidence);


    }

    private void broadcastActivity(DetectedActivity activity) {
        Intent intent = new Intent(Constants.BROADCAST_DETECTED_ACTIVITY);
        intent.putExtra("type", activity.getType());
        intent.putExtra("confidence", activity.getConfidence());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}