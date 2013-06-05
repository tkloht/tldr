package com.auth;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class AccountHelper {
	private Activity context;
	private SharedPreferences settings;
	private GoogleAccountCredential credential;
	public static final String PREF_ACCOUNT_NAME = "auth_account";
	private String accountName;
	public static final int REQUEST_ACCOUNT_PICKER = 2;
	
	public AccountHelper(Activity context) {
		this.context=context;
		settings = context.getSharedPreferences("TLDR", 0);
		credential = GoogleAccountCredential
				.usingAudience(
						context,
						"server:client_id:511171351776-3o8dc555nqai62t3pe4m7ubrgc58i2ge.apps.googleusercontent.com");
		setAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
	}
	
	public GoogleAccountCredential getCredential(){
		return credential;
	}
	
	
	public boolean isAccountSelected(){
		if (credential.getSelectedAccountName() != null) {
			// Already signed in, begin app!
			Logger.getLogger("tldr-logger").log(Level.INFO, "Logged in!");
			return true;

		} else {
			// Not signed in, show login window or request an account.
			Logger.getLogger("tldr-logger").log(Level.INFO, "Not signed in!");
			return false;
			

		}
	}
	
	public void startAccountSelection(){
		context.startActivityForResult(credential.newChooseAccountIntent(),
				REQUEST_ACCOUNT_PICKER);	
	}
	
	public void setAccountName(String accountName) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PREF_ACCOUNT_NAME, accountName);
		editor.commit();
		credential.setSelectedAccountName(accountName);
		this.accountName = accountName;
	}
	
}
