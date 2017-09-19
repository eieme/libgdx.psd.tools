package com.keyroy.gdx.tools;

import org.json.m.JSONArray;
import org.json.m.JSONObject;

public class JsonPack {
	private String name;
	//private JSONArray jsonArray;
	private JSONObject json;

	public JsonPack(String name, JSONObject json) {
		this.name = name;
		this.json = json;
	}

	public String getName() {
		return name;
	}

//	public JSONArray getJsonArray() {
//		return jsonArray;
//	}
	public JSONObject getJsonObject() {
		return json;
	}
}
