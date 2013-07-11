package com.tldr.gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.tldr.GlobalData;
import com.tldr.com.tldr.userinfoendpoint.model.UserInfo;
import com.tldr.exlap.TriggerRegister;

public class GoalRegister {

	private List<GoalStructure> registeredGoals;
	private TriggerRegister triggerRegister;
	private Map<Long, Integer> checkedConditions;
	private Handler handler;

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
			Log.i("TLDR", " REGISTER Condition:" + condition.toString());
			ConditionCheck cc = new ConditionCheck(condition, new OnTrue() {
				@Override
				public boolean onTrue() {
					// Hier muessten erstenaml allee contidions true sein bevor
					// isch fnished gloals setze

					if (checkedConditions.get(idgoals) > 0) {
						checkedConditions.put(idgoals,
								checkedConditions.get(idgoals) - 1);
					}
					Log.i("TLDR", " DONE Condition:" + idgoals);
					Log.i("TLDR", " DONE Condition:" + condition);
					Log.i("TLDR",
							" DONE Condition:" + checkedConditions.get(idgoals));
					if (checkedConditions.get(idgoals) == 0) {
						UserInfo currentUser = GlobalData.getCurrentUser();
						if (currentUser.getFinishedGoals() == null) {
							currentUser.setFinishedGoals(new ArrayList<Long>());
							currentUser
									.setFinishedGoalsTS(new ArrayList<Long>());
						}
						if (GlobalData.getDatastore() != null) {
							currentUser.getFinishedGoals().add(idgoals);
							currentUser.getFinishedGoalsTS().add(
									System.currentTimeMillis());
							GlobalData.getDatastore().updateUser(currentUser);
						}

						if (GlobalData.isParentGoalFinished(idgoals)) {
							String descToSpeech = desc;
							if (desc.contains("##"))
								descToSpeech = desc.split("##")[1];
							GlobalData.getTextToSpeach().say(
									"Annomalie Parameter " + descToSpeech
											+ " erfolgreich absolviert");
							LocalBroadcastManager broadcastManager = GlobalData.getBroadcastManager();
							Log.i("tldr-exlap", "goald id: " + idgoals);
							Intent notificationIntent = new Intent("android.intent.action.GOAL_NOTIFICATION");
							notificationIntent.putExtra("GOAL_ID", idgoals);
							notificationIntent.putExtra("GOAL_DESC", descToSpeech);
							broadcastManager.sendBroadcast(notificationIntent);
						}

						

						if (GlobalData.isTaskCompleted(GlobalData
								.getTaskForGoalId(idgoals))) {
							if (handler != null) {
								Message msg = new Message();
								msg.obj=GlobalData
										.getTaskForGoalId(idgoals);
								handler.sendMessage(msg);
							}
						}

						return true;
					}
					if (checkedConditions.get(idgoals) < 0) {
						return true;
					}
					return false;
				}
			}, new OnFalse() {

				@Override
				public boolean onFalse() {
					Log.i("TLDR",
							"UNDONE Condition:"
									+ checkedConditions.get(idgoals));
					checkedConditions.put(idgoals,
							checkedConditions.get(idgoals) + 1);
					return false;
				}
			});
			if (cc.successfullyInitialized()) {
				Log.i("TLDR", "try register" + cc.getIdentifier());
				if (triggerRegister.register(cc)) {
					Log.i("TLDR", "done register" + cc.getIdentifier());
					this.registeredGoals.add(goal);
				} else {
					Log.e("TLDR", "fail register" + cc.getIdentifier());
				}
			}
		}
		return true;
	}

	public interface OnTrue {
		public boolean onTrue();
	}

	public interface OnFalse {
		public boolean onFalse();
	}

	public void setDialogHandler(Handler doneHandler) {
		this.handler = doneHandler;
	}

}
