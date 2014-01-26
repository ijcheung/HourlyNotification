package com.espersoftworks.android.app.hourlynotification;

import java.util.Calendar;

import com.espersoftworks.android.app.hourlynotification.service.NotificationService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StartupBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//Retrieve Global Settings
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		
		if(sharedPref.getBoolean(MainActivity.PREF_KEY_ENABLED, false)) {
			Calendar calendar = Calendar.getInstance();
			int minute = Integer.parseInt(sharedPref.getString(MainActivity.PREF_MINUTE_PICKER, "0"));
			
			if(calendar.get(Calendar.MINUTE) >= minute) {
				calendar.add(Calendar.HOUR, 1);
			}
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, minute);
			AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 60*60*1000, generatePendingNotificationIntent(context));
		}
	}
	
	private PendingIntent generatePendingNotificationIntent(Context context) {
		Intent notificationIntent = new Intent(context, NotificationService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, notificationIntent, 0);
		
		return pendingIntent;
	}
}
