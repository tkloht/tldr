package com.datastore;

import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.tldr.CloudEndpointUtils;
import com.tldr.com.tldr.userinfoendpoint.Userinfoendpoint;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;

public class UserInfoDatastore {
	
	private DatastoreResultHandler context;
	private Userinfoendpoint service;
	public final static String REQUEST_NAME_REGISTER = "UserInfoDatastore_registerUser";
	
	public UserInfoDatastore(GoogleAccountCredential credential, DatastoreResultHandler context) {
		this.context=context;
		Userinfoendpoint.Builder builder = new Userinfoendpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new GsonFactory(),
				credential);
		service = CloudEndpointUtils.updateBuilder(builder).build();
	}
	
	public void registerUser(UserInfo user){
		new RegisterUserInfoTask().execute(user);
	}
	
	private class RegisterUserInfoTask extends
	AsyncTask<UserInfo, UserInfo, UserInfo> {
	@Override
	protected UserInfo doInBackground(UserInfo... userinfo) {
		UserInfo registeredUser = null;
		try {
			registeredUser = service.registerUserInfo((userinfo[0]))
					.execute();
			return registeredUser;
		} catch (IOException e) {
			Log.d("TLDR", e.getMessage(), e);
		}
		return registeredUser;
	}
	
	@Override
	protected void onPostExecute(UserInfo registeredUser) {
	
			context.handleRequestResult(REQUEST_NAME_REGISTER, registeredUser);
	
	}
}
	
}
