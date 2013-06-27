package com.datastore;

import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.tldr.com.tldr.userinfoendpoint.Userinfoendpoint;
import com.tldr.com.tldr.userinfoendpoint.model.CollectionResponseUserInfo;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.tools.CloudEndpointUtils;

public class UserInfoDatastore extends BaseDatastore{
	
	private Userinfoendpoint service;

	
	public UserInfoDatastore(DatastoreResultHandler context, GoogleAccountCredential credential) {
		super(context);
		Userinfoendpoint.Builder builder = new Userinfoendpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new GsonFactory(),
				credential);
		service = CloudEndpointUtils.updateBuilder(builder).build();
	}
	
	public void registerUser(UserInfo user){
		new RegisterUserInfoTask().execute(user);
	}
	
	public void getNearbyUsers(){
		new GetNearbyUsersTask().execute();
	}
	
	public void updateUser(UserInfo user) {
		new UpdateUserInfoTask().execute(user);
	}
	
	private class UpdateUserInfoTask extends
	AsyncTask<UserInfo, UserInfo, UserInfo> {
	@Override
	protected UserInfo doInBackground(UserInfo... userinfo) {
		UserInfo registeredUser = null;
		try {
			registeredUser = service.updateUserInfo((userinfo[0]))
					.execute();
			return registeredUser;
		} catch (IOException e) {
			Log.d("TLDR", e.getMessage(), e);
		}
		return registeredUser;
	}
	
	@Override
	protected void onPostExecute(UserInfo registeredUser) {
			if(context!=null)
				context.handleRequestResult(REQUEST_USERINFO_UPDATEUSER, registeredUser);
	
	}
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
		if(context!=null)
			context.handleRequestResult(REQUEST_USERINFO_REGISTER, registeredUser);
	
	}
}
	
	private class GetNearbyUsersTask extends
	AsyncTask<Void, Void, CollectionResponseUserInfo> {
	@Override
	protected CollectionResponseUserInfo doInBackground(Void... v) {
		CollectionResponseUserInfo registeredUser = null;
		try {
			registeredUser = service.listUserInfo()
					.execute();
			return registeredUser;
		} catch (IOException e) {
			Log.d("TLDR", e.getMessage(), e);
		}
		return registeredUser;
	}
	
	@Override
	protected void onPostExecute(CollectionResponseUserInfo registeredUser) {
		if(context!=null)
			context.handleRequestResult(REQUEST_USERINFO_NEARBYUSERS, registeredUser.getItems());
	
	}
}
	
}
