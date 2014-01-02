package com.blahbr.hourlynotification.pref;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.blahbr.hourlynotification.R;

public class MinutePickerPreference extends DialogPreference {
	public MinutePickerPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setDialogLayoutResource(R.layout.minute_picker_pref);
		
		
    }
	
	public MinutePickerPreference(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.dialogPreferenceStyle);
	}
	
	public MinutePickerPreference(Context context) {
		this(context, null);
	}
}
