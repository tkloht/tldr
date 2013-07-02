package com.tldr;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.gamelogic.Factions;
import com.tldr.taskendpoint.model.Task;
import com.tldr.tools.ToolBox;

import android.R.fraction;
import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FactionDetailsFragment extends Fragment {
	private ImageView factionbanner;
	private ImageView factionlogo;
	private ListView newsListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = new View(getActivity());
		v = inflater.inflate(R.layout.faction_details_layout, container, false);

		Bundle bundle = this.getArguments();

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		newsListView=(ListView) view.findViewById(R.id.list_fraction_news);
		factionbanner = (ImageView) getActivity().findViewById(
				R.id.fractionDetailBanner);
		factionlogo = (ImageView) getActivity().findViewById(
				R.id.fractionDetailLogo);
		Factions.setDetailLogo(factionbanner, factionlogo);
		
		List<UserInfo> users = GlobalData.getAllUsers();
		int members = 0;
		Integer fraction = GlobalData.getCurrentUser().getFaction();
		for (UserInfo u: users){
			if (u.getFaction() == fraction){
				members++;
			}
		}
		((TextView) getActivity().findViewById(R.id.memberNumber)).setText(members);
		
		
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		List<UserInfo> fractionUsers = GlobalData.getFractionUsers(GlobalData.getCurrentUser().getFaction());
		for(UserInfo u: fractionUsers)
		{
			
			HashMap<String, String> newMap= new HashMap<String, String>();
			newMap.put("username", u.getUsername());
		}
		
        ListAdapter adapter = new SimpleAdapter(
        		getActivity(), list,
                R.layout.layout_nearby_listitem, new String[] { "username", "1", "2" },
                new int[] { R.id.title, R.id.description, R.id.distance});
        // updating listview
        newsListView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
