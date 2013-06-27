package com.tldr;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tldr.gamelogic.GoalStructure;
import com.tldr.taskendpoint.model.Task;

public class TaskDetailsFragment extends Fragment{
	HashMap<String, String> hashMap;
	private MapView mMapView;
	private GoogleMap mMap;
	private Bundle mBundle;
	private Marker taskMarker;
	private double geo_lat;
	private double geo_lon;
	private Long id;
	
	private ListView goalList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = new View(getActivity());
		v = inflater.inflate(R.layout.task_details_layout, container, false);

		Bundle bundle = this.getArguments();
		this.hashMap = (HashMap<String, String>) bundle.getSerializable("HashMap");
		this.geo_lat = bundle.getDouble("geo_lat");
		this.geo_lon = bundle.getDouble("geo_lon");
		id=bundle.getLong("id");
		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			Log.w("TLDR", e);
		}
		mMapView = (MapView) v.findViewById(R.id.details_map);
		mMapView.onCreate(mBundle);
		setUpMapIfNeeded(v);
		
		return v;
	}
	
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        TextView task_title = (TextView) getView().findViewById(R.id.task_title);
        TextView task_distance = (TextView) getView().findViewById(R.id.task_distance);
        TextView task_description = (TextView) getView().findViewById(R.id.task_description);
        task_title.setText(hashMap.get("title"));
        task_distance.setText(hashMap.get("distance"));
        task_description.setText(hashMap.get("desc"));
        
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		mMapView.requestFocus();
		
		goalList =(ListView) view.findViewById(R.id.goal_list);
		int orientation=getResources().getConfiguration().orientation;
		if(orientation==Configuration.ORIENTATION_PORTRAIT){
			mMapView.setVisibility(View.GONE);
		}
		
		
		fillGoalList();
		
		
		
	}
	
	private void fillGoalList(){
		List<Task> allTasks=GlobalData.getAllTasks();
		Map<Long, GoalStructure> goals=GlobalData.getAllGoals();
		Task selected=null;
		List<Map<String, String>> listObjects = new ArrayList<Map<String,String>>();
		if(id!=null){
			for( Task t:allTasks){
				if(id.equals(t.getId())){
					selected = t;
				}
			}
			if(selected!=null){
				List<Long> goalIds = selected.getGoals();
				for(Long gid:goalIds){
					HashMap<String, String> singleListItem = new HashMap<String, String>();
					singleListItem.put("description", goals.get(gid).getDescription());
					listObjects.add(singleListItem);
				}
			}
		}
		
        ListAdapter adapter = new SimpleAdapter(
        		getActivity(),listObjects,
                R.layout.goal_listitem, new String[] { "description" },
                new int[] { R.id.goal_desc});
        // updating listview
        goalList.setAdapter(adapter);
		
	}
	
	private void setUpMapIfNeeded(View inflatedView) {
		if (mMap == null) {
			mMap = ((MapView) inflatedView.findViewById(R.id.details_map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}
	
	
	private void setUpMap() {
		mMap.setIndoorEnabled(false);
		mMap.setTrafficEnabled(false);
		mMap.getUiSettings().setAllGesturesEnabled(false);
		mMap.getUiSettings().setCompassEnabled(false);
		mMap.getUiSettings().setZoomControlsEnabled(false);
		mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(geo_lat, geo_lon)));
		this.taskMarker = mMap.addMarker(new MarkerOptions().position(
					new LatLng(geo_lat, geo_lon))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.target)));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

}
