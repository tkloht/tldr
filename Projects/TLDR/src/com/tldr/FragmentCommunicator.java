package com.tldr;

public interface FragmentCommunicator {
	public final static int SPEECH_REQUEST_MESSAGE=1;
	
	public void receiveMessage(int messageID, Object data);
	
}
