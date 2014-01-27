package com.espersoftworks.android.app.hourlynotification.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.espersoftworks.android.app.hourlynotification.MainActivity;
import com.espersoftworks.android.app.hourlynotification.R;

public class NotificationService extends Service {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//Retrieve Global Settings
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		//Notification
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		int notifyID = 1;
		Notification notification = new NotificationCompat.Builder(this)
			.setContentTitle("Hourly Notification")
			.setSmallIcon(R.drawable.ic_launcher)
			.build();
		
		notification.defaults = 0;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
		if(sharedPref.getBoolean(MainActivity.PREF_KEY_SOUND, false))
		{
			notification.defaults |= Notification.DEFAULT_SOUND;
		}
		if(sharedPref.getBoolean(MainActivity.PREF_KEY_VIBRATE, false))
		{
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		if(sharedPref.getBoolean(MainActivity.PREF_KEY_FLASH, false))
		{
			notification.ledARGB = 0xff00ffff;
			notification.ledOnMS = 300;
			notification.ledOffMS = 1000;
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		}
		
		notificationManager.notify(notifyID, notification);
		
		stopSelf();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
