package com.tldr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class TasksFragment extends Fragment {
	
	private TabHost mTabHost;
	
	public void initialize(){
		Bundle args = getArguments();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = new View(getActivity());
		v = inflater.inflate(R.layout.tasks_layout, container, false);
		
		mTabHost = (TabHost) v.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabHost.TabSpec tab;
		
		tab = mTabHost.newTabSpec("nearby tasks");
		tab.setIndicator("nearby tasks");
		tab.setContent(R.id.nearby_tasks_layout);
		mTabHost.addTab(tab);
		
		tab = mTabHost.newTabSpec("accepted tasks");
		tab.setIndicator("accepted tasks");
		tab.setContent(R.id.accepted_tasks_layout);
		mTabHost.addTab(tab);
		
		tab = mTabHost.newTabSpec("completed tasks");
		tab.setIndicator("completed tasks");
		tab.setContent(R.id.completed_tasks_layout);
		mTabHost.addTab(tab);
		
		return v;
	}

}
