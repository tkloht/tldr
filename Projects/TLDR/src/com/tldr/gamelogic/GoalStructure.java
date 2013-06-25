package com.tldr.gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoalStructure {

	private Map<String, Object> jsonParse;
	
	public GoalStructure(){
		jsonParse=new HashMap<String, Object>();
		
	}
	
	public GoalStructure addCondition(String data, String operator, String value){
		List<Map<String, String>> conditionsList;
		if(jsonParse.containsKey("conditions")){
			conditionsList = (List<Map<String, String>>) jsonParse.get("conditions");
		}
		else {
			conditionsList = new ArrayList<Map<String, String>>();
		}
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("data", data);
		condition.put("operator", operator);
		condition.put("value", value);
		conditionsList.add(condition);
		jsonParse.put("conditions", conditionsList);
		return this;
	}
	
	public GoalStructure addReward(String type, String value){
		List<Map<String, String>> rewardsList;
		if(jsonParse.containsKey("rewards")){
			rewardsList = (List<Map<String, String>>) jsonParse.get("rewards");
		}
		else {
			rewardsList = new ArrayList<Map<String, String>>();
		}
		HashMap<String, String> reward = new HashMap<String, String>();
		reward.put("type", type);
		reward.put("value", value);
		rewardsList.add(reward);
		jsonParse.put("rewards", rewardsList);
		return this;
	}
	
	public GoalStructure addBaseData(String description, String callback_uri){
		jsonParse.put("callback", callback_uri);
		jsonParse.put("description", description);
		return this;
	}
	
	public Map<String, Object> getJsonParse()
	{
		return jsonParse;
	}
	
	
}
