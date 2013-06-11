package com.tldr;

import android.location.Location;

import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.messageEndpoint.MessageEndpoint;

public class GlobalData {
	
	private static Location lastknownPosition = null;
	private static MessageEndpoint messageEndpoint = null;
	private static UserInfo currentUser = null;

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

	public static UserInfo getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(UserInfo currentUser) {
		GlobalData.currentUser = currentUser;
	}

	
	
	
}
