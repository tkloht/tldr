package com.tldr.gamelogic;

import java.util.ArrayList;
import java.util.List;

public class GoalRegister {
	
	private List<GoalStructure> registeredGoals;
	
	public GoalRegister(){
		this.registeredGoals = new ArrayList<GoalStructure>();
	}
	
	/**
	 * 
	 * @param goal
	 * @return true wenn goal noch nicht existierte, false wenn schon
	 */
	public boolean addGoal(GoalStructure goal){		
		return true;
	}

}
