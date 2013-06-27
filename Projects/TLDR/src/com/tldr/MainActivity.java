package com.tldr;

import java.util.HashMap;
import java.util.List;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.FormatException;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

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
import com.tldr.gamelogic.Factions;
import com.tldr.gamelogic.GoalStructure;
import com.tldr.goalendpoint.model.Goal;
import com.tldr.messageEndpoint.MessageEndpoint;
import com.tldr.taskendpoint.model.Task;
import com.tldr.tools.CloudEndpointUtils;
import com.tldr.tools.JsonParser;
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
public class MainActivity extends Activity implements DatastoreResultHandler, ViewFactory {

	enum State {
		REGISTERED, REGISTERING, UNREGISTERED, UNREGISTERING
	}
	
	private final static String progress_login="Authenticating..";
	private final static String progress_register="Registering..";
	private final static String progress_data="Fetching Data..";

	private UserInfo user;
	private int faction;
	private boolean fraction_checked;
	private View mLoginStatusView;
	private View mLoginFormView;
	private View mLoginErrorView;
	private View mRegisterStatusView;
	private TextSwitcher mSwitcher_login;
	private TextSwitcher mSwitcher_register;
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
        mSwitcher_login = (TextSwitcher) findViewById(R.id.login_status_message);
        mSwitcher_login.setFactory(this);
        mSwitcher_register = (TextSwitcher) findViewById(R.id.register_status_message);
        mSwitcher_register.setFactory(this);
        
		animationSkull();
		
		mUsernameText = (EditText) findViewById(R.id.reg_username);
		mTryAgainButton = (Button) findViewById(R.id.login_retry_button);
		mTryAgainButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				showProgress(VIEW_MODE_LOGIN_PROGRESS, progress_login);
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
					showProgress(VIEW_MODE_REGISTER_PROGRESS, progress_register);
					boolean success = true;
					// Check Values for EditText
					String username = mUsernameText.getText().toString();
					if (username.length() < 3) {
						ToolBox.showErrorMessage(mUsernameText,
								"Your username should at least have 3 characters!");
						success = false;
						showProgress(VIEW_MODE_REGISTER, "");
					} else {
						if (!fraction_checked) {
							ToolBox.showErrorMessage(mUsernameText,
									"Your have to choose a faction!");
							success = false;
							showProgress(VIEW_MODE_REGISTER, "");
						} else {
							if (user != null) {
								user.setUsername(username);
								user.setFaction(faction);
								userInfoDatastore.registerUser(user);
							} else
								success = false;
						}
					}
					return success;
				} else
					return false;
			}
		});

		showProgress(VIEW_MODE_LOGIN_PROGRESS, progress_login);

		// Init AccountHelper
		accountHelper = new AccountHelper(this);

	}

	public void onRadioButtonClicked(View view) {
		fraction_checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {
		case R.id.radio_fraction_def:
			if (fraction_checked)
				faction = Factions.FRACTION_DEFIANCE;
				break;
		case R.id.radio_fraction_mof:
			if (fraction_checked)
				faction = Factions.FRACTION_MINISTRY_OF_FREEDOM;
				break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		selectAccount();

	}
	


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
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

	private void showProgress(final int mode, String progress_description) {
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
		
		if(mode==VIEW_MODE_LOGIN_PROGRESS)
			ToolBox.animateTextSwitcher(mSwitcher_login, progress_description, this);
		if(mode==VIEW_MODE_REGISTER_PROGRESS)
			ToolBox.animateTextSwitcher(mSwitcher_register, progress_description, this);	

	}

	private void afterAccountSelection() {
		userInfoDatastore = new UserInfoDatastore(this,
				accountHelper.getCredential());
		taskDatastore = new TaskDatastore(this, accountHelper.getCredential());
		userInfoDatastore.registerUser(new UserInfo().setEmail(accountHelper
				.getCredential().getSelectedAccountName()));
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
			showProgress(VIEW_MODE_REGISTER, "");
		} else {
			registerGCM();
			fetchGameData();

		}
	}
	
	private void fetchGameData() {
		showProgress(VIEW_MODE_LOGIN_PROGRESS, "Gathering intel..");
		taskDatastore.getNearbyTasks();
	}
	
	private void ready() {
		anim.stop();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
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
				GlobalData.setCurrentUser(user);
				afterAuthorization();
			} else {
				Log.w("TLDR", "No User was registered due to an Error!");
				showProgress(VIEW_MODE_ERROR, "");
			}
			break;
		case BaseDatastore.REQUEST_USERINFO_NEARBYUSERS:
			List<UserInfo> nearbyUsers = (List<UserInfo>) result;
			for (UserInfo ui : nearbyUsers) {
				Log.d("TLDR", ui.getEmail() + " " + ui.getUsername());
			}

			break;
		case BaseDatastore.REQUEST_TASK_FETCHNEARBY:
			List<Task> allTasks = (List<Task>) result;
			GlobalData.setAllTasks(allTasks);
			taskDatastore.getGoalsForTask(null);
			break;
		case BaseDatastore.REQUEST_TASK_FETCHGOALS:
			List<Goal> allGoals=(List<Goal>) result;
			HashMap<Long, GoalStructure> parsedGoals= new HashMap<Long, GoalStructure>();
			for(Goal g:allGoals){
				try {
					GoalStructure newGS = JsonParser.parseJsonGoalString(g.getJsonString());
					newGS.setId(g.getId());
					parsedGoals.put(g.getId(), newGS);
					
				} catch (FormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			GlobalData.setCurrentAcceptedTasksGoals(parsedGoals);
			ready();
		}
		

	}

	
	@Override
	public View makeView() {
		// TODO Auto-generated method stub
        TextView t = new TextView(this);
        t.setTypeface(Typeface.SANS_SERIF);
        t.setTextAppearance(getApplicationContext(), android.R.attr.textAppearanceMedium);
        return t;
	}
}
