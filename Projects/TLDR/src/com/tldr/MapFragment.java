package com.tldr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class MapFragment extends Fragment {


	public void initialize(){
		Bundle args = getArguments();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = new View(getActivity());
		v = inflater.inflate(R.layout.map_layout, container, false);
		return v;
	}
	
}
