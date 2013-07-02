package com.tldr;



import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import com.auth.AccountHelper;
import com.datastore.BaseDatastore;
import com.datastore.DatastoreResultHandler;
import com.datastore.TaskDatastore;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tldr.gamelogic.Factions;
import com.tldr.taskendpoint.model.Task;
import com.tldr.tools.ToolBox;

public class ProfileDetailsFragment extends Fragment{
	private MapView mMapView;
	private GoogleMap mMap;
	private Bundle mBundle;
	private double geo_lat;
	private double geo_lon;
	HashMap<String, String> hashMap;
	private Marker userMarker;
	private ImageView factionlogo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = new View(getActivity());
		v = inflater.inflate(R.layout.profile_details_layout, container, false);

		Bundle bundle = this.getArguments();
		this.hashMap = (HashMap<String, String>) bundle.getSerializable("HashMap");
		this.geo_lat = bundle.getDouble("geo_lat");
		this.geo_lon = bundle.getDouble("geo_lon");
		
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
        TextView user_name = (TextView) getView().findViewById(R.id.user_name);
        TextView user_distance = (TextView) getView().findViewById(R.id.user_distance);
        user_name.setText(hashMap.get("name"));
        user_distance.setText(hashMap.get("distance"));
        
        factionlogo = (ImageView) getActivity().findViewById(R.id.fractionPicture);
        Factions.setProfileFractionLogo(factionlogo);
        
        InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		mMapView.requestFocus();
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
		mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(geo_lat, geo_lon)));
		this.userMarker = mMap.addMarker(new MarkerOptions().position(
					new LatLng(geo_lat, geo_lon))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.agent)));
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
