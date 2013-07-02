package com.tldr.gamelogic;

import java.util.ArrayList;
import java.util.List;

public class GoalStructureFactory {

	public static List<GoalStructure> generatePolyLineGoals(String description,
			String polyLineAsString,String rewardType, int reward) {
		List<GoalStructure> lReturn = new ArrayList<GoalStructure>();
		String[] splitLatLongs = polyLineAsString.split(";");
		int count=0;
		for(String latLongString:splitLatLongs){
			GoalStructure nextPoint = new GoalStructure().addBaseData((count++)+"##"+description, "null");
			nextPoint.addCondition("polyline_point", "eq", latLongString);
			nextPoint.addReward(rewardType, reward+"");
			lReturn.add(nextPoint);
		}
		return lReturn;
	}
}
