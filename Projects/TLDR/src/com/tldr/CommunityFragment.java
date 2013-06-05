package com.tldr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class CommunityFragment extends Fragment {
	
	private TabHost mTabHost;	
	
	public void initialize(){
		Bundle args = getArguments();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = new View(getActivity());
		v = inflater.inflate(R.layout.community_layout, container, false);
		
		mTabHost = (TabHost) v.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabHost.TabSpec tab;
		
		tab = mTabHost.newTabSpec("highscore");
		tab.setIndicator("highscore");
		tab.setContent(R.id.highscore_layout);
		mTabHost.addTab(tab);
		
		tab = mTabHost.newTabSpec("friends");
		tab.setIndicator("friends");
		tab.setContent(R.id.friends_layout);
		mTabHost.addTab(tab);
		
		tab = mTabHost.newTabSpec("nearby people");
		tab.setIndicator("nearby people");
		tab.setContent(R.id.nearby_people_layout);
		mTabHost.addTab(tab);
		
		
		return v;
	}

}
