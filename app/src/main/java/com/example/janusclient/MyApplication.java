package com.example.janusclient;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import com.dnion.P2PSDK;
import com.dnion.P2PSDKDelegate;
import com.dnion.P2PSDKSignaling;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lg on 24/10/2017.
 */

public class MyApplication extends Application {
    IntentFilter filter;
    private static final String TAG = "MyApplication";
    private static Context _context;
    public static final String MOBILEBRAND = android.os.Build.MODEL;
    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();


        EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .throwSubscriberException(true)
                .build();
        P2PSDK.CreateJanusGateway(getApplicationContext(), true);
        P2PSDK.getInstance().setListener(new P2PSDKDelegate() {
            @Override
            public void onConnectionSuccess() {
                Log.i(TAG, "onConnect");
                EventBus.getDefault().post(new JanusMessages.OnConnectionSuccess());
            }

            @Override
            public void onConnectionFailed(String error) {
                Log.i(TAG, "onConnect");
                EventBus.getDefault().post(new JanusMessages.OnConnectionFailed(error));
            }

            @Override
            public void onDisconnected(String reason) {
                Log.i(TAG, "onConnect");
                EventBus.getDefault().post(new JanusMessages.OnDisconnected(reason));
            }

            @Override
            public void onChatStart() {
                Log.i(TAG, "onChatStart");
                EventBus.getDefault().post(new JanusMessages.OnChatStart());
            }

            @Override
            public void onChatStop() {
                Log.i(TAG, "onChatStop");
                EventBus.getDefault().post(new JanusMessages.OnChatStop());
            }

            @Override
            public void onGotUserVideo() {
                EventBus.getDefault().post(new JanusMessages.onGotUserVideo());
            }

            @Override
            public void onLostUserVideo() {
                EventBus.getDefault().post(new JanusMessages.onLostUserVideo());
            }
        });
    }

    public boolean isScreenLandscape() {
        int ortation = getScreenOrientation();
        return ortation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||  ortation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
    }

    public int getScreenOrientation() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    Log.e(TAG, "Unknown screen orientation. Defaulting to " +
                            "portrait.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    Log.e(TAG, "Unknown screen orientation. Defaulting to " +
                            "landscape.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

}
