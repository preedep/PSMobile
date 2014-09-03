package com.epro.psmobile.util;

import java.sql.Timestamp;
import java.util.Date;

import org.json.JSONObject;

import android.util.Log;

public class JSONDataUtil {

	public JSONDataUtil() {
		// TODO Auto-generated constructor stub
	}
	public static void put(JSONObject jsonObj,String attributeName,Timestamp value)
	{
		try{
			if (value == null){
				jsonObj.put(attributeName, "");
			}else
				jsonObj.put(attributeName, value.toString());
		}catch(Exception ex){
			Log.d("DEBUG_D", "JSONDataUtil:putString error : "+ex.getMessage());
		}
	}
	public static void put(JSONObject jsonObj,String attributeName,Date value)
	{
		try{
			if (value == null){
				jsonObj.put(attributeName, "");
			}else
				jsonObj.put(attributeName, DataUtil.convertDateToStringYYYYMMDD(value));
		}catch(Exception ex){
			Log.d("DEBUG_D", "JSONDataUtil:putString error : "+ex.getMessage());
		}
	}

	public static void put(JSONObject jsonObj,String attributeName,int value)
	{
		try{
			jsonObj.put(attributeName, value);
		}catch(Exception ex){
			Log.d("DEBUG_D", "JSONDataUtil:putString error : "+ex.getMessage());
		}		
	}
	public static void put(JSONObject jsonObj,String attributeName,double value)
	{
		try{
			jsonObj.put(attributeName, value);
		}catch(Exception ex){
			Log.d("DEBUG_D", "JSONDataUtil:putString error : "+ex.getMessage());
		}		
	}
	public static void put(JSONObject jsonObj,String attributeName,String value){
		put(jsonObj,attributeName,value,false);	
	}
	public static void put(JSONObject jsonObj,String attributeName,String value,boolean needNull)
	{
		try{
			if ((value == null)||(value.trim().equalsIgnoreCase("null")))
			{
				if (needNull){
					jsonObj.put(attributeName, "null");					
				}else{
					jsonObj.put(attributeName, "");
				}
			}else
				jsonObj.put(attributeName, value);
		}catch(Exception ex){
//			jsonObj.put(attributeName, "");
			Log.d("DEBUG_D", "JSONDataUtil:putString error : "+ex.getMessage());
		}
	}
	public static String getString(JSONObject jsonObj,String attributeName)
	{
		String data = "";
		try{
			data = jsonObj.getString(attributeName);
			if (data.equalsIgnoreCase("null"))
			{
				data = "";
			}
		}catch(Exception ex)
		{
			Log.d("DEBUG_D", "JSONDataUtil:getString error : "+ex.getMessage());
		}
		return data;
	}
	
	public static int getInt(JSONObject jsonObj,String attributeName)
	{
		int data = 0;
		try{
			data = Integer.parseInt(
						getString(jsonObj,attributeName)
					);
		}catch(Exception ex){}
		return data;
	}
	
	public static double getDouble(JSONObject jsonObj,String attributeName)
	{
		double data = 0;
		try{
			data = Double.parseDouble(
						getString(jsonObj,attributeName)
					);
		}catch(Exception ex){}		
		return data;
	}
	public static boolean getBoolean(JSONObject jsonObj,String attributeName)
	{
		boolean data = false;
		try{
			data = Boolean.parseBoolean(getString(jsonObj,attributeName));
		}catch(Exception ex){}
		return data;
	}
}
