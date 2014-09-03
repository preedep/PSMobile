package com.epro.psmobile.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

	private final static String PREF_NAME = "com.epro.psmobile.app.state.pref_name";
	private final static String KEY_TEAM_CHK = "teamChkInKey";
	private final static String KEY_DASH_BOARD_VIEW = "dashBoardView";
	private final static String KEY_JOB_VIEW = "jobTaskView";
	
	private final static String DOWNLOAD_FOLDER = "com.epro.psmobile.app.state.downloadFolder";
	
	private final static String USER_NAME = "com.epro.psmobile.app.state.USER_NAME";
	private final static String PASSWORD = "com.epro.psmobile.app.state.PASSWORD";
	private final static String DEVICE_ID = "com.epro.psmobile.app.state.DEVICE_ID";

	private final static String TEAM_ID = "com.epro.psmobile.app.state.TEAM_ID";
	
	private final static String SERVICE_URL = "com.epro.psmobile.app.state.SERVICE_URL";

	private final static String TEXT_VERSION = "com.epro.psmobile.app.state.TEXT_VERSION";
	
	private final static String KEY_LAYOUT_MODIFED = "com.epro.psmobile.app.state.KEY_LAYOUT_MODIFIED";
    private final static String KEY_CAR_INSPECT_MODIFED = "com.epro.psmobile.app.state.KEY_CAR_INSPECT_MODIFED";

    private final static String KEY_LAST_TIME_OPEN_JOB = "com.epro.psmobile.app.state.KEY_LAST_TIME_OPEN_JOB";
    
    
    private final static String KEY_COMMENTED_SAVED = "KEY_COMMENTED_SAVED";
	public SharedPreferenceUtil() {
		// TODO Auto-generated constructor stub
	}
	public static boolean isAlreadyCommentSaved(Context ctxt)
	{
	   SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
       return pref.getBoolean(KEY_COMMENTED_SAVED, true);

	}
	public static void setAlreadyCommentSaved(Context ctxt,boolean alreadySaved)
    {
       SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
       SharedPreferences.Editor prefEditor = pref.edit();
       prefEditor.putBoolean(KEY_COMMENTED_SAVED, alreadySaved);
       prefEditor.commit();
    }
	public static long getLastTimeOpenJob(Context ctxt){
	   SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
       return pref.getLong(KEY_LAST_TIME_OPEN_JOB, 0);

	}
	public static void setLastTimeOpenJob(Context ctxt,long timeOpenJob)
	{
	   SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
       SharedPreferences.Editor prefEditor = pref.edit();
       prefEditor.putLong(KEY_LAST_TIME_OPEN_JOB, timeOpenJob);
       prefEditor.commit();
	}
	public static boolean getCarInspectDataModified(Context ctxt){
       SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
       return pref.getBoolean(KEY_CAR_INSPECT_MODIFED, false);
	}
	public static boolean getLayoutModified(Context ctxt)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return pref.getBoolean(KEY_LAYOUT_MODIFED, false);
	}
	public static void saveCarInspectDataModified(Context ctxt,boolean hasModified)
    {
        SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putBoolean(KEY_CAR_INSPECT_MODIFED, hasModified);
        prefEditor.commit();
    }
	public static void saveLayoutModified(Context ctxt,boolean hasModified)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putBoolean(KEY_LAYOUT_MODIFED, hasModified);
		prefEditor.commit();
	}

	public static void saveTextVersion(Context ctxt,String textVerion)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(TEXT_VERSION, textVerion);
		prefEditor.commit();
	}
	public static String getTextVersion(Context ctxt)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return pref.getString(TEXT_VERSION, "");
	}
	public static void saveServiceUrl(Context ctxt,String ServiceUrl)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(SERVICE_URL, ServiceUrl);
		prefEditor.commit();
	}
	public static String getServiceUrl(Context ctxt)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return pref.getString(SERVICE_URL, "");
	}
	public static int getTeamID(Context ctxt)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return pref.getInt(TEAM_ID, 0);//(DOWNLOAD_FOLDER, "");
		
	}
	public static String getDownloadFolder(Context ctxt)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return pref.getString(DOWNLOAD_FOLDER, "");
	}
	public synchronized static String getUserName(Context ctxt)
	{
		if (ctxt != null){
			SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			return pref.getString(USER_NAME, "");
		}else{
			return "";
		}
	}
	public synchronized static String getPassword(Context ctxt)
	{
	    if (ctxt != null){
	       SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	       return pref.getString(PASSWORD, "");
	    }else{
	       return "";
	    }
	}	
	public synchronized static String getDeviceId(Context ctxt)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return pref.getString(DEVICE_ID, "");		
	}
	public static void saveTeamID(Context ctxt,int teamID)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putInt(TEAM_ID, teamID);
		prefEditor.commit();
	}
	public static void saveUserPassword(Context ctxt,String userName,String password,String deviceId)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(USER_NAME, userName);
		prefEditor.putString(PASSWORD, password);
		prefEditor.putString(DEVICE_ID, deviceId);
		prefEditor.commit();
	}
	public static void saveDownloadFolder(Context ctxt,String folder)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(DOWNLOAD_FOLDER, folder);
		prefEditor.commit();
	}
	public static boolean checkTeamAlreadyCheckIn(Context ctxt){
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		boolean bRet =  pref.getBoolean(KEY_TEAM_CHK, false);
		return bRet;
	}
	public static void setStateTeamCheckIn(Context ctxt,boolean bCheckIn)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putBoolean(KEY_TEAM_CHK, bCheckIn);  
		prefEditor.commit();	
	}
	public static boolean getStateTeamCheckIn(Context ctxt)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return pref.getBoolean(KEY_TEAM_CHK,false);
	}
	////////////////
	public static boolean checkHomeShownInListView(Context ctxt){
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		boolean bRet =  pref.getBoolean(KEY_DASH_BOARD_VIEW, false);
		return bRet;		
	}
	public static void setHomeShownInListView(Context ctxt,boolean bListView)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putBoolean(KEY_DASH_BOARD_VIEW, bListView);  
		prefEditor.commit();	
	}

	public static void setJobAndTaskInListView(Context ctxt,boolean bListView)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putBoolean(KEY_JOB_VIEW, bListView);  
		prefEditor.commit();			
	}
	public static boolean checkJobAndTaskShownInListView(Context ctxt)
	{
		SharedPreferences pref = ctxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		boolean bRet =  pref.getBoolean(KEY_JOB_VIEW, true);
		return bRet;
	}
}
