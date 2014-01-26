package com.espersoftworks.android.app.hourlynotification;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.espersoftworks.android.app.hourlynotification.service.NotificationService;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class MainActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static final String PREF_KEY_ENABLED = "pref_key_enabled";
	public static final String PREF_MINUTE_PICKER = "pref_key_minute_picker";
	public static final String PREF_KEY_SOUND= "pref_key_sound";
	public static final String PREF_KEY_VIBRATE = "pref_key_vibrate";
	public static final String PREF_KEY_FLASH = "pref_key_flash";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		
		EditTextPreference minutePicker = (EditTextPreference) findPreference(PREF_MINUTE_PICKER);
		minutePicker.setDefaultValue("00");
		
		setMinutePickerSummary();
		
		EditText minutePickerEditText = minutePicker.getEditText();
		InputFilter[] filters = {new InputFilter.LengthFilter(2)};
		minutePickerEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		minutePickerEditText.setFilters(filters);
		
		AdView adView = new AdView(this, AdSize.BANNER, "ca-app-pub-2078905918254410/9975500487");

        setListFooter(adView);

        // Initiate a generic request to load it with an ad
        AdRequest request = new AdRequest();
        request.addTestDevice(AdRequest.TEST_EMULATOR);
        request.addTestDevice("630709098A8C59B1FCD8D4266AFDCC6B");
        adView.loadAd(request); 
	}

	private void setMinutePickerSummary() {
		EditTextPreference minutePicker = (EditTextPreference) findPreference(PREF_MINUTE_PICKER);
		int minutePickerValue;
		try {
			minutePickerValue = Integer.parseInt(minutePicker.getText());
			
			if(minutePickerValue > 59)
			{
				minutePicker.setText("0");
				minutePickerValue = 0;
			}
		} catch (NumberFormatException e) {
			minutePicker.setText("0");
			minutePickerValue = 0;
		}
		minutePicker.setSummary("X:" + ((minutePickerValue > 9)?minutePickerValue:"0"+minutePickerValue));
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(PREF_KEY_ENABLED)) {
			if(sharedPreferences.getBoolean(key, false)) {
				setNotification();
			}
			else {
				cancelNotification();
			}
		}
		else if (key.equals(PREF_MINUTE_PICKER)) {
			setMinutePickerSummary();
			
			if(sharedPreferences.getBoolean(PREF_KEY_ENABLED, false)) {
				setNotification();
			}
		}
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
	
	private void setNotification() {
		Calendar calendar = Calendar.getInstance();
		int minute = Integer.parseInt(getPreferenceScreen().getSharedPreferences().getString(PREF_MINUTE_PICKER, "0"));
		
		if(calendar.get(Calendar.MINUTE) >= minute) {
			calendar.add(Calendar.HOUR, 1);
		}
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, minute);
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 60*60*1000, generatePendingNotificationIntent());
		
		//Notify User
		Toast.makeText(getApplicationContext(), "Next Notification Set for: " + DateFormat.getTimeInstance().format(calendar.getTime()), Toast.LENGTH_SHORT).show();
	}
	
	private void cancelNotification() {
		PendingIntent pendingIntent = generatePendingNotificationIntent();
		pendingIntent.cancel();
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		
		//Notify User
		Toast.makeText(getApplicationContext(), "Notification Disabled", Toast.LENGTH_SHORT).show();
	}
	
	private PendingIntent generatePendingNotificationIntent() {
		Intent notificationIntent = new Intent(MainActivity.this, NotificationService.class);
		PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, notificationIntent, 0);
		
		return pendingIntent;
	}
}