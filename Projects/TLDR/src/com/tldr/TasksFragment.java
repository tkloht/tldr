package com.tldr;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import com.auth.AccountHelper;
import com.datastore.BaseDatastore;
import com.datastore.DatastoreResultHandler;
import com.datastore.TaskDatastore;
import com.tldr.taskendpoint.model.Task;
import com.tldr.tools.ToolBox;

public class TasksFragment extends Fragment implements DatastoreResultHandler{
	
	private TabHost mTabHost;
	private ListView nearbyListView;
	private ListView acceptedListView;
	private ListView completedListView;
	private int currentView;
	private final static String TAG_TITLE="title";
	private final static String TAG_DESCRIPTION="desc";
	private final static String TAG_DISTANCE="distance";
	private final int TAG_TASK = 1;
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
		completedListView=(ListView) view.findViewById(R.id.list_completed_tasks);
		acceptedListView=(ListView) view.findViewById(R.id.list_accepted_tasks);
		currentView=R.id.list_nearby_tasks;
		Location current = ToolBox.receiveCurrentLocation(getActivity());
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		List<Task> tasks= GlobalData.getAllTasks();
		List<Task> completedTasks=GlobalData.getCompletedTasks();
		for(Task t:completedTasks)
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
			DecimalFormat df = new DecimalFormat("#.#");
			newMap.put(TAG_DISTANCE, (dist<1000? dist+"m" : "~"+df.format((dist/1000))+"km"));
			newMap.put(ToolBox.TAG_REAL_DISTANCE, dist+"");
			ToolBox.addInRealDistanceOrder(list, newMap);
		}
		
        ListAdapter adapter = new SimpleAdapter(
        		getActivity(), list,
                R.layout.layout_nearby_listitem, new String[] { TAG_TITLE, TAG_DESCRIPTION, TAG_DISTANCE },
                new int[] { R.id.title, R.id.description, R.id.distance});
        // updating listview
        completedListView.setAdapter(adapter);
        
        MyOnClickListener completedOCL = new MyOnClickListener(TAG_TASK, list, completedTasks, null, this);
        completedListView.setOnItemClickListener(completedOCL);


        List<HashMap<String, String>> list2 = new ArrayList<HashMap<String, String>>();
		List<Task> acceptedTasks=GlobalData.getAcceptedTasks();
		for(Task t:acceptedTasks)
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
			DecimalFormat df = new DecimalFormat("#.#");
			newMap.put(TAG_DISTANCE, (dist<1000? dist+"m" : "~"+df.format((dist/1000))+"km"));
			newMap.put(ToolBox.TAG_REAL_DISTANCE, dist+"");
			ToolBox.addInRealDistanceOrder(list2, newMap);
		}
		
        ListAdapter adapter2 = new SimpleAdapter(
        		getActivity(), list2,
                R.layout.layout_nearby_listitem, new String[] { TAG_TITLE, TAG_DESCRIPTION, TAG_DISTANCE },
                new int[] { R.id.title, R.id.description, R.id.distance});
        // updating listview
        acceptedListView.setAdapter(adapter2);
        
        MyOnClickListener acceptedOCL = new MyOnClickListener(TAG_TASK, list2, acceptedTasks, null, this);
        acceptedListView.setOnItemClickListener(acceptedOCL);
		
		
		List<HashMap<String, String>> list3 = new ArrayList<HashMap<String, String>>();
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
			DecimalFormat df = new DecimalFormat("#.#");
			newMap.put(TAG_DISTANCE, (dist<1000? dist+"m" : "~"+df.format((dist/1000))+"km"));
			newMap.put(ToolBox.TAG_REAL_DISTANCE, dist+"");
			ToolBox.addInRealDistanceOrder(list3, newMap);
		}
		
        ListAdapter adapter3 = new SimpleAdapter(
        		getActivity(), list3,
                R.layout.layout_nearby_listitem, new String[] { TAG_TITLE, TAG_DESCRIPTION, TAG_DISTANCE },
                new int[] { R.id.title, R.id.description, R.id.distance});
        // updating listview
        nearbyListView.setAdapter(adapter3);
        
        MyOnClickListener nearbyOCL = new MyOnClickListener(TAG_TASK, list3, tasks, null, this);
        nearbyListView.setOnItemClickListener(nearbyOCL);

		
		
		
//		handleRequestResult(BaseDatastore.REQUEST_TASK_FETCHNEARBY, GlobalData.getAllTasks());
		
	}



	@Override
	public void handleRequestResult(int requestId, Object result) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onResume () {
		((HomeActivity) getActivity()).animateMenuIcons(2);
		super.onResume();
	}

}
