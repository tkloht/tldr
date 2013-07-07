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
	private Map<Long, Integer> checkedConditions;

	public GoalRegister() {
		this.registeredGoals = new ArrayList<GoalStructure>();
		this.checkedConditions = new HashMap<Long, Integer>();
		if (GlobalData.getTriggerRegister() == null) {
			GlobalData.setTriggerRegister(new TriggerRegister());
		}
		this.triggerRegister = GlobalData.getTriggerRegister();
		registerAll();
	}

	public void registerAll() {
		if (!GlobalData.getAcceptedUnfinishedGoals().isEmpty()) {
			for (GoalStructure goalStructure : GlobalData
					.getAcceptedUnfinishedGoals()) {
				if (!this.registeredGoals.contains(goalStructure)) {
					this.addGoal(goalStructure);
				}
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
		List<Map<String, String>> conditionsList = (List<Map<String, String>>) goal
				.getJsonParse().get("conditions");
		this.checkedConditions.put(idgoals, conditionsList.size());
		for (final Map<String, String> condition : conditionsList) {
			Log.i("TLDR", " REGISTER Condition:"+ condition.toString());
			ConditionCheck cc = new ConditionCheck(condition, new OnTrue() {
				@Override
				public void onTrue() {
					// Hier muessten erstenaml allee contidions true sein bevor
					// isch fnished gloals setze					

					if (checkedConditions.get(idgoals) > 0) {
						checkedConditions.put(idgoals,
								checkedConditions.get(idgoals) - 1);
					}
					Log.i("TLDR", " FINISHED Condition:" + idgoals);
					Log.i("TLDR", " FINISHED Condition:" + condition);
					Log.i("TLDR", " FINISHED Condition:" + checkedConditions.get(idgoals) );
					if (checkedConditions.get(idgoals) == 0) {
						GlobalData.getTextToSpeach().say(
								"Annomalie Parameter " + desc
										+ " erfolgreich absolviert");

						UserInfo currentUser = GlobalData.getCurrentUser();
						if (currentUser.getFinishedGoals() == null) {
							currentUser.setFinishedGoals(new ArrayList<Long>());
							currentUser.setFinishedGoalsTS(new ArrayList<Long>());
						}
						if (GlobalData.getDatastore() != null) {
							currentUser.getFinishedGoals().add(idgoals);
							currentUser.getFinishedGoalsTS().add(System.currentTimeMillis());
							GlobalData.getDatastore().updateUser(currentUser);
						}
					}
				}
			});
			if (cc.successfullyInitialized()) {
				Log.i("TLDR", "try register" + cc.getIdentifier());
				if (triggerRegister.register(cc)) {
					Log.i("TLDR", "done register" + cc.getIdentifier());
					this.registeredGoals.add(goal);
				}else{
					Log.e("TLDR", "fail register" + cc.getIdentifier());
				}
			}
		}
		return true;
	}

	//
	// public UserInfoDatastore getDatastore() {
	// return datastore;
	// }
	//
	// public void setDatastore(UserInfoDatastore datastore) {
	// this.datastore = datastore;
	// }

	public interface OnTrue {
		public void onTrue();
	}

}
