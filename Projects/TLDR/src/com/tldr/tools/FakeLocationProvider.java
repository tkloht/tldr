package com.tldr.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author manuschmanu This class will Call "OnLocationChanged" with given Fake
 *         Data in an given Interval in milliseconds
 */
public class FakeLocationProvider extends Thread {

	private long interval;
	private List<LatLng> locations;
	private LocationManager mLocationManager;
	private static FakeLocationProvider singleton;
	private LocationListener listener;
	public static final String PROVIDER_NAME = "FAKE_LOCATION_PROVIDER";
	private Activity context;
	private Location next;
	private boolean needsReset=false;
	public static FakeLocationProvider getFakeLocationProvider(long interval, LocationManager mLocationManager, LocationListener listener, Activity context){
		if(singleton==null)
		{
			singleton=new FakeLocationProvider(interval, mLocationManager, listener, context);
			
		}
		return singleton;
	}
	
	private FakeLocationProvider(long interval,
			 LocationManager mLocationManager, LocationListener listener, Activity context) {
		this.interval = interval;
		this.mLocationManager = mLocationManager;
		if (mLocationManager.getProvider(PROVIDER_NAME) != null) {
			  mLocationManager.removeTestProvider(PROVIDER_NAME);
			}
		mLocationManager.addTestProvider(PROVIDER_NAME, false, false,
	            false, false, true, true, true, 0, 5);
	    mLocationManager.setTestProviderEnabled(PROVIDER_NAME, true);
	    this.listener=listener;
	    this.context=context;
	    needsReset=false;
	}
	
	

	public List<LatLng> getLocations() {
		return locations;
	}


	public void reset(){
		List<LatLng> locations=singleton.locations;
		if(needsReset)
			singleton=new FakeLocationProvider(interval, mLocationManager, listener, context);
		singleton.setLocations(locations);
		
		needsReset=false;
	}

	public void setLocations(List<LatLng> locations) {
		this.locations = locations;
	}

	public void startFakeLocations(){
		needsReset=true;
		singleton.start();
		
	}
	
	@Override
	public void run() {
		try {
			for (LatLng location : locations) {
				Thread.sleep(interval);
				next = new Location(PROVIDER_NAME);
				next.setLatitude(location.latitude);
				next.setLongitude(location.longitude);
				next.setTime(System.currentTimeMillis());
				next.setAccuracy(100);
				Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
				if (locationJellyBeanFixMethod != null) {
				   locationJellyBeanFixMethod.invoke(next);
				}
				
				mLocationManager.setTestProviderLocation(PROVIDER_NAME, next);
//				context.runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						listener.onLocationChanged(next);
//					}
//				});
				
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
