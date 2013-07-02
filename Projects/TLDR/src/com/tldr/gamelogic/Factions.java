package com.tldr.gamelogic;

import com.tldr.GlobalData;

import com.tldr.R;
import android.widget.ImageView;

public class Factions {

	public static final int FRACTION_DEFIANCE = 1;
	public static final int FRACTION_MINISTRY_OF_FREEDOM = 2;

	public static void setMenueButton(ImageView fractionbuton) {
		if (GlobalData.getCurrentUser() != null) {
			switch (GlobalData.getCurrentUser().getFaction()) {
			case FRACTION_DEFIANCE:
				fractionbuton.setImageResource(R.drawable.tldr_button_def);
				break;
			case FRACTION_MINISTRY_OF_FREEDOM:
				fractionbuton.setImageResource(R.drawable.tldr_button_mof);
				break;

			default:
				break;
			}
		}

	}

	public static void setDetailLogo(ImageView factionbanner,
			ImageView factionlogo) {
		if (GlobalData.getCurrentUser() != null) {
			switch (GlobalData.getCurrentUser().getFaction()) {
			case FRACTION_DEFIANCE:
				factionbanner.setImageResource(R.drawable.tldr_banner_def);
				factionlogo.setImageResource(R.drawable.tldr_def_mini);
				break;
			case FRACTION_MINISTRY_OF_FREEDOM:
				factionbanner.setImageResource(R.drawable.tldr_banner_mof);
				factionlogo.setImageResource(R.drawable.tldr_mof_mini);
				break;

			default:
				break;
			}
		}

	}
	
	public static void setProfileFractionLogo(ImageView factionlogo) {
		if (GlobalData.getCurrentUser() != null) {
			switch (GlobalData.getCurrentUser().getFaction()) {
			case FRACTION_DEFIANCE:
				factionlogo.setImageResource(R.drawable.tldr_def_mini);
				break;
			case FRACTION_MINISTRY_OF_FREEDOM:
				factionlogo.setImageResource(R.drawable.tldr_mof_mini);
				break;

			default:
				break;
			}
		}

	}

}
