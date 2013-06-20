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

}
