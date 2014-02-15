package com.espersoftworks.android.app.hourlynotification.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AdFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		AdView adView = new AdView(getActivity(), AdSize.BANNER, "ca-app-pub-2078905918254410/9975500487");

		// Initiate a generic request to load it with an ad
		AdRequest request = new AdRequest();
		request.addTestDevice(AdRequest.TEST_EMULATOR);
		request.addTestDevice("630709098A8C59B1FCD8D4266AFDCC6B");
		adView.loadAd(request);
		
		return adView;
	}
}
