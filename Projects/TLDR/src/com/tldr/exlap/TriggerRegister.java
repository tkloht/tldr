package com.tldr.exlap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.tldr.GlobalData;
import com.tldr.gamelogic.ConditionCheck;

import de.exlap.DataObject;

public class TriggerRegister {

	public enum TriggerDomains {
		GPS, EXLAP
	}

	private Map<String, List<ConditionCheck>> registertConditions;

	public TriggerRegister() {
		this.registertConditions = new HashMap<String, List<ConditionCheck>>();
	}

	public boolean register(ConditionCheck conditionCheck) {
		String identifier = conditionCheck.getIdentifier();
		List<ConditionCheck> cList;
		boolean registerExlapInterface = false;
		if (this.registertConditions.containsKey(identifier)) {
			cList = this.registertConditions.get(identifier);
		} else {
			cList = new ArrayList<ConditionCheck>();
			registerExlapInterface = true;
		}
		switch (conditionCheck.getDomain()) {
		case GPS:

			break;

		case EXLAP:
			if (GlobalData.isExlapConnected() && registerExlapInterface) {
				if (GlobalData.getConnectionHelper() != null) {
					GlobalData.getConnectionHelper().subscribe(identifier);
				}
			} else {
				if (this.registertConditions.containsKey(identifier)) {
					break;
				}
				Log.i("Trigerregister", "Skipted no Exlap");
				return false;
			}
			break;

		default:
			break;
		}
		this.registertConditions.put(identifier, cList);
		Log.i("Trigerregister", "Condition inserted");
		cList.add(conditionCheck);
		return true;
	}

	public void onNewData(TriggerDomains domain, Object data) {
		Object value = null;
		String key = null;
		switch (domain) {
		case EXLAP:
			DataObject dataObject = (DataObject) data;
			if (dataObject.size() > 0) {
				if (this.registertConditions.containsKey(dataObject.getUrl())) {
					key = dataObject.getUrl();
					if (dataObject.getUrl().equals("SeatBeltLock")) {
						value = dataObject.getElement(1).getValue();
					} else {
						value = dataObject.getElement(0).getValue() + "";
					}
				}
			}
			break;
		case GPS:
			key = "gps";
			value = data;
			break;

		default:
			break;
		}

		if (key != null && this.registertConditions.get(key) != null) {
			List<ConditionCheck> cList = this.registertConditions.get(key);
			List<ConditionCheck> done = new ArrayList<ConditionCheck>();
			for (ConditionCheck cc : cList) {
				if (value != null) {
					if (cc.updateData(value)) {
						done.add(cc);
					}
					// TODO if ture dann unsuscriben
				}
			}
			for (ConditionCheck ccd : done) {
				cList.remove(ccd);
			}
			// if (cList.isEmpty()) {
			// this.registertConditions.remove(key);
			// if(domain ==TriggerDomains.EXLAP){
			// GlobalData.getConnectionHelper().unsubscribe(key);
			// Log.i("TLDR", "Unsubscribe" + key);
			// }
			// }
		}

	}
}
