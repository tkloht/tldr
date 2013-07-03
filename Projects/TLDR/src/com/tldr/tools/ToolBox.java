package com.tldr.tools;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextSwitcher;

import com.google.android.gms.maps.model.LatLng;
import com.tldr.GlobalData;
import com.tldr.MapFragment.AutoCompletionMarker;

public final class ToolBox {
	public final static String TAG_REAL_DISTANCE="real_distance";
	
	
	public static Location receiveCurrentLocation(Activity context){
		Location current = GlobalData.getLastknownPosition();
		if(current==null)
		{
			LocationManager locationManager = (LocationManager) context.getSystemService(
					Context.LOCATION_SERVICE);
			if(locationManager!= null){
				current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if(current == null){
					current = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
			}	
		}
		return current;
	}
	
	public static void showErrorMessage(EditText view, String message) {
		int ecolor = Color.RED; // whatever color you want
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder;
		ssbuilder = new SpannableStringBuilder(message);
		ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
		view.setError(ssbuilder);
	}
	
	public static SpannableStringBuilder buildColoredString(String text, int color){
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(color);
		SpannableStringBuilder ssbuilder;
		ssbuilder = new SpannableStringBuilder(text);
		return ssbuilder;
	}
	
	public static void animateTextSwitcher(TextSwitcher txtSwitch, String txt, Activity context) {
		new TextSwitchAnimator(txtSwitch, txt, context).start();
	}

	
	public static void showAlert(Activity context, String title, String message, String closeButtonText, OnClickListener l) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setNegativeButton(closeButtonText,l);
        AlertDialog alert = builder.create();
        alert.show();
	}
	
	public static void addInRealDistanceOrder(List<HashMap<String, String>> list, HashMap<String, String> element){
		float new_distance=Float.parseFloat(element.get("real_distance"));
		boolean added=false;
		for(int i=0; i<list.size(); i++){
			float d1=Float.parseFloat(list.get(i).get("real_distance"));
			if(!added&&new_distance<d1){
				list.add(i, element);
				added=true;
			}
		}
		if(!added)
			list.add(element);
	}
	public static void addInRealDistanceOrder(List<AutoCompletionMarker> list, AutoCompletionMarker acm){
		float new_distance=acm.getDistance();
		boolean added=false;
		for(int i=0; i<list.size(); i++){
			float d1=list.get(i).getDistance();
			if(!added&&new_distance<d1){
				list.add(i, acm);
				added=true;
			}
		}
		if(!added)
			list.add(acm);
	}
	
	public static LatLng locationFromString(String latLon){
		String[] coords = latLon.split(";");
		LatLng point=new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
		return point;
	}
	
}
