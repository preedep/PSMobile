package com.epro.psmobile.remote.api;

import org.json.JSONObject;

public class ServiceParameters  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3785630038175897025L;
	private JSONObject jsonParam = null;
	public ServiceParameters()
	{
		jsonParam = new JSONObject();
	}
	public void addParam(String paramName,
						 String value)
	throws Exception
	{
		jsonParam.put(paramName, value);
	}
	public String toString(){
		return jsonParam.toString();
	}
}
