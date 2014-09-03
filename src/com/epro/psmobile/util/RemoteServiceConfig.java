package com.epro.psmobile.util;

import java.io.IOException;
import java.util.Properties;

import com.epro.psmobile.R;

import android.content.Context;

public class RemoteServiceConfig {

	private final static String SERVICE_ACTIVE = "SERVICE_ACTIVE";
	private final static String SERVICE_TEST = "SERVICE_TEST";
	private final static String SERVICE_UAT = "SERVICE_UAT";
	private final static String SERVICE_PROD = "SERVICE_PROD";
	private final static String SERVICE_LAYOUT_TEST = "SERVICE_TEST_LAYOUT";
	
	public static String SERVICE_NAME = "";
	public static String VERSION_NAME = "";
	
	public static String getServerActived(Context context) throws Exception
	{
		String server = "";
		Properties properties = 
				AssetsPropertyReader.getProperties(context, "services.properties");
		String[] serverNames = context.getResources().getStringArray(R.array.server_name_array);
		//VERSION_NAME = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		VERSION_NAME = SysInfoGetter.getAppVersionName(context);
		if (properties != null)
		{
			/*
			 SERVICE_ACTIVE=1
			 SERVICE_TEST=http://58.136.124.6:88/PROGRESS_TEST/
			 SERVICE_UAT=http://58.136.124.6:89/PROGRESS_UAT/
			 SERVICE_PROD=http://58.136.124.6:99/PROGRESS/
			 */
			String activeCode = properties.getProperty(SERVICE_ACTIVE, "1");
			if (activeCode.equalsIgnoreCase("1"))
			{
				server = 
						properties.getProperty(SERVICE_TEST);
				SERVICE_NAME = serverNames[0];
			}else if (activeCode.equalsIgnoreCase("2")){
				server = 
						properties.getProperty(SERVICE_UAT);
				SERVICE_NAME = serverNames[1];
			}else if (activeCode.equalsIgnoreCase("3"))
			{
				server = 
						properties.getProperty(SERVICE_PROD);
				SERVICE_NAME = serverNames[2];
			}else{
				server = 
				properties.getProperty(SERVICE_LAYOUT_TEST);
		SERVICE_NAME = serverNames[3];

			}
		}
		return server;
	}
}
