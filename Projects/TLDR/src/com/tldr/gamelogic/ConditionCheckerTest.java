package com.tldr.gamelogic;

import java.util.HashMap;

import com.tldr.exlap.TriggerRegister;

public class ConditionCheckerTest {

	public static void test(){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("data", "Speed");
		condition.put("operator", "le");
		condition.put("value", "50");
		System.out.println(condition);
		ConditionCheck cc = new ConditionCheck(condition);
		System.out.println(cc.updateData("49"));
		System.out.println(cc.updateData("50"));
	}
	
	public static void test2(){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("data", "CurrentGear");
		condition.put("operator", "eq");
		condition.put("value", "3");
		System.out.println(condition);
		ConditionCheck cc = new ConditionCheck(condition);
		System.out.println(cc.updateData("2"));
		System.out.println(cc.updateData("3"));
		TriggerRegister tr = new TriggerRegister();
		tr.register(cc);
	}

}
