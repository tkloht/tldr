package com.tldr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.auth.AccountHelper;
import com.datastore.TaskDatastore;

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

import com.datastore.BaseDatastore;
import com.datastore.DatastoreResultHandler;
import com.datastore.UserInfoDatastore;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;

public class CommunityFragment extends Fragment implements DatastoreResultHandler{
	
	private TabHost mTabHost;	
	private ListView nearbyFriendsView;
	private int currentView;
	private final static String TAG_NAME="name";
	private final static String TAG_DISTANCE="distance";
	private UserInfoDatastore userInfoDatastore;
	
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
		AccountHelper auth = new AccountHelper(getActivity());
		userInfoDatastore= new UserInfoDatastore(this, auth.getCredential());
		return v;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		nearbyFriendsView=(ListView) view.findViewById(R.id.list_nearby_people);
		currentView=R.id.list_nearby_people;
        // Create a progress bar to display while the list loads
		userInfoDatastore.getNearbyUsers();
	}
	
	@Override
	public void handleRequestResult(int requestId, Object result) {
		// TODO Auto-generated method stub
		if(requestId==BaseDatastore.REQUEST_USERINFO_NEARBYUSERS){
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
			List<UserInfo> users=(List<UserInfo>) result;
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			for(UserInfo u:users)
			{
				if (!u.getUsername().equals(GlobalData.getCurrentUser().getUsername())){
					HashMap<String, String> newMap= new HashMap<String, String>();
					newMap.put(TAG_NAME, u.getUsername());
					float[] distance = new float[]{0.0f};
					Log.d("TLDR", "User Position: "+u.getGeoLat()+" "+u.getGeoLon());
					if(current!=null){
						Location.distanceBetween(current.getLatitude(), current.getLongitude(), u.getGeoLat(), u.getGeoLon(), distance);
					}
					int dist = Math.round(distance[0]);
					newMap.put(TAG_DISTANCE, (dist<1000? dist+"m" : "~"+(dist/1000)+"km"));
					list.add(newMap);
				}
			}
			if(activity !=null){
		        ListAdapter adapter = new SimpleAdapter(
		        		activity, list,
		                R.layout.layout_nearby_people_listitem, new String[] { TAG_NAME, TAG_DISTANCE },
		                new int[] { R.id.title, R.id.distance});
		        // updating listview
		        nearbyFriendsView.setAdapter(adapter);
			}
		}
	}
	
	

}
