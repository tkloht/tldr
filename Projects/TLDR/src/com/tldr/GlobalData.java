package com.tldr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;

import com.datastore.UserInfoDatastore;
import com.google.android.gms.maps.model.CameraPosition;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.exlap.ConnectionHelper;
import com.tldr.exlap.TriggerRegister;
import com.tldr.gamelogic.GoalRegister;
import com.tldr.gamelogic.GoalStructure;
import com.tldr.messageEndpoint.MessageEndpoint;
import com.tldr.taskendpoint.model.Task;
import com.tldr.tools.FakeLocationProvider;
import com.tldr.tools.KnightRider;

public class GlobalData {

	private static Location lastknownPosition = null;
	private static MessageEndpoint messageEndpoint = null;
	private static UserInfo currentUser = null;
	private static UserInfoDatastore datastore = null;
	private static ConnectionHelper connectionHelper;
	private static TriggerRegister triggerRegister;
	private static GoalRegister goalRegister = null;
	private static boolean firstStart = true;
	private static boolean exlapConnected = false;
	private static KnightRider textToSpeach;
	private static LocationManager mLocationManager;
	private static LocalBroadcastManager broadcastManager;
	private static HashMap<Long, GoalStructure> allGoals;

	private static List<Task> allTasks;
	private static List<UserInfo> allUsers;
	private static List<UserInfo> usersDef;
	private static List<UserInfo> usersMof;
	public static final int FRACTION_DEFIANCE = 1;
	public static final int FRACTION_MINISTRY_OF_FREEDOM = 2;
	public static boolean fake_location_data_enabled = false;

	public static FakeLocationProvider fakeLocationProvider = null;
	private static CameraPosition cameraPosition;
	private static Activity activity;

	public static LocationManager getLocationManager() {
		return mLocationManager;
	}

	public static void setLocationManager(LocationManager mLocationManager) {
		GlobalData.mLocationManager = mLocationManager;

	}

	public static FakeLocationProvider getFakeLocationProvider() {
		return fakeLocationProvider;
	}

	public static void setFakeLocationProvider(
			FakeLocationProvider fakeLocationProvider) {
		GlobalData.fakeLocationProvider = fakeLocationProvider;
	}

	public static boolean isFake_location_data_enabled() {
		return fake_location_data_enabled;
	}

	public static void setFake_location_data_enabled(
			boolean fake_location_data_enabled) {
		GlobalData.fake_location_data_enabled = fake_location_data_enabled;
	}

	public static List<Task> getAcceptedTasks() {
		List<Task> lReturn = new ArrayList<Task>();
		List<Task> competed = getCompletedTasks();
		if (allTasks != null) {

			for (Task t : allTasks) {
				if (currentUser.getAcceptedTasks() != null
						&& currentUser.getAcceptedTasks().contains(t.getId())
						&& !competed.contains(t)) {
					lReturn.add(t);
				}
			}
		}
		return lReturn;
	}

	public static List<Task> getCompletedTasks() {
		List<Task> lReturn = new ArrayList<Task>();
		if (allTasks != null) {

			for (Task t : allTasks) {
				if (currentUser.getAcceptedTasks() != null
						&& currentUser.getAcceptedTasks().contains(t.getId())) {
					if (currentUser.getFinishedGoals() != null) {
						boolean done = false;
						for (Long l : t.getGoals()) {
							if (currentUser.getFinishedGoals().contains(l)) {
								done = true;
							} else {
								done = false;
								break;
							}
						}
						if (done) {
							lReturn.add(t);
						}
					}
				}
			}
		}
		return lReturn;
	}

	public static List<Task> getCompletedTasks(UserInfo u) {
		List<Task> lReturn = new ArrayList<Task>();
		for (Task t : allTasks) {
			if (u.getAcceptedTasks() != null
					&& u.getAcceptedTasks().contains(t.getId())) {
				if (u.getFinishedGoals() != null) {
					boolean done = false;
					for (Long l : t.getGoals()) {
						if (u.getFinishedGoals().contains(l)) {
							done = true;
						} else {
							done = false;
							break;
						}
					}
					if (done) {
						lReturn.add(t);
					}
				}
			}
		}
		return lReturn;
	}

