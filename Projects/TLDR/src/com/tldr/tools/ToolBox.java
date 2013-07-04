package com.tldr.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.android.gms.maps.model.PolylineOptions;
import com.tldr.GlobalData;
import com.tldr.MapFragment.AutoCompletionMarker;
import com.tldr.gamelogic.GoalStructure;

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
		String[] coords;
		if(latLon.contains(";"))
			coords = latLon.split(";");
		else
			coords = latLon.split(",");
		LatLng point=new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
		return point;
	}
	
	public static List<PolylineOptions> generatePolylineFromIDs(List<Long> goals, List<Long> finishedGoals){
		Map<Long, GoalStructure> map=GlobalData.getAllGoals();
		List<GoalStructure> gs_list= new ArrayList<GoalStructure>();
		for(Long g: goals){
			gs_list.add(map.get(g));
		}
		return generatePolyline(gs_list, finishedGoals);
	}
	public static List<PolylineOptions> generatePolyline(List<GoalStructure> goals, List<Long> finishedGoals){
		PolylineOptions line_unfinished = new PolylineOptions();
		PolylineOptions line_finished = new PolylineOptions();
		List<PolylineOptions> lReturn = new ArrayList<PolylineOptions>();
		HashMap<Integer, LatLng> lineStructure= new HashMap<Integer, LatLng>();
		if(finishedGoals==null){
			finishedGoals= new ArrayList<Long>();
		}
		for(GoalStructure goal:goals){
			int seq;
			List<Map<String, String>> conditions = (List<Map<String, String>>) goal.getJsonParse().get("conditions");
			if(goal.getDescription().contains("##")){
				seq=Integer.parseInt(goal.getDescription().split("##")[0]);
				seq++;
			}
			else
				seq=1;
			if(conditions!=null){
				for(Map<String, String> cond:conditions){
					if(cond.get("data").equals(GoalStructure.CONDITION_TYPE_POLYLINE_POINT)){
						if(finishedGoals.contains(goal.getId())){
							lineStructure.put(seq, ToolBox.locationFromString(cond.get("value")));
						}
						else
						{
							lineStructure.put(-seq, ToolBox.locationFromString(cond.get("value")));
						}
					}
				}
			}
		}
		//Compute lines
		LatLng lastLocation=null;
		boolean lastFinished=false;
		for(int i=1; lineStructure.containsKey(i)||lineStructure.containsKey(-i); i++){
			if(i==1){ //do not draw
				if(lineStructure.containsKey(i)){
					lastLocation=lineStructure.get(i);
					lastFinished=true;
				}
				else
				{
					lastLocation=lineStructure.get(-i);
					lastFinished=false;
				}
			}
			else
			{
				PolylineOptions nextLine = new PolylineOptions();
				if(lineStructure.containsKey(i)){
					if(lastFinished)
						nextLine.color(Color.GREEN);
					else
						nextLine.color(Color.BLUE);
					nextLine.add(lastLocation);
					nextLine.add(lineStructure.get(i));
					lReturn.add(nextLine);
					lastLocation=lineStructure.get(i);
					lastFinished=true;
				}
				else
				{
					nextLine.color(Color.BLUE);
					nextLine.add(lastLocation);
					nextLine.add(lineStructure.get(-i));
					lReturn.add(nextLine);
					lastLocation=lineStructure.get(-i);
					lastFinished=false;
				}	
			}
		}
		
		return lReturn;
		
	}
	
}
