package com.tldr;
import java.io.ObjectInputStream.GetField;
import java.util.HashMap;
import java.util.List;

import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.taskendpoint.model.Task;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MyOnClickListener implements OnItemClickListener{
		private final int TAG_PROFILE = 0;  
		private final int TAG_TASK = 1;  
		private final static String TAG_TITLE="title";
		private final static String TAG_NAME="name";
	
		int tag; 
		List<HashMap<String, String>> list;
		Fragment fragment;
		List<Task> tasks;
		List<UserInfo> users;
		
		public MyOnClickListener (int tag, List<HashMap<String, String>> list, List<Task> tasks, List<UserInfo> users, Fragment fragment){
			this.list = list;
			this.tag = tag;
			this.fragment = fragment;
			this.tasks = tasks;
			this.users = users;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Fragment f1 = null;
			Bundle bundle = new Bundle();
			if (tag == TAG_PROFILE){
				f1 = new ProfileDetailsFragment();
				bundle.putSerializable("HashMap", list.get(arg2));
				if (users != null){
					UserInfo myUserInfo = null;
					for (UserInfo u: users){
						if (u.getUsername().equals(list.get(arg2).get(TAG_NAME))){
							myUserInfo = u;
						}
					}
					bundle.putDouble("geo_lat", myUserInfo.getGeoLat());
					bundle.putDouble("geo_lon", myUserInfo.getGeoLon());
				}
				else {
					bundle.putDouble("geo_lat", 52.12);
					bundle.putDouble("geo_lon", 11.59);
				}
			}
			else if (tag == TAG_TASK){
				f1 = new TaskDetailsFragment();
				bundle.putSerializable("HashMap", list.get(arg2));
				if (tasks != null){
					Task myTask = null;
					for (Task t: tasks){
						if (t.getTitle().equals(list.get(arg2).get(TAG_TITLE))){
							myTask = t;
						}
					}
					bundle.putDouble("geo_lat", myTask.getGeoLat());
					bundle.putDouble("geo_lon", myTask.getGeoLon());
				}
				else {
					bundle.putDouble("geo_lat", 52.12);
					bundle.putDouble("geo_lon", 11.59);
				}
			}
			f1.setArguments(bundle);
			FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
			ft.replace(fragment.getId(), f1);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.addToBackStack(null);
			ft.commit();

		}

	}