package com.tldr.exlap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tldr.GlobalData;
import com.tldr.gamelogic.ConditionCheck;

import de.exlap.DataObject;

public class TriggerRegister {

	public enum TriggerDomains {
		GPS, EXLAP
	}

	private Map<String, List<ConditionCheck>> exlapCondition;

	public TriggerRegister() {
		this.exlapCondition = new HashMap<String, List<ConditionCheck>>();
	}

	public void register(ConditionCheck conditionCheck) {
		String identifier = conditionCheck.getIdentifier();
		switch (conditionCheck.getDomain()) {
		case GPS:

			break;

		case EXLAP:
			List<ConditionCheck> cList;
			if (this.exlapCondition.containsKey(identifier)) {
				cList = this.exlapCondition.get(identifier);
			} else {
				cList = new ArrayList<ConditionCheck>();
				this.exlapCondition.put(identifier, cList);
				GlobalData.getConnectionHelper().subscribe(identifier);
			}
			cList.add(conditionCheck);
			break;

		default:
			break;
		}
	}

	public void onNewData(TriggerDomains domain, Object data) {
		switch (domain) {
		case EXLAP:
			DataObject dataObject = (DataObject) data;
			if (dataObject.size() > 0) {
				if (this.exlapCondition.containsKey(dataObject.getUrl())) {
					List<ConditionCheck> cList = this.exlapCondition
							.get(dataObject.getUrl());
					List<ConditionCheck> done = new ArrayList<ConditionCheck>();
					for (ConditionCheck cc : cList) {
						Object value = dataObject.getElement(0).getValue();
						if (value != null) {
							if(cc.updateData(value)){
								done.add(cc);
							}
							//TODO if ture  dann unsuscriben
						}
					}
					for(ConditionCheck ccd: done){
						cList.remove(ccd);
					}
				}
			}
			break;

		default:
			break;
		}

	}
}
