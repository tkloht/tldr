package com.tldr.gamelogic;

import java.util.HashMap;

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

}
