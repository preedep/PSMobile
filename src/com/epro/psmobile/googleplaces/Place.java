package com.epro.psmobile.googleplaces;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;
import com.google.api.client.util.Key;

/**
 * Implement this class from "Serializable" So that you can pass this class
 * Object to another using Intents Otherwise you can't pass to another actitivy
 * */
public class Place implements Serializable {

	@Key
	public String id;

	@Key
	public String name;

	@Key
	public String reference;

	@Key
	public String icon;

	@Key
	public String vicinity;

	@Key
	public String formatted_address;

	@Key
	public String formatted_phone_number;
	
	@Key
	public String[] types;
	
	@Key
	public String website;

	// geometry
	@Key
	public Geometry geometry;

	public static class Geometry implements Serializable {
		@Key
		public Location location;
		@Key
		public Viewport viewport;
	}

	public static class Location implements Serializable {
		@Key
		public double lat;

		@Key
		public double lng;
	}

	public static class Viewport implements Serializable {
		@Key
		public Northeast northeast;
		@Key
		public Southwest southwest;
	}

	public static class Northeast implements Serializable {
		@Key
		public double lat;

		@Key
		public double lng;
	}

	public static class Southwest implements Serializable {
		@Key
		public double lat;

		@Key
		public double lng;
	}

	@Override
	public String toString() {
		return name + " - " + id + " - " + reference;
	}
	
	public GeoPoint getGeoMetryLocationPoint(){
		try{
			GeoPoint point = new GeoPoint((int)(geometry.location.lat * 1E6),(int)(geometry.location.lng * 1E6));
			return point;
		}catch(Exception e){
			return null;
		}
	}

}
