package com.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleAdapter;

import com.tldr.R;

public class SimpleCheckBoxListAdapter extends SimpleAdapter{

	private Boolean[] checked;
	
	
	public SimpleCheckBoxListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to, Boolean[] checked) {
		super(context, data, resource, from, to);
		this.checked=checked;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = super.getView(position, convertView, parent);
		if(v!=null){
			CheckBox cb = (CheckBox) v.findViewById(R.id.list_goal_checkbox);
			if(checked[position])
				cb.setChecked(true);
			
		}
		
		return v;
	}
	
	
	

}
