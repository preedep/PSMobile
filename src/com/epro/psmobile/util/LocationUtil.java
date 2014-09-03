package com.epro.psmobile.util;

import java.util.List;

import android.content.Context;
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
	private static StringBuilder sb = new StringBuilder(20);

    /**
     * returns ref for latitude which is S or N.
     * @param latitude
     * @return S or N
     */
    public static String latitudeRef(double latitude) {
        return latitude<0.0d?"S":"N";
    }

    /**
     * returns ref for latitude which is S or N.
     * @param latitude
     * @return S or N
     */
    public static String longitudeRef(double longitude) {
        return longitude<0.0d?"W":"E";
    }

    /**
     * convert latitude into DMS (degree minute second) format. For instance<br/>
     * -79.948862 becomes<br/>
     *  79/1,56/1,55903/1000<br/>
     * It works for latitude and longitude<br/>
     * @param latitude could be longitude.
     * @return
     */
    synchronized public static final String convert(double latitude) {
        latitude=Math.abs(latitude);
        int degree = (int) latitude;
        latitude *= 60;
        latitude -= (degree * 60.0d);
        int minute = (int) latitude;
        latitude *= 60;
        latitude -= (minute * 60.0d);
        int second = (int) (latitude*1000.0d);

        sb.setLength(0);
        sb.append(degree);
        sb.append("/1,");
        sb.append(minute);
        sb.append("/1,");
        sb.append(second);
        sb.append("/1000,");
        return sb.toString();
    }
    public static Location getLastBestLocation(Context context) 
    {
       LocationManager locationManager = (LocationManager) context
             .getSystemService(Context.LOCATION_SERVICE);
       return getLastBestLocation(locationManager,1000,1);
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
