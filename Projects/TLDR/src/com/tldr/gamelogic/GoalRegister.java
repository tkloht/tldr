package com.tldr.gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.tldr.GlobalData;
import com.tldr.exlap.TriggerRegister;

public class GoalRegister {
	
	private List<GoalStructure> registeredGoals;
	private TriggerRegister triggerRegister;
	
	public GoalRegister(){
		this.registeredGoals = new ArrayList<GoalStructure>();
		this.triggerRegister = GlobalData.getTriggerRegister();
	}
	
	/**
	 * 
	 * @param goal
	 * @return true wenn goal noch nicht existierte, false wenn schon
	 */
	public boolean addGoal(GoalStructure goal){
		final String desc = goal.getDescription();
		this.registeredGoals.add(goal);
		List<Map<String, String>> conditionsList= (List<Map<String, String>>) goal.getJsonParse().get("conditions");
		for(Map<String, String> condition:conditionsList){
			ConditionCheck cc = new ConditionCheck(condition,new OnTrue() {
				
				@Override
				public void onTrue() {
					Log.i("TLDR", " FINISHED Condition:"+desc);					
				}
			});
			triggerRegister.register(cc);
		}
		return true;
	}
	
	public interface OnTrue{
		public void onTrue();
	}

}
