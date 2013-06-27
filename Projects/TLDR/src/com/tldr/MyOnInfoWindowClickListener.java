package com.tldr;

import java.io.ObjectInputStream.GetField;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.Marker;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.taskendpoint.model.Task;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MyOnInfoWindowClickListener implements OnInfoWindowClickListener {
	private final int TAG_PROFILE = 0;
	private final int TAG_TASK = 1;
	private final static String TAG_TITLE = "title";
	private final static String TAG_NAME = "name";

	private Fragment fragment;
	private HashMap<Marker, Task> tasksHashMap;
	private HashMap<Marker, UserInfo> usersHashMap;
	List<HashMap<String, String>> usersList;
	List<HashMap<String, String>> tasksList;

	public MyOnInfoWindowClickListener(Fragment fragment,
			HashMap<Marker, Task> tasksHashMap,
			HashMap<Marker, UserInfo> usersHashMap,
			List<HashMap<String, String>> tasksList,
			List<HashMap<String, String>> usersList) {
		this.fragment = fragment;
		this.tasksHashMap = tasksHashMap;
		this.usersHashMap = usersHashMap;
		this.tasksList = tasksList;
		this.usersList = usersList;
	}

	public void setTasks(HashMap<Marker, Task> tasksHashMap,
			List<HashMap<String, String>> tasksList) {
		this.tasksHashMap = tasksHashMap;
		this.tasksList = tasksList;
	}

	public void setUsers(HashMap<Marker, UserInfo> usersHashMap,
			List<HashMap<String, String>> usersList) {
		this.usersHashMap = usersHashMap;
		this.usersList = usersList;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Fragment f1 = null;
		Bundle bundle = new Bundle();
		if (usersHashMap != null && usersHashMap.containsKey(marker)) {
			f1 = new ProfileDetailsFragment();
			HashMap<String, String> myUser = null;
			for (HashMap<String, String> u : usersList) {
				if (u.get("name").equals(marker.getTitle())) {
					myUser = u;
				}
			}
			bundle.putSerializable("HashMap", myUser);
			bundle.putDouble("geo_lat", usersHashMap.get(marker).getGeoLat());
			bundle.putDouble("geo_lon", usersHashMap.get(marker).getGeoLon());
		} else if (tasksHashMap != null && tasksHashMap.containsKey(marker)) {
			f1 = new TaskDetailsFragment();
			HashMap<String, String> myTask = null;
			for (HashMap<String, String> t : tasksList) {
				if (t.get("title").equals(marker.getTitle())) {
					myTask = t;
				}
			}
			bundle.putSerializable("HashMap", myTask);
			bundle.putDouble("geo_lat", tasksHashMap.get(marker).getGeoLat());
			bundle.putDouble("geo_lon", tasksHashMap.get(marker).getGeoLon());
			bundle.putLong("id", tasksHashMap.get(marker).getId());
		}
		f1.setArguments(bundle);
		FragmentTransaction ft = fragment.getFragmentManager()
				.beginTransaction();
		ft.replace(fragment.getId(), f1);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();

	}

}