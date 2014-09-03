package com.epro.psmobile.remote.api;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class RestAPIService {

	
	public RestAPIService() {
		// TODO Auto-generated constructor stub
	}
	
	public static String CallWS(String url, 
			JSONObject data, String token) throws ClientProtocolException, IOException{

		final DefaultHttpClient httpClient=new DefaultHttpClient();
		// request parameters
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 1000 * 60 * 3);
		HttpConnectionParams.setSoTimeout(params, 1000 * 60 * 3);
		
		// set parameter
		HttpProtocolParams.setUseExpectContinue(httpClient.getParams(), true);

		// POST the envelope
		HttpPost httppost = new HttpPost(url);
		// add headers
		httppost.setHeader("Accept", "application/json");
	    httppost.setHeader("Content-type", "application/json; charset=utf-8");
//	    httppost.setHeader("Content-Length", ""+(data!= null? data.toString().getBytes().length: 0));
		if(null != token){
			Log.d("DEBUG_D", "Connect with token: " + token);
			httppost.setHeader("authtoken", token);
		}else{
			Log.d("DEBUG_D", "Connect without token ");
		}
//		httppost.setHeader("authtoken", "Baf2UteIfv7xi01tUiC6xlOlySgOnaYyHmpyRlA570QqmdV4f9ijhqgkTt1dol5w");

		String responseString="";
		try {
			// the entity holds the request	
		    JSONObject holder = data; //getJsonObjectFromMap(bodyParams);
		    StringEntity se = new StringEntity(holder.toString(), "UTF-8");
		    
			httppost.setEntity(se);

			// Response handler
			ResponseHandler<String> rh = new ResponseHandler<String>() {
				// invoked when client receives response
				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {

					if(response.getStatusLine().getStatusCode() != 200){
						Log.d("Outstream", "Status code: " + response.getStatusLine().getStatusCode());
						throw new ServiceMobileException(response.getStatusLine().getStatusCode());
					}
					
					// get response entity
					HttpEntity entity = response.getEntity();

					// read the response as byte array
					StringBuffer out = new StringBuffer();
					byte[] b = EntityUtils.toByteArray(entity);

					// write the response byte array to a string buffer
					Log.d("Outstream", String.valueOf(b));
					out.append(new String(b, 0, b.length));        
					return out.toString();
				}
			};
			
			Log.d("SOAPConnector", "CONN: " + httppost.getURI());
			responseString=httpClient.execute(httppost, rh); 
			Log.d("SOAPConnector", "Result length: " + responseString.length());
			Log.d("DEBUG_D", "Rest return = "+responseString);
		} 
		finally
		{
			// close the connection
			httpClient.getConnectionManager().shutdown();			
		}
		return responseString;
	}
}
