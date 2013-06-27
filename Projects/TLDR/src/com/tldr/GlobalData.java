package com.tldr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.location.Location;

import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.exlap.ConnectionHelper;
import com.tldr.exlap.TriggerRegister;
import com.tldr.gamelogic.GoalRegister;
import com.tldr.gamelogic.GoalStructure;
import com.tldr.goalendpoint.model.Goal;
import com.tldr.messageEndpoint.MessageEndpoint;
import com.tldr.taskendpoint.model.Task;

public class GlobalData {

	private static Location lastknownPosition = null;
	private static MessageEndpoint messageEndpoint = null;
	private static UserInfo currentUser = null;
	private static ConnectionHelper connectionHelper;
	private static TriggerRegister triggerRegister;
	private static GoalRegister goalRegister;

	private static HashMap<Long, GoalStructure> allGoals;

	private static List<Task> allTasks;

	public static List<Task> getAcceptedTasks() {
		List<Task> lReturn = new ArrayList<Task>();
		for (Task t : allTasks) {
			if (currentUser.getAcceptedTasks() != null
					&& currentUser.getAcceptedTasks().contains(t.getId())) {
				lReturn.add(t);
			}
		}
		return lReturn;
	}

	public static List<Task> getCompletedTasks() {
		  List<Task> lReturn = new ArrayList<Task>();
		  for (Task t : allTasks) {
		   if (currentUser.getAcceptedTasks() != null
		     && currentUser.getAcceptedTasks().contains(t.getId())) {
		    if (currentUser.getFinishedGoals() != null) {
		     for (Long l : t.getGoals()) {
		      if (currentUser.getFinishedGoals().contains(l)) {
		       lReturn.add(t);
		      }
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
				}
				if (!currentUser.getFinishedGoals().contains(id)) {
					lReturn.add(current);
				}
			}
		}
		return lReturn;
	}

	public static Task getTastById(Long id) {
		for (Task t : allTasks) {
			if (t.getId() == id) {
				return t;
			}
		}
		return null;
	}

	public static GoalStructure getGoalsFromTask(Long taskId) {
		return allGoals.get(getTastById(taskId).getGoals().get(0));
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

	public static void setAllTasks(List<Task> allTasks) {
		GlobalData.allTasks = allTasks;
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

	public static void setConnectionHelper(ConnectionHelper connectionHelperr) {
		connectionHelper = connectionHelperr;
	}

	public static TriggerRegister getTriggerRegister() {
		if (triggerRegister == null) {
			setTriggerRegister(new TriggerRegister());
		}
		return triggerRegister;
	}

	public static void setTriggerRegister(TriggerRegister triggerRegister) {
		GlobalData.triggerRegister = triggerRegister;
	}

	public static GoalRegister getGoalRegister() {
		if (goalRegister == null) {
			setGoalRegister(new GoalRegister());
		}
		return goalRegister;
	}

	public static void setGoalRegister(GoalRegister goalRegister) {
		GlobalData.goalRegister = goalRegister;
	}

}
