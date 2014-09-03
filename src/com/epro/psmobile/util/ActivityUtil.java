package com.epro.psmobile.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.epro.psmobile.dialog.SyncDialog.SyncThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityUtil {

	public ActivityUtil() {
		// TODO Auto-generated constructor stub
	}
	public static void killThreadByName(String threadName)
	{
		Map<Thread, StackTraceElement[]> myMap = Thread.getAllStackTraces();
		ArrayList<Thread> setThreads = new ArrayList<Thread>(myMap.keySet());
		for(Thread t : setThreads){
			if (t.getName().equalsIgnoreCase(threadName)){
				try{
					if (t instanceof SyncThread){
						SyncThread syncThread = (SyncThread)t;
						syncThread.forceStopAnimationThread = true;
						if (syncThread.animationThread != null){
							syncThread.animationThread.interrupt();
						}
					}
					t.interrupt();
				}catch(Exception ex){};
			}
		}
	}
	public static void startNewActivity(Activity activity,
			Class myClass
			)
	{		
		Intent myIntent = new Intent(activity,myClass);
		activity.startActivity(myIntent);
	}
	public static void startNewActivity(Activity activity,
			Class myClass,
			Bundle bundle
			)
	{		
		Intent myIntent = new Intent(activity,myClass);
		if (bundle != null)
		{
			myIntent.putExtras(bundle);
		}
		activity.startActivity(myIntent);
	}
	public static void startNewActivityWithResult(Activity activity,
			Class myClass,
			Bundle bundle,
			int requestCode){
		Intent myIntent = new Intent(activity,myClass);
		if (bundle != null)
		{
			myIntent.putExtras(bundle);
		}
		activity.startActivityForResult(myIntent, requestCode);
	}
}
