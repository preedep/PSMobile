package com.epro.psmobile.data.download;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.epro.psmobile.remote.api.JSONDataHolder;

public class ResultSyncDownloadData implements JSONDataHolder {

	private final static String COLUMN_TOKEN_KEY = "tokenKey";
	private final static String COLUMN_DATA_ZIP_URL = "data_zip_file_url";
	
	private String tokenKey;
	private String dataZipUrl;
	
	public ResultSyncDownloadData() {
		// TODO Auto-generated constructor stub
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

	public String getDataZipUrl() {
		return dataZipUrl;//.replace(".zip", "x.zip");
	}

	public void setDataZipUrl(String dataZipUrl) {
		this.dataZipUrl = dataZipUrl;
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		try{
			this.tokenKey = jsonObj.getString(COLUMN_TOKEN_KEY);
		}catch(Exception ex)
		{
			Log.d("DEBUG_D", "ResultSyncDownload json error " + ex.getMessage());
		}
		try{	
			this.dataZipUrl = jsonObj.getString(COLUMN_DATA_ZIP_URL);
		}catch(Exception ex){
			Log.d("DEBUG_D", "ResultSyncDownload json error " + ex.getMessage());			
		}
	}

	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
