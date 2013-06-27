package com.datastore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.nfc.FormatException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.tldr.gamelogic.GoalStructure;
import com.tldr.goalendpoint.Goalendpoint;
import com.tldr.goalendpoint.model.CollectionResponseGoal;
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
	
	
	public void getGoalsForTask(Task t){
		new GetGoalsTask().execute();
	}
	
	
	
	private class CreateFakeTasksTask extends
	AsyncTask<Void, Void, Void> {
	@Override
	protected Void doInBackground(Void... v) {

		try {
			GoalStructure gs_t1 = new GoalStructure(); 
			GoalStructure gs_t2= new GoalStructure();
			GoalStructure gs_t3= new GoalStructure();
			GoalStructure gs_t4 = new GoalStructure();
			GoalStructure gs2_t4 = new GoalStructure();
			
			gs_t1.addBaseData("Fahre langsamer als 50km/h", "null").addCondition("Speed", "leq", "50").addReward("xp", "2000");
			gs_t2.addBaseData("Radiofrequenz an Ampel wechseln", "traffic_lights_entry").addCondition("Speed", "leq", "10").addCondition("radio_freq", "change", "null").addReward("xp", "4000");
			gs_t3.addBaseData("Schalte Warnblinklicht an Baustelle ein", "null").addCondition("hazards_light", "eq", "on").addReward("xp", "3000");
			gs_t4.addBaseData("Melde Stoppschilder", "stop_sign_entry").addCondition("handbreak", "eq", "on").addReward("xp", "1600");
			gs2_t4.addBaseData("Fahre Route ab", "").addCondition("drive_polyline", "eq", "52.456114,13.295408;52.45803,13.300332;52.456637,13.301695;52.45448,13.299378;52.454558,13.295547").addReward("xp", "5000");

			try {
				String goal_t1= JsonParser.writeNewJsonGoalString(gs_t1.getJsonParse());
				String goal_t2= JsonParser.writeNewJsonGoalString(gs_t2.getJsonParse());
				String goal_t3= JsonParser.writeNewJsonGoalString(gs_t3.getJsonParse());
				String goal_t4= JsonParser.writeNewJsonGoalString(gs_t4.getJsonParse());
				String goal2_t4= JsonParser.writeNewJsonGoalString(gs2_t4.getJsonParse());
//				Goal g_t1=goal_service.insertGoal(new Goal().setJsonString(goal_t1)).execute();
//				Goal g_t2=goal_service.insertGoal(new Goal().setJsonString(goal_t2)).execute();
//				Goal g_t3=goal_service.insertGoal(new Goal().setJsonString(goal_t3)).execute();
				Goal g_t4=goal_service.insertGoal(new Goal().setJsonString(goal_t4)).execute();
				Goal g2_t4=goal_service.insertGoal(new Goal().setJsonString(goal2_t4)).execute();
				List<Long> goals_t1= new ArrayList<Long>();
				List<Long> goals_t2= new ArrayList<Long>();
				List<Long> goals_t3= new ArrayList<Long>();
				List<Long> goals_t4= new ArrayList<Long>();
//				goals_t1.add(g_t1.getId());
//				goals_t2.add(g_t2.getId());
//				goals_t3.add(g_t3.getId());
				goals_t4.add(g_t4.getId());
				goals_t4.add(g2_t4.getId());
			
			
//			service.insertTask(new Task().setTitle("Observiere den Monbijoupark")
//					.setDescription("Es wird von Autonomy Komplikationen am Monbijou Park berichtet."+
//							"Umrunden Sie den Monbijou Park und melden Sie Unregelmäßigkeiten. "+
//							"Fahren sie nicht schneller als 50km/h um unentdeckt zu bleiben!")
//							.setGeoLat(52.523702).setGeoLon(13.397588).setGoals(goals_t1)).execute();
//			service.insertTask(new Task().setTitle("Sabotage")
//					.setDescription("Versuche die Ampeln der Lindauer Allee unter unsere Kontrolle zu bringen."+
//							"Fahren Sie dazu über die Lindauer Allee und wechseln sie die Radiofrequenz, wenn Sie an einer Ampel stehen.")
//							.setGeoLat(52.574211).setGeoLon(13.349095 ).setGoals(goals_t2)).execute();
//			service.insertTask(new Task().setTitle("Untersuchung am Salzufer")
//					.setDescription("Ein Informant hat uns mitgeteilt, dass eine oder mehrere Baustellen am Salzufer von der gegnerischen Fraktion sabotiert wurde."+
//							"Fahren das Salzufer entlang und markieren Sie Baustellen durch kurzes Betätigen vom Warnblinker")
//							.setGeoLat(52.518493).setGeoLon(13.321928).setGoals(goals_t3)).execute();
			service.insertTask(new Task().setTitle("Spuren der Vergangenheit")
					.setDescription("Angeblich existieren an der angegebenen Route noch Relikte aus vergangenen Tagen. "+
							"Melden Sie diese durch anziehen der Handbremse, während Sie davor stehen.")
							.setGeoLat(52.456114).setGeoLon(13.295408).setGoals(goals_t4)).execute();
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
	
			if(context!=null)
				if(tasks!=null)
					context.handleRequestResult(REQUEST_TASK_FETCHNEARBY, tasks.getItems());
				else
					context.handleRequestResult(REQUEST_TASK_FETCHNEARBY, new ArrayList<Task>());
	
	}
}
	
	private class GetGoalsTask extends
	AsyncTask<Void, Void, CollectionResponseGoal> {
	@Override
	protected CollectionResponseGoal doInBackground(Void... v) {
		CollectionResponseGoal goals = null;
		try {
			goals = goal_service.listGoal()
					.execute();
			return goals;
		} catch (IOException e) {
			Log.d("TLDR", e.getMessage(), e);
		}
		return goals;
	}
	
	@Override
	protected void onPostExecute(CollectionResponseGoal goals) {
		if(context!=null)
			context.handleRequestResult(REQUEST_TASK_FETCHGOALS, goals.getItems());
	
	}
}	

	

}
