package com.tldr.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.nfc.FormatException;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.tldr.gamelogic.GoalStructure;

public class JsonParser {
	
	

	public static GoalStructure parseJsonGoalString(String goalString) throws FormatException {
		GoalStructure gs = new GoalStructure();
		JsonReader reader = new JsonReader(new StringReader(goalString));
		String desc="";
		String callback="";
		try {
			reader.beginObject();
			String name;
			while(reader.hasNext()){
				name=reader.nextName();
				if(name.equals("conditions"))
					parseConditions(reader, gs);
				else
					if(name.equals("rewards"))
						parseRewards(reader, gs);
					else
						if(name.equals("description"))
							desc=reader.nextString();
						else
							if(name.equals("callback")){
								callback=reader.nextString();
							}
							else{
								Log.e("TLDR", "Unknown name in Json String: "+name);
								throw new FormatException("Unknown name in Json String: "+name);
							}
			}
			
				
			
			reader.endObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gs.addBaseData(desc, callback);
		
		return gs;
	}
	
	private static void parseConditions(JsonReader reader, GoalStructure gs) throws FormatException{
		try {
			reader.beginArray();
			while(reader.hasNext()){
				reader.beginObject();
				String data="";
				String value="";
				String op="";
				while(reader.hasNext()){
					String next=reader.nextName();
					if(next.equals("data"))
						data=reader.nextString();
					else
						if(next.equals("operator"))
							op=reader.nextString();
						else
							if(next.equals("value"))
								value=reader.nextString();
							else
								throw new FormatException("Unknown Name in Condition: "+next);
				}
				reader.endObject();
				gs.addCondition(data, op, value);
			}
			reader.endArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void parseRewards(JsonReader reader, GoalStructure gs) throws FormatException {
		try {
			reader.beginArray();
			while(reader.hasNext()){
				reader.beginObject();
				String type="";
				String value="";
				while(reader.hasNext()){
					String next=reader.nextName();
					if(next.equals("type"))
						type=reader.nextString();
					else
						if(next.equals("value"))
							value=reader.nextString();
						else
							throw new FormatException("Unknown Name in Reward: "+next);
				}
				reader.endObject();
				gs.addReward(type, value);
			}
			reader.endArray();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String writeNewJsonGoalString( Map<String, Object> map) throws FormatException{
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.setIndent("  ");
			try {
			writer.beginObject();
			parseMap(writer, map);
			writer.endObject();
			writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return out.toString();
			
			
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
	}
	
	private static void parseMap(JsonWriter writer,Map<String, Object> map) throws FormatException{
		Iterator i=map.entrySet().iterator();
		while(i.hasNext()){
			Entry<String, Object> entry=(Entry<String, Object>) i.next();
			//simple Object with value
			if(entry.getValue() instanceof String){
				writeObject(writer, entry.getKey(), (String)entry.getValue());
			}else
				if(entry.getValue() instanceof List){
					writeArray(writer, (List<Map<String, Object>>) entry.getValue(), entry.getKey());
				}
				else
					if(entry.getValue() instanceof Map){
						parseMap(writer, (Map<String, Object>) entry.getValue());
					}
					else{
						Log.e("TLDR", "ERROR WHILE PARSING. WRONG FORMAT!");
						throw new FormatException("Wrong JsonParseFormat");
					}
			
		}
		
	}
	
	private static void writeObject(JsonWriter writer, String key, String value){
		try {
			writer.name(key).value(value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("TLDR", e.getMessage());
		}
	}
	
	private static void writeArray(JsonWriter writer, List<Map<String, Object>> list, String key) throws FormatException{
		try{
			writer.name(key);
			writer.beginArray();
			
			for(Map<String, Object> item:list){
				writer.beginObject();
					parseMap(writer, item);
				writer.endObject();
			}
			
			writer.endArray();
		} catch(IOException e){
			e.printStackTrace();
		}
	}


}
