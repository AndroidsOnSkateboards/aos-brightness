package com.androidsonskateboards.brightness;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class AOSBrightness extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("brightness")) {
            double brightnessPercent = args.getDouble(0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                adjustBrightnessCompat(brightnessPercent);
            }
            else {
                adjustBrightness(brightnessPercent);
            }

            return true;
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void adjustBrightnessCompat(double percent) {
        Context context = this.cordova.getActivity();
        
        if (Settings.System.canWrite(context)) {
            adjustBrightness(percent);
        } else {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    private void adjustBrightness(double percent) {
		Activity activity = this.cordova.getActivity();

        int brightness = (int) (percent * 255);

        // Constrain the value of brightness
        if (brightness < 0) {
            brightness = 0;
        }
        else if (brightness > 255) {
            brightness = 255;
        }

        ContentResolver cResolver = activity.getApplicationContext().getContentResolver();
		Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
    }
}
