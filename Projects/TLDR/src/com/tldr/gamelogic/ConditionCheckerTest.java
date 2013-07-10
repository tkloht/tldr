package com.tldr.gamelogic;

import java.util.HashMap;

import android.nfc.FormatException;

import com.tldr.GlobalData;
import com.tldr.exlap.TriggerRegister;
import com.tldr.tools.JsonParser;

public class ConditionCheckerTest {

//	public static void test() {
//		HashMap<String, String> condition = new HashMap<String, String>();
//		condition.put("data", "Speed");
//		condition.put("operator", "le");
//		condition.put("value", "50");
//		System.out.println(condition);
//		ConditionCheck cc = new ConditionCheck(condition,null);
//		System.out.println(cc.updateData("49"));
//		System.out.println(cc.updateData("50"));
//	}
//
//	public static void test2() {
//		HashMap<String, String> condition = new HashMap<String, String>();
//		condition.put("data", "CurrentGear");
//		condition.put("operator", "eq");
//		condition.put("value", "3");
//		System.out.println(condition);
//		ConditionCheck cc = new ConditionCheck(condition,null);
//		System.out.println(cc.updateData("2"));
//		System.out.println(cc.updateData("3"));
//		TriggerRegister tr = new TriggerRegister();
//		tr.register(cc);
//	}

	public static void test3() {
		GoalRegister goalRegister = GlobalData.getGoalRegister();
		String goalString = "{ \"rewards\": [ { \"value\": \"2000\", \"type\": \"xp\" } ], \"conditions\": [ { \"value\": \"3\", \"data\": \"CurrentGear\", \"operator\": \"eq\" } ], \"callback\": \"null\", \"description\": \"Schalte in den 3. Gang\" }";
		GoalStructure gs = null;
		try {
			gs = JsonParser.parseJsonGoalString(goalString);
			gs.setId(12345l);
		} catch (FormatException e) {

		}
		goalRegister.addGoal(gs);
	}

}
