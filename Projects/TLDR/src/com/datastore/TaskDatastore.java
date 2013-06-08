package com.datastore;

import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.tldr.com.tldr.userinfoendpoint.model.CollectionResponseUserInfo;
import com.tldr.taskendpoint.Taskendpoint;
import com.tldr.taskendpoint.model.CollectionResponseTask;
import com.tldr.taskendpoint.model.Task;
import com.tldr.tools.CloudEndpointUtils;

public class TaskDatastore extends BaseDatastore {

	Taskendpoint service;
	
	public TaskDatastore(DatastoreResultHandler context, GoogleAccountCredential credential) {
		super(context);
		Taskendpoint.Builder builder = new Taskendpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new GsonFactory(),
				credential);
		service = CloudEndpointUtils.updateBuilder(builder).build();
	}
	
	public void createFakeTasks(){
		new CreateFakeTasksTask().execute();
	}
	
	public void getNearbyTasks(){
		new GetNearbyTasksTask().execute();
	}
	
	private class CreateFakeTasksTask extends
	AsyncTask<Void, Void, Void> {
	@Override
	protected Void doInBackground(Void... v) {

		try {
			service.insertTask(new Task().setTitle("TestTask")).execute();

		} catch (IOException e) {
			Log.d("TLDR", e.getMessage(), e);
		}
		
		return null;
		
		

	}

}
	
	private class GetNearbyTasksTask extends
	AsyncTask<Void, Void, CollectionResponseTask> {
	@Override
	protected CollectionResponseTask doInBackground(Void... v) {
		CollectionResponseTask registeredUser = null;
		try {
			registeredUser = service.listTask()
					.execute();
			return registeredUser;
		} catch (IOException e) {
			Log.d("TLDR", e.getMessage(), e);
		}
		return registeredUser;
	}
	
	@Override
	protected void onPostExecute(CollectionResponseTask tasks) {
	
			context.handleRequestResult(REQUEST_TASK_FETCHNEARBY, tasks.getItems());
	
	}
}
	

}
