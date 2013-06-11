package com.tldr;

import com.tldr.messageEndpoint.MessageEndpoint;

import android.location.Location;

public class GlobalData {
	
	private static Location lastknownPosition = null;
	private static MessageEndpoint messageEndpoint = null;

	public static Location getLastknownPosition() {
		return lastknownPosition;
	}

	public static void setLastknownPosition(Location lastknown) {
		lastknownPosition = lastknown;
	}

	public static MessageEndpoint getMessageEndpoint() {
		return messageEndpoint;
	}

	public static void setMessageEndpoint(MessageEndpoint endpoint) {
		messageEndpoint = endpoint;
	}

}
