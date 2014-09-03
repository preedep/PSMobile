package com.epro.psmobile.remote.api;

import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

public class Result {

	private final static String RETURN_CODE = "returnCode";
	private final static String ERROR_MESSAGE = "errorMessage";
	private final static String DATA = "data";
	private boolean bResult;
	private Exception exception;
	private JSONObject result;
	
	
	private final static String RETURN_OK = "R_OK";
	private final static String RETURN_FAILED = "R_FAILED";
	private JSONObject data;
	
	public Result()
	{
		
	}
	public boolean isSuccessful() 
	{
		return bResult;
	}
	
	public void setIsSuccessful(boolean bResult) {
		this.bResult = bResult;
	}

	
	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) throws JSONException 
	{
		this.result = result;
		
		String returnCode = result.getString(RETURN_CODE);
		if (returnCode.equalsIgnoreCase(RETURN_OK)){
			this.setIsSuccessful(true);
		}else{
			this.setIsSuccessful(false);
			
			String errorMessage = result.getString(ERROR_MESSAGE);
			if ((errorMessage != null)&&(!errorMessage.isEmpty())){
				this.setException(new Exception(errorMessage));
			}
			
		}
		
		
		this.data = result.getJSONObject(DATA);
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
		if (this.exception != null){
			this.setIsSuccessful(false);
		}
	}

	public String getErrorMessage(){
		String errorMessage = "System Error";
		if (exception != null)
			errorMessage = 
				exception.getMessage();
		
		if (errorMessage == null){
			if (exception instanceof SocketTimeoutException)
			{
				errorMessage = "Socket timeout";
			}
		}
		
		return errorMessage;
	}
	
	public <T> T getData(Class<T> type) throws InstantiationException, 
	IllegalAccessException, 
	JSONException 
	{
		T obj = type.newInstance();
		if (obj instanceof JSONDataHolder)
		{
			JSONDataHolder dataHolder = (JSONDataHolder)obj;
			dataHolder.onJSONDataBind(data);
			return obj;
		}
		return null;
	}
	
}