	public static List<GoalStructure> getAcceptedUnfinishedGoals() {
		List<GoalStructure> lReturn = new ArrayList<GoalStructure>();
		List<Task> acceptedTasks = getAcceptedTasks();
		List<Long> acceptedGoals = new ArrayList<Long>();
		for (Task t : acceptedTasks) {
			acceptedGoals.addAll(t.getGoals());
		}
		for (Long id : acceptedGoals) {
			GoalStructure current = allGoals.get(id);
			if (acceptedGoals.contains(id)) {
				if (currentUser.getFinishedGoals() == null) {
					currentUser.setFinishedGoals(new ArrayList<Long>());
					currentUser.setFinishedGoalsTS(new ArrayList<Long>());
				}
				if (!currentUser.getFinishedGoals().contains(id)) {
					lReturn.add(current);
				}
			}
		}
		return lReturn;
	}

	public static Task getTaskOfGoal(long goalId) {
		for (Task t : allTasks) {
			if (t.getGoals().contains(goalId)) {
				return t;
			}
		}
		return null;
	}

	public static Long getTimestampAccepted(UserInfo u, Task task) {
		List<Long> tasks = u.getAcceptedTasks();
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).equals(task.getId())
					&& u.getAcceptedTasksTS() != null) {
				return u.getAcceptedTasksTS().get(i);
			}
		}
		return 0L;
	}

	public static Long getTimestampCompleted(UserInfo u, Task task) {
		List<Long> goals = u.getFinishedGoals();
		Long max = 0L;
		for (int i = 0; i < goals.size(); i++) {
			for (Long l : task.getGoals()) {
				if (l.equals(goals.get(i))
						&& u.getFinishedGoalsTS().get(i).compareTo(max) > 1
						&& u.getFinishedGoalsTS() != null) {
					max = u.getFinishedGoalsTS().get(i);
				}
			}
		}
		return 0L;
	}

	public static Task getTaskById(Long id) {
		for (Task t : allTasks) {
			if (t.getId().equals(id)) {
				return t;
			}
		}
		return null;
	}

	public static boolean isTaskCompleted(Task t) {
		List<Long> taskGoals = t.getGoals();
		boolean complete = true;
		for (long goalId : taskGoals) {
			if (!currentUser.getFinishedGoals().contains(goalId)) {
				complete = false;
				break;
			}
		}
		return complete;
	}

	public static GoalStructure getGoalsFromTask(Long taskId) {
		return allGoals.get(getTaskById(taskId).getGoals().get(0));
	}

	public static HashMap<Long, GoalStructure> getAllGoals() {
		return allGoals;
	}

	public static void setCurrentAcceptedTasksGoals(
			HashMap<Long, GoalStructure> currentAcceptedTasksGoals) {
		GlobalData.allGoals = currentAcceptedTasksGoals;
	}

	public static List<Task> getAllTasks() {
		return allTasks;
	}

	public static List<UserInfo> getAllUsers() {
		return allUsers;
	}

	public static List<UserInfo> getFractionUsers(int fraction) {
		switch (fraction) {
		case (FRACTION_DEFIANCE):
			return usersDef;
		case (FRACTION_MINISTRY_OF_FREEDOM):
			return usersMof;
		}
		return null;
	}

	public static void setFractionUsers(int fraction, List<UserInfo> users) {
		switch (fraction) {
		case (FRACTION_DEFIANCE):
			GlobalData.usersDef = users;
			break;
		case (FRACTION_MINISTRY_OF_FREEDOM):
			GlobalData.usersMof = users;
			break;
		}
	}

	public static void setAllTasks(List<Task> allTasks) {
		GlobalData.allTasks = allTasks;
	}

	public static void setAllUsers(List<UserInfo> allUsers) {
		GlobalData.allUsers = allUsers;
	}

	public static Location getLastknownPosition() {
		return lastknownPosition;
	}

	public static void setLastknownPosition(Location lastknown) {
		lastknownPosition = lastknown;
	}

	public static MessageEndpoint getMessageEndpoint() {
		return messageEndpoint;
	}

	public static void setMessageEndpoint(MessageEndpoint endpoint) {
		messageEndpoint = endpoint;
	}

	public static UserInfo getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(UserInfo currentUser) {
		GlobalData.currentUser = currentUser;
	}

	public static ConnectionHelper getConnectionHelper() {
		return connectionHelper;
	}

	public static void setConnectionHelper(ConnectionHelper connectionHelper) {
		GlobalData.connectionHelper = connectionHelper;
	}

	public static TriggerRegister getTriggerRegister() {
		// if (triggerRegister == null) {
		// setTriggerRegister(new TriggerRegister());
		// }
		return triggerRegister;
	}

	public static void setTriggerRegister(TriggerRegister triggerRegister) {
		GlobalData.triggerRegister = triggerRegister;
	}

	public static GoalRegister getGoalRegister() {
		return goalRegister;
	}

	public static void setGoalRegister(GoalRegister goalRegister) {
		GlobalData.goalRegister = goalRegister;
	}

	public static boolean isFirstStart() {
		return firstStart;
	}

	public static void setFirstStart(boolean firstStart) {
		GlobalData.firstStart = firstStart;
	}

	public static boolean isExlapConnected() {
		return exlapConnected;
	}

	public static void setExlapConnected(boolean exlapConnected) {
		GlobalData.exlapConnected = exlapConnected;
	}

	public static UserInfoDatastore getDatastore() {
		return datastore;
	}

	public static void setDatastore(UserInfoDatastore datastore) {
		GlobalData.datastore = datastore;
	}

	public static KnightRider getTextToSpeach() {
		return textToSpeach;
	}

	public static void setTextToSpeach(Context c) {
		GlobalData.textToSpeach = new KnightRider(c);
	}

	public static boolean isParentGoalFinished(Long goalId) {
		if (currentUser.getFinishedGoals() == null) {
			return false;
		}
		boolean finished = true;
		Task parentTask = getTaskForGoalId(goalId);
		GoalStructure subGoal = allGoals.get(goalId);
		String desc = subGoal.getDescription();
		if (desc.contains("##")) {
			String[] split = desc.split("##");
			List<Long> otherGoals = parentTask.getGoals();
			for (Long gId : otherGoals) {
				GoalStructure otherGoal = allGoals.get(gId);
				String other_desc = otherGoal.getDescription();
				if (other_desc.contains("##")) {
					String[] other_split = other_desc.split("##");
					if (other_split[1].equals(split[1])) { // Geh��ren zum
															// selben subgoal
						if (!currentUser.getFinishedGoals().contains(gId))
							finished = false;
					}
				}
			}
		} else
			finished = currentUser.getFinishedGoals().contains(goalId);

		return finished;

	}

	public static Task getTaskForGoalId(Long goalId) {
		for (Task t : allTasks) {
			if (t.getGoals().contains(goalId))
				return t;
		}
		return null;
	}

	public static void setCameraPosition(CameraPosition cameraPosition) {
		GlobalData.cameraPosition = cameraPosition;
	}

	public static CameraPosition getCameraPosition() {
		return GlobalData.cameraPosition;
	}

	public static void setActivity(Activity activity) {
		GlobalData.activity = activity;
	}

	public static Activity getActivity() {
		return activity;
	}
	
	public static LocalBroadcastManager getBroadcastManager() {
		return broadcastManager;
	}

	public static void setBroadcastManager(LocalBroadcastManager broadcastManager) {
		GlobalData.broadcastManager = broadcastManager;
	}

}
