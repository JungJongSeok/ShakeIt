package com.mingle.myapplication.mapviewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingle.myapplication.R;

public class Fragment1 extends NMapFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.map_fragment1, container, false);
	}
}
