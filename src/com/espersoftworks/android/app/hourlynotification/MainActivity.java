package com.espersoftworks.android.app.hourlynotification;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
//		getSupportFragmentManager().beginTransaction()
//				.replace(android.R.id.content, new SettingsFragment())
//				.commit();
		
//		AdView adView = new AdView(this, AdSize.BANNER, "ca-app-pub-2078905918254410/9975500487");
//
//		setListFooter(adView);
//
//		// Initiate a generic request to load it with an ad
//		AdRequest request = new AdRequest();
//		request.addTestDevice(AdRequest.TEST_EMULATOR);
//		request.addTestDevice("630709098A8C59B1FCD8D4266AFDCC6B");
//		adView.loadAd(request); 
	}
}