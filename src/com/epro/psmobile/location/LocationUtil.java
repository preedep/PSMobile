package com.epro.psmobile.location;

import java.util.List;

import android.location.Location;
import android.location.LocationManager;

public class LocationUtil {

	public final static int MIN_TIME_GPS = 300;
	public final static int MIN_TIME_NETWORK = 500;
	public final static int MIN_TIME_PASSIVE = 600;
	
	public final static int MIN_DISTANCE = 50;
	
	public LocationUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static Location getLastBestLocation(LocationManager locationManager,
			int minDistance, 
			long minTime) 
	{
		    Location bestResult = null;
		    float bestAccuracy = Float.MAX_VALUE;
		    long bestTime = Long.MIN_VALUE;
		    
		    // Iterate through all the providers on the system, keeping
		    // note of the most accurate result within the acceptable time limit.
		    // If no result is found within maxTime, return the newest Location.
		    List<String> matchingProviders = locationManager.getAllProviders();
		    for (String provider: matchingProviders) {
		      Location location = locationManager.getLastKnownLocation(provider);
		      if (location != null) {
		        float accuracy = location.getAccuracy();
		        long time = location.getTime();
		        
		        if ((time > minTime && accuracy < bestAccuracy)) {
		          bestResult = location;
		          bestAccuracy = accuracy;
		          bestTime = time;
		        }
		        else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
		          bestResult = location;
		          bestTime = time;
		        }
		      }
		    }
		    return bestResult;
	}
}
