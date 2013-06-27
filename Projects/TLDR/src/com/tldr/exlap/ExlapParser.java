package com.tldr.exlap;

import de.exlap.DataElement;
import de.exlap.DataObject;

public class ExlapParser {
	
	public static Object getSingleValue(DataObject dataObject){
		String url = dataObject.getUrl();
		DataElement de = dataObject.getElement(0);		
		return dataObject.getElement(0).getValue();
	}

}
