package com.tldr;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.tldr.com.tldr.userinfoendpoint.Userinfoendpoint;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;

/**
 * The Main Activity.
 * 
 * This activity starts up the RegisterActivity immediately, which communicates
 * with your App Engine backend using Cloud Endpoints. It also receives push
 * notifications from backend via Google Cloud Messaging (GCM).
 * 
 * Check out RegisterActivity.java for more details.
 */
public class MainActivity extends Activity {
	private SharedPreferences settings;
	private GoogleAccountCredential credential;
	private String accountName;
	private Userinfoendpoint service;
	private UserInfo user;
	public static final String PREF_ACCOUNT_NAME = "auth_account";
	static final int REQUEST_ACCOUNT_PICKER = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		settings = getSharedPreferences("TLDR", 0);
		credential = GoogleAccountCredential.usingAudience(this,
				"server:client_id:511171351776-3o8dc555nqai62t3pe4m7ubrgc58i2ge.apps.googleusercontent.com");
		setAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
		Userinfoendpoint.Builder builder = new Userinfoendpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new GsonFactory(),
				credential);
		service = CloudEndpointUtils.updateBuilder(builder).build();
		

		if (credential.getSelectedAccountName() != null) {
			// Already signed in, begin app!
			Logger.getLogger("tldr-logger").log(Level.INFO, "Logged in!");
			afterAccountSelection();

		} else {
			// Not signed in, show login window or request an account.
			Logger.getLogger("tldr-logger").log(Level.INFO, "Not signed in!");
			startActivityForResult(credential.newChooseAccountIntent(),
					REQUEST_ACCOUNT_PICKER);

		}

		// setContentView(R.layout.activity_main);
	}

	// setAccountName definition
	private void setAccountName(String accountName) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PREF_ACCOUNT_NAME, accountName);
		editor.commit();
		credential.setSelectedAccountName(accountName);
		this.accountName = accountName;
	}

	private void afterAccountSelection() {
		new RegisterUserInfoTask().execute(new UserInfo()
				.setEmail(accountName));
	}
	private void showLoginAlert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Success!");
		alertDialog.setMessage("You just logged in with email Adress: "+user.getEmail()+", username: "+user.getUsername()+" and key: "+user.getKey());
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		// here you can add functions
			afterAuthorization();
		}
		});
		alertDialog.show();
	}
	private void afterAuthorization() {
		// Start up RegisterActivity right away
		if(user.getUsername()==null){
		// redirect
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
		// Since this is just a wrapper to start the main activity,
		// finish it after launching RegisterActivity
		}
		else
		{
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
		}
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:
			if (data != null && data.getExtras() != null) {
				String accountName = data.getExtras().getString(
						AccountManager.KEY_ACCOUNT_NAME);
				if (accountName != null) {
					setAccountName(accountName);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString(PREF_ACCOUNT_NAME, accountName);
					editor.commit();
					// User is authorized.
					afterAccountSelection();

				}
			}
			break;
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
				Log.d("TicTacToe", e.getMessage(), e);
			}
			return registeredUser;
		}

		@Override
		    protected void onPostExecute(UserInfo registeredUser) {
		    if(registeredUser!=null){
		    	user=registeredUser;
		    	showLoginAlert();
		    }
		    else{
		    	Log.w("TLDR", "No User was registered due to an Error!");
		    }
			   
		    }
	}
}
