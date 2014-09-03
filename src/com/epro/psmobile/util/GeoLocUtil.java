package com.epro.psmobile.util;

import android.annotation.SuppressLint;
import android.location.Location;

public class GeoLocUtil {

	public GeoLocUtil() {
		// TODO Auto-generated constructor stub
	}
	public static float distFrom(Location locStart,Location locTo)
	{
		return distFrom(locStart.getLatitude(),
				locStart.getLongitude(),
				locTo.getLatitude(),
				locTo.getLongitude());
	}

	@SuppressLint("UseValueOf")
	public static float distFrom(double lat1, 
			double lng1, 
			double lat2, 
			double lng2) 
	{
		    double earthRadius = 3958.75;
		    double dLat = Math.toRadians(lat2-lat1);
		    double dLng = Math.toRadians(lng2-lng1);
		    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		               Math.sin(dLng/2) * Math.sin(dLng/2);
		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		    double dist = earthRadius * c;

		    int meterConversion = 1609;

		    return new Float(dist * meterConversion).floatValue();
	}
}
