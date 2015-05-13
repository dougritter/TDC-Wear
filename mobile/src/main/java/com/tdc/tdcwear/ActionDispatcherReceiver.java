package com.tdc.tdcwear;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationManagerCompat;

public class ActionDispatcherReceiver extends BroadcastReceiver {

    public static final String EXTRA_ACTION = "action_to_dispatch";
    public static final int ACTION_CALENDAR = 1;

    private static final String PREF_KEY_COUNT = "count";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null){
            if (intent.getIntExtra(EXTRA_ACTION, -1) == ACTION_CALENDAR){
                Uri.Builder uriBuilder = CalendarContract.CONTENT_URI.buildUpon();
                uriBuilder.appendPath("time");
                ContentUris.appendId(uriBuilder, System.currentTimeMillis());

                Intent actionIntent = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
                actionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(actionIntent);


            }
            NotificationManagerCompat.from(context).cancel(NotificationUtils.NOTIFICATION_ID);
            setUnreadCount(context, 0);
        }
    }

    public static int getUnreadCount(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(PREF_KEY_COUNT, 0);
    }

    public static void setUnreadCount(Context context, int count){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(PREF_KEY_COUNT, count).apply();
    }
}
