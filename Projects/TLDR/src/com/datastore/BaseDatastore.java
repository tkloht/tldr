package com.datastore;


public abstract class BaseDatastore {
	/*
	 * Define all RequestTyoes here!
	 */
	public final static int REQUEST_USERINFO_REGISTER = 0;
	public final static int REQUEST_USERINFO_NEARBYUSERS = 1;
	public final static int REQUEST_USERINFO_UPDATEUSER = 2;
	public final static int REQUEST_TASK_FETCHNEARBY=3;
	public final static int REQUEST_TASK_FETCHGOALS=4;
	public final static int REQUEST_TASK_CREATEFAKETASKS = 999;
	protected DatastoreResultHandler context;
	
	protected BaseDatastore(DatastoreResultHandler context) {
		this.context=context;
	}
}
