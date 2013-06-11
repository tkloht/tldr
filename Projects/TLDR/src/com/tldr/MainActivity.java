package com.tldr;

import java.util.List;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.auth.AccountHelper;
import com.datastore.BaseDatastore;
import com.datastore.DatastoreResultHandler;
import com.datastore.TaskDatastore;
import com.datastore.UserInfoDatastore;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.messageEndpoint.MessageEndpoint;
import com.tldr.tools.CloudEndpointUtils;
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
public class MainActivity extends Activity implements DatastoreResultHandler {

	enum State {
		REGISTERED, REGISTERING, UNREGISTERED, UNREGISTERING
	}

	private UserInfo user;
	private View mLoginStatusView;
	private View mLoginFormView;
	private View mLoginErrorView;
	private View mRegisterStatusView;
	private Button mTryAgainButton;
	private EditText mUsernameText;
	private AccountHelper accountHelper;
	private UserInfoDatastore userInfoDatastore;
	private TaskDatastore taskDatastore;
	private AnimationDrawable anim;
	private State curState = State.UNREGISTERED;
	private OnTouchListener registerListener = null;
	private OnTouchListener unregisterListener = null;

	static final int VIEW_MODE_LOGIN_PROGRESS = 0;
	static final int VIEW_MODE_ERROR = 1;
	static final int VIEW_MODE_REGISTER = 2;
	static final int VIEW_MODE_REGISTER_PROGRESS = 3;
	private int currentMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		animationSkull();

		mUsernameText = (EditText) findViewById(R.id.reg_username);
		mTryAgainButton = (Button) findViewById(R.id.login_retry_button);
		mTryAgainButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				showProgress(VIEW_MODE_LOGIN_PROGRESS);
				selectAccount();
				return false;
			}
		});

		Button registerButton = (Button) findViewById(R.id.btnRegister);
		registerButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
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
							userInfoDatastore.registerUser(user);
						} else
							success = false;
					}
					return success;
				} else
					return false;
			}
		});

		showProgress(VIEW_MODE_LOGIN_PROGRESS);

		// Init AccountHelper
		accountHelper = new AccountHelper(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		selectAccount();

	}

	private void selectAccount() {
		if (!accountHelper.isAccountSelected()) {
			accountHelper.startAccountSelection();
		} else {
			afterAccountSelection();
		}
	}

	private void animationSkull() {
		ImageView img = (ImageView) findViewById(R.id.tldr_skull_anim);
		anim = (AnimationDrawable) img.getDrawable();
		img.post(run);
	}

	Runnable run = new Runnable() {
		@Override
		public void run() {
			anim.start();
		}
	};

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

	private void afterAccountSelection() {
		userInfoDatastore = new UserInfoDatastore(
				accountHelper.getCredential(), this);
		taskDatastore = new TaskDatastore(this, accountHelper.getCredential());
		userInfoDatastore.registerUser(new UserInfo().setEmail(accountHelper
				.getCredential().getSelectedAccountName()));
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
			anim.stop();
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void registerGCM() {
		try {
			GCMIntentService.register(getApplicationContext());
		} catch (Exception e) {
			Log.w("TLDR", "GCM Fail");
			Log.e(MainActivity.class.getName(),
					"Exception received when attempting to register for Google Cloud "
							+ "Messaging. Perhaps you need to set your virtual device's "
							+ " target to Google APIs? "
							+ "See https://developers.google.com/eclipse/docs/cloud_endpoints_android"
							+ " for more information.", e);
		}
		MessageEndpoint.Builder endpointBuilder = new MessageEndpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});

		GlobalData.setMessageEndpoint(CloudEndpointUtils.updateBuilder(
				endpointBuilder).build());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case AccountHelper.REQUEST_ACCOUNT_PICKER:
			if (data != null && data.getExtras() != null) {
				String accountName = data.getExtras().getString(
						AccountManager.KEY_ACCOUNT_NAME);
				if (accountName != null) {
					accountHelper.setAccountName(accountName);
					registerGCM();
					// User is authorized.
					afterAccountSelection();

				}
			}
			break;
		}
	}

	@Override
	public void handleRequestResult(int requestId, Object result) {
		// TODO Auto-generated method stub
		switch (requestId) {
		case BaseDatastore.REQUEST_USERINFO_REGISTER:
			UserInfo registeredUser = (UserInfo) result;
			if (registeredUser != null) {
				user = registeredUser;
				Log.d("TLDR",
						"User Sign In Successfull.. testing nearbyUserDatastore Method...");
				userInfoDatastore.getNearbyUsers();
			} else {
				Log.w("TLDR", "No User was registered due to an Error!");
				showProgress(VIEW_MODE_ERROR);
			}
			break;
		case BaseDatastore.REQUEST_USERINFO_NEARBYUSERS:
			List<UserInfo> nearbyUsers = (List<UserInfo>) result;
			for (UserInfo ui : nearbyUsers) {
				Log.d("TLDR", ui.getEmail() + " " + ui.getUsername());
			}
			showLoginAlert();
			break;
		}

	}
}
