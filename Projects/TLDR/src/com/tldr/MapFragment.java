package com.tldr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.auth.AccountHelper;
import com.datastore.BaseDatastore;
import com.datastore.DatastoreResultHandler;
import com.datastore.TaskDatastore;
import com.datastore.UserInfoDatastore;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.taskendpoint.model.Task;
import com.tldr.tools.ToolBox;

public class MapFragment extends Fragment implements LocationListener,
		FragmentCommunicator, DatastoreResultHandler {

	private MapView mMapView;
	private GoogleMap mMap;
	private Bundle mBundle;
	private OnLocationChangedListener mListener;
	private LocationManager locationManager;

	private TaskDatastore taskDatastore;
	private UserInfoDatastore userDatastore;
	private AccountHelper auth;

	private List<Marker> taskMarkers;
	private List<Marker> userMarkers;
	private final static int NUM_MARKERS = 10;

	public void initialize() {
		Bundle args = getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		auth = new AccountHelper(getActivity());
		taskDatastore = new TaskDatastore(this, auth.getCredential());
		userDatastore = new UserInfoDatastore(this, auth.getCredential());
		View v = new View(getActivity());
		v = inflater.inflate(R.layout.map_layout, container, false);

		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			Log.w("TLDR", e);
		}

		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		if (locationManager != null) {
			boolean gpsIsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkIsEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (gpsIsEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 5000L, 10F, this);
				GlobalData.setLastknownPosition(locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER));
			} else if (networkIsEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
				GlobalData
						.setLastknownPosition(locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
			} else {
				// Show an error dialog that GPS is disabled...
			}
		} else {
			// Show some generic error dialog because something must have gone
			// wrong with location manager.
		}

		mMapView = (MapView) v.findViewById(R.id.map);
		mMapView.onCreate(mBundle);
		setUpMapIfNeeded(v);
		Location lastKnown = GlobalData.getLastknownPosition();
		if (lastKnown != null) {
			UserInfo current = GlobalData.getCurrentUser();
			if (current != null) {
				userDatastore.updateUser(current.setGeoLat(
						lastKnown.getLatitude()).setGeoLon(
						lastKnown.getLongitude()));
			}
		}
		return v;
	}

	// DemoCode
	private void generateMarkers(Location location) {
		if (location != null) {
			LatLng newPos;
			Random rand = new Random();
			for (int i = 0; i <= NUM_MARKERS; i++) {
				newPos = new LatLng(location.getLatitude()
						+ (rand.nextBoolean() ? -1 : 1) * rand.nextDouble()
						/ 100, location.getLongitude()
						+ (rand.nextBoolean() ? -1 : 1) * rand.nextDouble()
						/ 100);

				Marker newMarker = mMap.addMarker(new MarkerOptions()
						.position(newPos)
						.title("Quest" + i)
						.snippet("Quest " + i + " ist super")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.target)));
				this.taskMarkers.add(newMarker);
			}
		}

	}

	private void acceptAllNearbyTasks() {
		for (Marker m : taskMarkers) {
			m.setIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.tldr_target_sm));
		}
	}

	private void rejectAllNearbyTasks() {
		for (Marker m : taskMarkers) {
			m.setIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.tldr_task_sm));
		}
	}

	private void deleteAllTasks() {
		for (Marker m : taskMarkers) {
			m.remove();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;

	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (mMap == null) {
			mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
		// mMap.setLocationSource(null);
	}

	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		if (GlobalData.getLastknownPosition() != null) {
			// generateMarkers(lastknown);
			flyTo(GlobalData.getLastknownPosition());
		}
		taskMarkers = new ArrayList<Marker>();
		userMarkers = new ArrayList<Marker>();
		taskDatastore.getNearbyTasks();
		userDatastore.getNearbyUsers();

	}

	private void flyTo(Location location) {
		if (location != null) {
			mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
			mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(
					location.getLatitude(), location.getLongitude())));
		}
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
	public void onLocationChanged(Location location) {
		// flyTo(location);
		GlobalData.setLastknownPosition(location);
		if (GlobalData.getCurrentUser() != null) {
			userDatastore.updateUser(GlobalData.getCurrentUser()
					.setGeoLon(location.getLongitude())
					.setGeoLat(location.getLatitude()));
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessage(int messageID, Object data) {
		// TODO Auto-generated method stub
		if (messageID == SPEECH_REQUEST_MESSAGE) {
			String message = (String) data;
			Log.d("TLDR", "Speech Input: " + message);
			message = message.toLowerCase();
			if (message.contains("all")
					&& (message.contains("tasks") || message.contains("task")
							|| message.contains("alle") || message
								.contains("missions"))) {
				if ((message.contains("accept")
						|| message.contains("akzeptieren") || message
							.contains("except"))) {
					acceptAllNearbyTasks();
				} else if (message.contains("reject")
						|| message.contains("decline")) {
					rejectAllNearbyTasks();
				} else if (message.contains("delete")) {
					deleteAllTasks();
				}

			} else
				ToolBox.showAlert(this.getActivity(), "Speech Result", message,
						"Dismiss", null);

		}
	}

	@Override
	public void handleRequestResult(int requestId, Object result) {
		// TODO Auto-generated method stub
		if (requestId == BaseDatastore.REQUEST_TASK_FETCHNEARBY) {
			List<Task> tasks = (List<Task>) result;
			if (tasks != null) {
				for (Task t : tasks) {
					Marker newMarker = mMap.addMarker(new MarkerOptions()
							.position(new LatLng(t.getGeoLat(), t.getGeoLon()))
							.title(t.getTitle())
							.snippet(
									(t.getDescription().length() < 30 ? t
											.getDescription() : t
											.getDescription().substring(0, 29)
											+ ".."))
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.tldr_task_sm)));
					taskMarkers.add(newMarker);
				}
			}
		}
		if (requestId == BaseDatastore.REQUEST_USERINFO_UPDATEUSER) {
			UserInfo userResult = (UserInfo) result;
			if (userResult != null) {
				GlobalData.setCurrentUser(userResult);
			}
		}
		if(requestId==BaseDatastore.REQUEST_USERINFO_NEARBYUSERS) {
			List<UserInfo> users = (List<UserInfo>) result;
			if(users!=null){
				for(UserInfo u: users){
					if(u.getId()!=GlobalData.getCurrentUser().getId()&&u.getGeoLat()!=0.0 && u.getGeoLon() !=0.0){
						Marker newMarker = mMap.addMarker(new MarkerOptions()
						.position(new LatLng(u.getGeoLat(), u.getGeoLon()))
						.title(u.getUsername())
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.agent)));
						userMarkers.add(newMarker);
					}
				}
			}
		}
	}
}
