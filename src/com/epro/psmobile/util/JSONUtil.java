package com.epro.psmobile.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {

	public JSONUtil() {
		// TODO Auto-generated constructor stub
	}
	private static final int DEFAULT_INT = -1;
	private static final String DEFAULT_STRING = null;
	
	@SuppressWarnings("unused")
	private static int GetInt(JSONObject jo, String key) throws JSONException{
		try {
			return jo.getInt(key);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return DEFAULT_INT;
	}
	
	@SuppressWarnings("unused")
	private static String GetString(JSONObject jo, String key) throws JSONException{
		try {
			return jo.getString(key);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return DEFAULT_STRING;
	}
	
	@SuppressWarnings("unused")
	private static JSONArray GetArray(JSONObject jo, String key) throws JSONException{
		try {
			return jo.getJSONArray(key);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get response code
	 * 
	 * @param jo
	 * @return
	 * @throws JSONException
	 */
	public static String GetCode(JSONObject jo) throws JSONException {
		return jo.getString("Code");
	}

	/**
	 * Get response message
	 * 
	 * @param jo
	 * @return
	 * @throws JSONException
	 */
	public static String GetMessage(JSONObject jo) throws JSONException {
		return jo.getString("Message");
	}

	/**
	 * Get Data
	 * 
	 * @param jo
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject GetData(JSONObject jo) throws JSONException {
		return jo.getJSONObject("Data");
	}

	/**
	 * Get JSONArray from Data
	 * @param jo
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray GetDataAsArray(JSONObject jo) throws JSONException {
		return jo.getJSONArray("Data");
	}

}
