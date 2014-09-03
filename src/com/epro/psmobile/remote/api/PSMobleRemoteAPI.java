package com.epro.psmobile.remote.api;

import org.json.JSONObject;

import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.content.Context;

public class PSMobleRemoteAPI {
	
	public final static String COMMAND_TYPE = "cmdType";
	public final static String USER_NAME = "userName";
	public final static String PASSWORD = "password";
	public final static String DEVICE_ID = "deviceID";
	public final static String LANG_CODE = "langCode";
	public final static String CONTENT_LEN = "contentLength";
	public final static String DATA_FILE = "dataFile";
	public final static String VERSION_CODE = "versionCode";
	
	public final static String CMD_TYPE_LOGIN = "1";
	public final static String CMD_TYPE_DOWNLOAD = "2";
	public final static String CMD_TYPE_UPLOAD = "3";
	
	private Context context;
	public PSMobleRemoteAPI(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public Result login(String userName,
			String passWord,
			String deviceId,
			String langCode,
			int versionCode)
	{
		Result result = new Result();
		try{
			JSONObject dataInput = new JSONObject();
			dataInput.put(COMMAND_TYPE, CMD_TYPE_LOGIN);
			dataInput.put(USER_NAME, userName);
			dataInput.put(PASSWORD, passWord);
			dataInput.put(DEVICE_ID, deviceId);
			dataInput.put(VERSION_CODE, versionCode);
			dataInput.put(LANG_CODE, "041e");			
			
			String url = SharedPreferenceUtil.getServiceUrl(context);
			url += CommonValues.PSMOBILE_WEB_SERVICE_URL;
			String jsonResult = RestAPIService.CallWS(url,
				dataInput, 
				null);
			
			if ((jsonResult != null)&&(!jsonResult.isEmpty())){
				result.setResult(new JSONObject(jsonResult));
			}else{
				result.setException(new Exception("No data returned"));
			}
		}
		catch(Exception ex){
			result.setIsSuccessful(false);
			result.setException(ex);
		}
		return result;
	}
}
