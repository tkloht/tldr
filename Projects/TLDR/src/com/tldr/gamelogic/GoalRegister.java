package com.tldr.gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.datastore.UserInfoDatastore;
import com.tldr.GlobalData;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.exlap.TriggerRegister;

public class GoalRegister {

	private List<GoalStructure> registeredGoals;
	private TriggerRegister triggerRegister;
	private UserInfoDatastore datastore;

	public GoalRegister() {
		this.registeredGoals = new ArrayList<GoalStructure>();
		this.triggerRegister = GlobalData.getTriggerRegister();
		if (!GlobalData.getAcceptedUnfinishedGoals().isEmpty()) {
			for (GoalStructure goalStructure : GlobalData
					.getAcceptedUnfinishedGoals()) {
				this.addGoal(goalStructure);
			}
		}
	}

	/**
	 * 
	 * @param goal
	 * @return true wenn goal noch nicht existierte, false wenn schon
	 */
	public boolean addGoal(GoalStructure goal) {
		final String desc = goal.getDescription();
		final long idgoals = goal.getId();
		this.registeredGoals.add(goal);
		List<Map<String, String>> conditionsList = (List<Map<String, String>>) goal
				.getJsonParse().get("conditions");
		for (Map<String, String> condition : conditionsList) {
			ConditionCheck cc = new ConditionCheck(condition, new OnTrue() {

				@Override
				public void onTrue() {
					// Hier muessten erstenaml allee contidions true sein bevor
					// isch fnished gloals setze
					Log.i("TLDR", " FINISHED Condition:" + desc);
					Log.i("TLDR", " FINISHED Condition:" + idgoals);

					UserInfo currentUser = GlobalData.getCurrentUser();
					if(currentUser.getFinishedGoals()==null){
						currentUser.setFinishedGoals(new ArrayList<Long>());
					}
					currentUser.getFinishedGoals().add(idgoals);
					datastore.updateUser(currentUser);
				}
			});
			if (cc.successfullyInitialized()) {
				triggerRegister.register(cc);
			}
		}
		return true;
	}

	public UserInfoDatastore getDatastore() {
		return datastore;
	}

	public void setDatastore(UserInfoDatastore datastore) {
		this.datastore = datastore;
	}

	public interface OnTrue {
		public void onTrue();
	}

}
