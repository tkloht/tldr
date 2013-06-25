package com.tldr;

import com.tldr.gamelogic.Factions;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FactionDetailsFragment extends Fragment {
	private ImageView factionbanner;
	private ImageView factionlogo;

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
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		factionbanner = (ImageView) getActivity().findViewById(
				R.id.fractionDetailBanner);
		factionlogo = (ImageView) getActivity().findViewById(
				R.id.fractionDetailLogo);
		Factions.setDetailLogo(factionbanner, factionlogo);

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
