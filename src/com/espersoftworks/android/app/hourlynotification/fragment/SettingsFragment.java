package com.espersoftworks.android.app.hourlynotification.fragment;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.v4.preference.PreferenceFragment;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.espersoftworks.android.app.hourlynotification.R;
import com.espersoftworks.android.app.hourlynotification.service.NotificationService;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	private Resources res;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		
		res = getResources();
		
		EditTextPreference minutePicker = (EditTextPreference) findPreference(res.getString(R.string.pref_key_minute_picker));
		minutePicker.setDefaultValue("00");
		
		setMinutePickerSummary();
		
		EditText minutePickerEditText = minutePicker.getEditText();
		InputFilter[] filters = {new InputFilter.LengthFilter(2)};
		minutePickerEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		minutePickerEditText.setFilters(filters);
	}
	
	private void setMinutePickerSummary() {
		EditTextPreference minutePicker = (EditTextPreference) findPreference(res.getString(R.string.pref_key_minute_picker));
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
		if (key.equals(res.getString(R.string.pref_key_enabled))) {
			if(sharedPreferences.getBoolean(key, false)) {
				setNotification();
			}
			else {
				cancelNotification();
			}
		}
		else if (key.equals(res.getString(R.string.pref_key_minute_picker))) {
			setMinutePickerSummary();
			
			if(sharedPreferences.getBoolean(res.getString(R.string.pref_key_enabled), false)) {
				setNotification();
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
	
	private void setNotification() {
		Calendar calendar = Calendar.getInstance();
		int minute = Integer.parseInt(getPreferenceScreen().getSharedPreferences().getString(res.getString(R.string.pref_key_minute_picker), "0"));
		
		if(calendar.get(Calendar.MINUTE) >= minute) {
			calendar.add(Calendar.HOUR, 1);
		}
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, minute);
		AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Activity.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 60*60*1000, generatePendingNotificationIntent());
		
		//Notify User
		Toast.makeText(getActivity().getApplicationContext(), "Next Notification Set for: " + DateFormat.getTimeInstance().format(calendar.getTime()), Toast.LENGTH_SHORT).show();
	}
	
	private void cancelNotification() {
		PendingIntent pendingIntent = generatePendingNotificationIntent();
		pendingIntent.cancel();
		AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Activity.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		
		//Notify User
		Toast.makeText(getActivity().getApplicationContext(), "Notification Disabled", Toast.LENGTH_SHORT).show();
	}
	
	private PendingIntent generatePendingNotificationIntent() {
		Intent notificationIntent = new Intent(getActivity(), NotificationService.class);
		PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, notificationIntent, 0);
		
		return pendingIntent;
	}
}
