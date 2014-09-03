package com.epro.psmobile.util;

import java.util.UUID;

import com.epro.psmobile.sys.DeviceUuidFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

public class SysInfoGetter {

	private static String uniqueID = null;
	private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

	//private static boolean isTest = false;
	public SysInfoGetter() {
		// TODO Auto-generated constructor stub
	}

	public static String getDeviceID(Context ctxt){
		/*
		if (!isTest){
			DeviceUuidFactory deviceFactory = new DeviceUuidFactory(ctxt);
			return deviceFactory.getDeviceUuid().toString();
		}else{
			return "83cc7d16-d60e-31cc-a9ef-094770ebebc0";
		}
		*/
		/*
		boolean isDebuggable = (0 != (ctxt.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
		if (isDebuggable)
		{
			return "83cc7d16-d60e-31cc-a9ef-094770ebebc0";			
		}else{
			DeviceUuidFactory deviceFactory = new DeviceUuidFactory(ctxt);
			return deviceFactory.getDeviceUuid().toString();
		}
		 */
		DeviceUuidFactory deviceFactory = new DeviceUuidFactory(ctxt);
		return deviceFactory.getDeviceUuid().toString();
	}
	public static String getDeviceIDShort(){
		String m_szDevIDShort = "35" + //we make this look like a valid IMEI
	            Build.BOARD.length()%10+ Build.BRAND.length()%10 +
	            Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
	            Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
	            Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
	            Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
	            Build.TAGS.length()%10 + Build.TYPE.length()%10 +
	            Build.USER.length()%10 ; //13 digits
		return m_szDevIDShort;
	}
	
	public synchronized static String getPreferenceUniqueID(Context context) {
	    if (uniqueID == null) {
	        SharedPreferences sharedPrefs = context.getSharedPreferences(
	                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
	        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
	        if (uniqueID == null) {
	            uniqueID = UUID.randomUUID().toString();
	            Editor editor = sharedPrefs.edit();
	            editor.putString(PREF_UNIQUE_ID, uniqueID);
	            editor.commit();
	        }
	    }
	    return uniqueID;
	}
	
	public static int getAppVersionCode(Context context) throws NameNotFoundException
	{
	   int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
	   return versionCode;
	}
	public static String getAppVersionName(Context context) throws NameNotFoundException
	{
	   String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
       return versionName;
	}
}
