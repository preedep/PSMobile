package com.epro.psmobile.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class PSMobileLocationManager implements LocationListener {

	private Context context;
	private LocationManager locationManager;
	private String providerGps;
	private String providerNetwork;
	
	private OnPSMLoctaionListener locationListener;
	
	
	public interface OnPSMLoctaionListener
	{
		void onLocationUpdated(Location location);
	}
	public PSMobileLocationManager(Context context) 
	{
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public void startPSMLocationListener(OnPSMLoctaionListener locationListener)
	throws Exception
	{
		this.locationListener = locationListener;
		
		// Get the location manager
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    
	    Location location = null;
	    location = LocationUtil.getLastBestLocation(locationManager, 5, 10);
	    
	    if (location != null)
	    {
	    	onLocationChanged(location);//first time get last know location
	    }
	    
	    requestLocationUpdated();
		   
	}
	public void requestLocationUpdated() throws Exception
	{
		if (locationManager != null)
		{
			String bestProvider = locationManager.getBestProvider(LocationProviderCriteria.createFineCriteria(), false);
			/*
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
					LocationUtil.MIN_TIME_GPS, 
					LocationUtil.MIN_DISTANCE, this);
			
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
					LocationUtil.MIN_TIME_NETWORK, LocationUtil.MIN_DISTANCE, this);
			
			locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 
					LocationUtil.MIN_TIME_PASSIVE, LocationUtil.MIN_DISTANCE, this);

*/
			if (locationManager != null){
			   locationManager.removeUpdates(this);
			}
			locationManager.requestLocationUpdates(bestProvider, 
                  LocationUtil.MIN_TIME_GPS, LocationUtil.MIN_DISTANCE, this);
          		}else{
			throw new Exception("Must call startAGALocationListener before.");
		}
	}
	public void stopRequestLocationUpdated(){
		if (locationManager != null)
		{
			locationManager.removeUpdates(this);
		}
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (locationListener != null)
		{
			locationListener.onLocationUpdated(location);
		}
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	public Location getLastknowLocation(){
	   return LocationUtil.getLastBestLocation(locationManager, 5, 10);
	}
}
