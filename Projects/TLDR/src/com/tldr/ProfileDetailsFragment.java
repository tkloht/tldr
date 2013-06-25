package com.tldr;



import java.util.HashMap;

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
import android.webkit.WebView.FindListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import com.auth.AccountHelper;
import com.datastore.BaseDatastore;
import com.datastore.DatastoreResultHandler;
import com.datastore.TaskDatastore;
import com.tldr.taskendpoint.model.Task;
import com.tldr.tools.ToolBox;

public class ProfileDetailsFragment extends Fragment{
	private double geo_lat;
	private double geo_lon;
	HashMap<String, String> hashMap;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = new View(getActivity());
		v = inflater.inflate(R.layout.profile_details_layout, container, false);

		Bundle bundle = this.getArguments();
		this.hashMap = (HashMap<String, String>) bundle.getSerializable("HashMap");
		this.geo_lat = bundle.getDouble("geo_lat");
		this.geo_lon = bundle.getDouble("geo_lon");
		
		return v;
	}
	
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        TextView user_name = (TextView) getView().findViewById(R.id.user_name);
        TextView user_distance = (TextView) getView().findViewById(R.id.user_distance);
        user_name.setText(hashMap.get("name"));
        user_distance.setText(hashMap.get("distance"));
	}

}
