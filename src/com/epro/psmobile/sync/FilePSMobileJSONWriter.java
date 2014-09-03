package com.epro.psmobile.sync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.CommonValues;

public class FilePSMobileJSONWriter {

	public FilePSMobileJSONWriter() {
		// TODO Auto-generated constructor stub
	}

	public static boolean writeJSONArrayToFile(
			Context context,
			String copyToFolder,
			String fileName,
			ArrayList<JSONDataHolder> jsonDataList) throws JSONException, IOException
	{
		boolean bRet = false;
		//JSONObject jsonObjectRoot = new JSONObject();
		
		JSONArray jsonArray = new JSONArray();
		for(JSONDataHolder dataItem : jsonDataList)
		{
			jsonArray.put(dataItem.getJSONObject());
		}
		/*
		String txtJsonArray = 
				jsonArray.toString();
		*/
		//jsonObjectRoot.put("data", jsonArray);
		
//		String rootFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
//		String deviceId = AppStateUtil.getDeviceId(context);		
//		File folderUploader = new File(rootFolder+"/"+CommonValues.UPLOAD_FOLDER+"/"+deviceId);
		File folderUploader = new File(copyToFolder);
		if (!folderUploader.exists())
		{
			folderUploader.mkdirs();
		}
		if (!fileName.endsWith(".json"))
			fileName += ".json";
		File fJson = new File(folderUploader.getAbsoluteFile()+"/"+fileName);
		if (fJson.exists()){
			fJson.delete();
		}
		FileUtils.writeStringToFile(fJson, jsonArray.toString(),"UTF-8");
//		FileUtils.write(fJson, txtJsonArray, "UTF-8");
		bRet = true;
		return bRet;
	}
}
