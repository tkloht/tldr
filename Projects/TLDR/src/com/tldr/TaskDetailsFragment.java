package com.tldr;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth.AccountHelper;
import com.datastore.DatastoreResultHandler;
import com.datastore.UserInfoDatastore;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.gamelogic.GoalStructure;
import com.tldr.taskendpoint.model.Task;
import com.ui.SimpleCheckBoxListAdapter;

public class TaskDetailsFragment extends Fragment implements DatastoreResultHandler{
	HashMap<String, String> hashMap;
	private MapView mMapView;
	private GoogleMap mMap;
	private Bundle mBundle;
	private Marker taskMarker;
	private double geo_lat;
	private double geo_lon;
	private Long id;
	private ListView goalList;
	private AccountHelper auth;
	private UserInfoDatastore datastore; 
	private Task selected;
	private List<Long> goalIds;
	private SimpleCheckBoxListAdapter goalListAdapter;

	
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
		
		auth = new AccountHelper(getActivity()); 	 
		datastore = new UserInfoDatastore(this, auth.getCredential());
		final Button button = (Button) v.findViewById(R.id.accept_button);
	    button.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View v) {
	        	 UserInfo user = GlobalData.getCurrentUser();
	        	 List<Long> acceptedTasks = user.getAcceptedTasks();
	        	 if (acceptedTasks == null) {
	        		 acceptedTasks = new ArrayList<Long>();
	        	 }
	        	 acceptedTasks.add(id);
	        	 user.setAcceptedTasks(acceptedTasks);
	        	 GlobalData.setCurrentUser(user);
	        	 
	        	 datastore.updateUser(user);
	        	 Map<Long, GoalStructure> goals=GlobalData.getAllGoals();

	        	 for(Long gid:goalIds){
		        	 GlobalData.getGoalRegister().addGoal(goals.get(gid));
	        	 }
	             Toast.makeText(getActivity(), "Accepted Task, id:" + id  , Toast.LENGTH_LONG).show();
	             
	         }
	     });
		
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
		
		Map<Integer, LatLng> points = new HashMap<Integer, LatLng>();
		List<Task> allTasks=GlobalData.getAllTasks();
		Map<Long, GoalStructure> goals=GlobalData.getAllGoals();
		List<Map<String, String>> listObjects = new ArrayList<Map<String,String>>();
		List<Boolean> checked= new ArrayList<Boolean>();
		if(id!=null){
			for( Task t:allTasks){
				if(id.equals(t.getId())){
					selected = t;
				}
			}
			if(selected!=null){
				goalIds = selected.getGoals();
				for(Long gid:goalIds){
					boolean add=true;
					HashMap<String, String> singleListItem = new HashMap<String, String>();
					GoalStructure gs = goals.get(gid);
					String desc=gs.getDescription();
					int sequenceNr=0;
					if(desc.contains("##")){
						sequenceNr=Integer.parseInt(desc.split("##")[0]);
						desc=desc.split("##")[1];
						
					}
					if(sequenceNr>0)
						add=false;
					singleListItem.put("description", desc);
					Map<String, Object> goalParse = goals.get(gid).getJsonParse();
					ArrayList<Map<String, String>> conditions= (ArrayList<Map<String, String>>) goalParse.get("conditions");
					for(Map<String, String> condition:conditions){
						if(condition.get("data").equals("polyline_point")){
							String latLon=condition.get("value");
							String[] coords = latLon.split(",");
							LatLng point=new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
							points.put(sequenceNr, point);
							}
					}
					
					
					if(add){
						listObjects.add(singleListItem);
					
						if(GlobalData.getCurrentUser().getFinishedGoals()!=null&&GlobalData.getCurrentUser().getFinishedGoals().contains(gid)){
							checked.add(true);
						}
						else
							checked.add(false);
					}
				}
			}
		}
		
        goalListAdapter = new SimpleCheckBoxListAdapter(
        		getActivity(),listObjects,
                R.layout.goal_listitem, new String[] { "description" },
                new int[] { R.id.goal_desc}, checked.toArray(new Boolean[]{true}));
        // updating listview

        goalList.setAdapter(goalListAdapter);
        
        //SetUpMatPolylineIfNeeded
        if(points.size()>0){
        	PolylineOptions po = new PolylineOptions();
        	for(int i=0; points.containsKey(i); i++)
        		po.add(points.get(i));
        	po.color(Color.BLUE);
        	mMap.addPolyline(po);
        }
		
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
		mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
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




	@Override
	public void handleRequestResult(int requestId, Object result) {
		// TODO Auto-generated method stub
		
	}

}
