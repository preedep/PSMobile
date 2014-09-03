package com.epro.psmobile.remote.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.pm.PackageManager.NameNotFoundException;

import com.epro.psmobile.sync.TransferProgressListener;
import com.epro.psmobile.sync.UploadDataEntry;
import com.epro.psmobile.util.SysInfoGetter;



public class HttpUploader 
{
	/*
	 *  public final static String  MESSAGE_TYPE_CODE = "MessageTypeCode";
	 public final static String  MESSAGE_DEVICE_ID = "DeviceID";
	 public final static String  MESSAGE_DATA_ZIP  = "DataZip";
	 public final static String  MESSAGE_IMG_ZIP   = "ImgsZip";
	 public final static String  MESSAGE_PASSWORD  = "Password";
	 public final static String  MESSAGE_FILES_REQUEST = "FilesRequest";
	 public final static String  MESSAGE_RETRY_REQUEST = "Resend";
	 public final static String  MESSAGE_VERSION_ID	 	= "VersionID";
	 public final static String  MESSAGE_CONTENT_LEN  = "ContentLength";

	 */
	private final static String PART_NAME_MESSAGE_TYPE = PSMobleRemoteAPI.COMMAND_TYPE;
	private final static String PART_NAME_DATA_ZIP	   = PSMobleRemoteAPI.DATA_FILE;
	private final static String PART_NAME_DEV_ID	   = PSMobleRemoteAPI.DEVICE_ID;
	private final static String PART_CONTENT_LEN       = PSMobleRemoteAPI.CONTENT_LEN;
	private final static String PART_USER_NAME 		   = PSMobleRemoteAPI.USER_NAME;
	private final static String PART_PASSWORD 		   = PSMobleRemoteAPI.PASSWORD;
	private final static String PART_VERSION_CODE      = PSMobleRemoteAPI.VERSION_CODE;
	
	private final static String PART_JSON 			   = "json";
	
	public interface OnHttpUploadListener{
		public void onHttpUploadComplete();
	}
	private OnHttpUploadListener onHttpUploadListener;
	
	private String deviceID;
	private String url;
	
	
	public HttpUploader(String postUrl,String deviceID)
	{
		this.url = postUrl;
		this.deviceID = deviceID;
	}
	
	public Result upload(String messageTypeID,
					   String userName,
					   String password,
					   File dataZip,
					   int versionCode,
					   TransferProgressListener listener) 
	{
		Result result = new Result();
		HttpResponse response = null;
		HttpEntity resEntity = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(this.url);
		try{

		HttpParams params = httpclient.getParams();
		
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setConnectionTimeout(params, 1000 * 60 * 3);
        HttpConnectionParams.setSoTimeout(params, 1000 * 60 * 3);
        // set parameter
        HttpProtocolParams.setUseExpectContinue(params, true);
        
		
		MultipartEntity reqEntity = null;
		
		if (listener != null)
		{		
		  reqEntity = new UploadDataEntry(HttpMultipartMode.BROWSER_COMPATIBLE,listener);
		}else{
		  reqEntity	= new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		}
		/*
		StringBody userNameBody = new StringBody(userName);
		StringBody passwordBody = new StringBody(password);
		StringBody deviceIDBody = new StringBody(this.deviceID);
		StringBody messageTypeIDBody = new StringBody(messageTypeID);
		
		reqEntity.addPart(HttpUploader.PART_USER_NAME, userNameBody);
		reqEntity.addPart(HttpUploader.PART_PASSWORD, passwordBody);
		reqEntity.addPart(HttpUploader.PART_NAME_DEV_ID, deviceIDBody);
		reqEntity.addPart(HttpUploader.PART_NAME_MESSAGE_TYPE, messageTypeIDBody);
		*/
		JSONObject dataInput = new JSONObject();
		dataInput.put(PSMobleRemoteAPI.COMMAND_TYPE, PSMobleRemoteAPI.CMD_TYPE_UPLOAD);
		dataInput.put(PSMobleRemoteAPI.USER_NAME, userName);
		dataInput.put(PSMobleRemoteAPI.PASSWORD, password);
		dataInput.put(PSMobleRemoteAPI.DEVICE_ID, deviceID);
		dataInput.put(PSMobleRemoteAPI.LANG_CODE, "041e");	
		dataInput.put(PSMobleRemoteAPI.VERSION_CODE, versionCode);

		
		StringBody strJsonBody = new StringBody(dataInput.toString());
		reqEntity.addPart(HttpUploader.PART_JSON, strJsonBody);
		
		
		FileBody dataZipBody  = null;
		//FileBody imgZipBody = null;
		long contentLen = 0;
		if (dataZip != null){
			dataZipBody = new FileBody(dataZip);			
			reqEntity.addPart(HttpUploader.PART_NAME_DATA_ZIP, dataZipBody);
			contentLen += dataZipBody.getFile().length();
		}
		
		reqEntity.addPart(PART_CONTENT_LEN, new StringBody(contentLen+""));
		if (reqEntity instanceof UploadDataEntry)
		{
			((UploadDataEntry)reqEntity).setContentLength(contentLen);
		}
		httppost.setEntity(reqEntity);
		
		
		response = httpclient.execute(httppost);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		{
			resEntity = response.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
			    builder.append(line).append("\n");
			}
//			JSONTokener tokener = new JSONTokener(builder.toString());
//			JSONObject obj = (JSONObject) tokener.nextValue();
			result.setIsSuccessful(true);
			result.setResult(new JSONObject(builder.toString()));
			
		}else
		{
			String err = response.getStatusLine().getReasonPhrase();			
			result.setException(new Exception(err));
		  }
		}
		catch(Exception ex){
			result.setException(ex);
		}
		finally{
			try{
				if (resEntity != null)
				{			
					InputStream is = resEntity.getContent();
					is.close();
				}
			}catch(Exception ex){}
			
			try{
				
				if (httpclient != null)
				{
					httpclient.getConnectionManager().shutdown();
				}
			}catch(Exception ex){}
			
			//result = null;
			response = null;
			resEntity = null;
			httpclient = null;
			
			System.gc();
		}
		return result;
	}
	/**
	 * @param onHttpUploadListener the onHttpUploadListener to set
	 */
	public void setOnHttpUploadListener(OnHttpUploadListener onHttpUploadListener) {
		this.onHttpUploadListener = onHttpUploadListener;
	}
	/**
	 * @return the onHttpUploadListener
	 */
	public OnHttpUploadListener getOnHttpUploadListener() {
		return onHttpUploadListener;
	}
}
