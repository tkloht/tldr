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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.tldr.com.tldr.userinfoendpoint.Userinfoendpoint;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.tools.ToolBox;

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
	private View mLoginStatusView;
	private View mLoginFormView;
	private View mLoginErrorView;
	private View mRegisterStatusView;
	private Button mTryAgainButton;
	private EditText mUsernameText;
	public static final String PREF_ACCOUNT_NAME = "auth_account";
	static final int REQUEST_ACCOUNT_PICKER = 2;

	static final int VIEW_MODE_LOGIN_PROGRESS = 0;
	static final int VIEW_MODE_ERROR = 1;
	static final int VIEW_MODE_REGISTER = 2;
	static final int VIEW_MODE_REGISTER_PROGRESS = 3;
	private int currentMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mUsernameText = (EditText) findViewById(R.id.reg_username);
		mTryAgainButton = (Button) findViewById(R.id.login_retry_button);
		mTryAgainButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				startAccountSelection();
				showProgress(VIEW_MODE_LOGIN_PROGRESS);

				return false;
			}
		});

		Button registerButton = (Button) findViewById(R.id.btnRegister);
		registerButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
			if(event.getAction()==MotionEvent.ACTION_UP){
					showProgress(VIEW_MODE_REGISTER_PROGRESS);
					boolean success = true;
					// Check Values for EditText
					String username = mUsernameText.getText().toString();
					if (username.length() < 3) {
						ToolBox.showErrorMessage(mUsernameText,
								"Your username should at least have 3 characters!");
						success = false;
						showProgress(VIEW_MODE_REGISTER);
					} else {
						if (user != null) {
							user.setUsername(username);
							new RegisterUserInfoTask().execute(user);
						} else
							success = false;
					}
					return success;
				}
			else
				return false;
			}
		});

		showProgress(VIEW_MODE_LOGIN_PROGRESS);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startAccountSelection();
	}

	private void showProgress(final int mode) {
		currentMode = mode;
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginErrorView = findViewById(R.id.login_error);
		mLoginFormView = findViewById(R.id.login_form);
		mRegisterStatusView = findViewById(R.id.register_status);

		// The ViewPropertyAnimator APIs are not available, so simply show
		// and hide the relevant UI components.
		mLoginStatusView
				.setVisibility(mode == VIEW_MODE_LOGIN_PROGRESS ? View.VISIBLE
						: View.GONE);
		mLoginFormView.setVisibility(mode == VIEW_MODE_REGISTER ? View.VISIBLE
				: View.GONE);
		mLoginErrorView.setVisibility(mode == VIEW_MODE_ERROR ? View.VISIBLE
				: View.GONE);
		mRegisterStatusView
				.setVisibility(mode == VIEW_MODE_REGISTER_PROGRESS ? View.VISIBLE
						: View.GONE);

	}

	private void startAccountSelection() {
		settings = getSharedPreferences("TLDR", 0);
		credential = GoogleAccountCredential
				.usingAudience(
						this,
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
		new RegisterUserInfoTask()
				.execute(new UserInfo().setEmail(accountName));
	}

	private void showLoginAlert() {
		ToolBox.showAlert(this, "Success",
				"You just logged in with email Adress: " + user.getEmail()
						+ ", username: " + user.getUsername() + " and id: "
						+ user.getId(), "Dismiss",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// here you can add functions
						afterAuthorization();
					}
				});
	}

	private void afterAuthorization() {
		// Start up RegisterActivity right away
		if (user.getUsername() == null) {
			// Request Username
			if (currentMode == VIEW_MODE_REGISTER_PROGRESS) {
				// Username was already taken!
				ToolBox.showErrorMessage(mUsernameText,
						"Your username is already taken!");
			}
			showProgress(VIEW_MODE_REGISTER);
		} else {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
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
				Log.d("TLDR", e.getMessage(), e);
			}
			return registeredUser;
		}

		@Override
		protected void onPostExecute(UserInfo registeredUser) {

				if (registeredUser != null) {
					user = registeredUser;
					showLoginAlert();
				} else {
					Log.w("TLDR", "No User was registered due to an Error!");
					showProgress(VIEW_MODE_ERROR);
				}

		}
	}
}
