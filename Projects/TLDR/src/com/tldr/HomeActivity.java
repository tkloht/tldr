package com.tldr;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;


public class HomeActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_home);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setIcon(R.drawable.logo);
		actionBar.setTitle("TL;DR");

		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction;
		Fragment fragment = new MapFragment();
		transaction = fm.beginTransaction();
		transaction.replace(R.id.homeActivity, fragment);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		

		Fragment fragment;
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction;
		Log.e("com.tldr.homeActivity", "menuitem selected: " + item.toString());
		switch (item.getItemId()) {
		case R.id.menu_map:
			fragment = new MapFragment();
			fragment.setArguments(new Bundle());
			transaction = fm.beginTransaction();
			transaction.replace(R.id.homeActivity, fragment);
			transaction.commit();
			break;
		case R.id.menu_tasks:
			fragment = new TasksFragment();
			fragment.setArguments(new Bundle());
			transaction = fm.beginTransaction();
			transaction.replace(R.id.homeActivity, fragment);
			transaction.commit();
			break;
		case R.id.menu_community:
			fragment = new CommunityFragment();
			fragment.setArguments(new Bundle());
			transaction = fm.beginTransaction();
			transaction.replace(R.id.homeActivity, fragment);
			transaction.commit();
			break;
		case R.id.menuShowCompass:
		case R.id.menuOnlyShowAcceptedTasks:
		case R.id.menuShowPlayer:
			if (item.isChecked())
				item.setChecked(false);
			else
				item.setChecked(true);
			break;

		default:
			break;
		}

		return true;
	}

	
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

}