package com.tldr;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import com.auth.AccountHelper;
import com.datastore.BaseDatastore;
import com.datastore.DatastoreResultHandler;
import com.datastore.TaskDatastore;
import com.tldr.taskendpoint.model.Task;

public class TasksFragment extends Fragment implements DatastoreResultHandler{
	
	private TabHost mTabHost;
	private ListView nearbyListView;
	private int currentView;
	private final static String TAG_TITLE="title";
	private final static String TAG_DESCRIPTION="desc";
	private final static String TAG_DISTANCE="distance";
	private TaskDatastore taskDatastore;
	
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
		AccountHelper auth = new AccountHelper(getActivity());
		taskDatastore= new TaskDatastore(this, auth.getCredential());
		return v;
	}
	
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		nearbyListView=(ListView) view.findViewById(R.id.list_nearby_tasks);
		currentView=R.id.list_nearby_tasks;
        // Create a progress bar to display while the list loads
		taskDatastore.getNearbyTasks();



        
	}



	@Override
	public void handleRequestResult(int requestId, Object result) {
		// TODO Auto-generated method stub
		if(requestId==BaseDatastore.REQUEST_TASK_FETCHNEARBY){
			Location current = GlobalData.getLastknownPosition();
			Activity activity = getActivity();
			if(activity !=null){ //TODO warum wird activity null? beim rotieren.
				LocationManager locationManager = (LocationManager) activity.getSystemService(
						Context.LOCATION_SERVICE);
				if(locationManager!= null){
					current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if(current == null){
						current = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}
				}
			}
			List<Task> tasks=(List<Task>) result;
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			for(Task t:tasks)
			{
				HashMap<String, String> newMap= new HashMap<String, String>();
				newMap.put(TAG_TITLE, t.getTitle());
				newMap.put(TAG_DESCRIPTION, (t.getDescription().length()<500?t.getDescription():t.getDescription().substring(0, 49)+".."));
				float[] distance = new float[]{0.0f};
				Log.d("TLDR", "Task Position: "+t.getGeoLat()+" "+t.getGeoLon());
				if(current!=null){
					Location.distanceBetween(current.getLatitude(), current.getLongitude(), t.getGeoLat(), t.getGeoLon(), distance);
				}
				int dist = Math.round(distance[0]);
				newMap.put(TAG_DISTANCE, (dist<1000? dist+"m" : "~"+(dist/1000)+"km"));
				list.add(newMap);
			}
			if(activity !=null){
		        ListAdapter adapter = new SimpleAdapter(
		        		activity, list,
		                R.layout.layout_nearby_listitem, new String[] { TAG_TITLE, TAG_DESCRIPTION, TAG_DISTANCE },
		                new int[] { R.id.title, R.id.description, R.id.distance});
		        // updating listview
		        nearbyListView.setAdapter(adapter);
			}
		}
	}
	
	
	
	
	

}
