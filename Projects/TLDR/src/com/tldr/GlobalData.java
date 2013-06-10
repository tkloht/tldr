package com.tldr;

import android.location.Location;

public class GlobalData {
	
	private static Location lastknownPosition = null;

	public static Location getLastknownPosition() {
		return lastknownPosition;
	}

	public static void setLastknownPosition(Location lastknown) {
		lastknownPosition = lastknown;
	}

}
