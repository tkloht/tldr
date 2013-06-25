package com.datastore;

import java.io.IOException;

import android.nfc.FormatException;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.tldr.com.tldr.userinfoendpoint.model.CollectionResponseUserInfo;
import com.tldr.gamelogic.GoalStructure;
import com.tldr.goalendpoint.Goalendpoint;
import com.tldr.goalendpoint.model.Goal;
import com.tldr.taskendpoint.Taskendpoint;
import com.tldr.taskendpoint.model.CollectionResponseTask;
import com.tldr.taskendpoint.model.Task;
import com.tldr.tools.CloudEndpointUtils;
import com.tldr.tools.JsonParser;

/**
 * @author manuschmanu
 *	This Datastore handles access to Tasks and Goals!
 */
public class TaskDatastore extends BaseDatastore {

	Taskendpoint service;
	Goalendpoint goal_service;
	
	

	public TaskDatastore(DatastoreResultHandler context, GoogleAccountCredential credential) {
		super(context);
		Taskendpoint.Builder builder = new Taskendpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new GsonFactory(),
				credential);
		service = CloudEndpointUtils.updateBuilder(builder).build();
		Goalendpoint.Builder goal_builder = new Goalendpoint.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential);
		goal_service = CloudEndpointUtils.updateBuilder(goal_builder).build();
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
//			service.insertTask(new Task().setTitle("TestTask")).execute();
			GoalStructure gs = new GoalStructure();
			gs.addBaseData("This is a test goal. Drive 50km/h", "null").addCondition("Speed", "eq", "100").addReward("xp", "2000");
			try {
				String goal= JsonParser.writeNewJsonGoalString(gs.getJsonParse());
//				goal_service.insertGoal(new Goal().setJsonString(goal)).execute();
				gs =JsonParser.parseJsonGoalString(goal);
				System.out.println(gs);
			} catch (FormatException e) {
				// TODO Auto-generated catch block
				Log.e("TLDR", e.getMessage());
			}
			
			
			

		} catch (Exception e) {
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
