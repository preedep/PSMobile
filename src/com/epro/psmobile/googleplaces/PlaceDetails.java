package com.epro.psmobile.googleplaces;

import java.io.Serializable;

import com.google.api.client.util.Key;

/**
 * Implement this class from "Serializable" So that you can pass this class
 * Object to another using Intents Otherwise you can't pass to another actitivy
 * */
public class PlaceDetails implements Serializable {

	// @Key
	// public String status;
	//
	// @Key
	// public Place result;
	//
	// @Override
	// public String toString() {
	// if (result!=null) {
	// return result.toString();
	// }
	// return super.toString();
	// }

	@Key
	public String status;

	@Key
	public Place result;

	public static class Place implements Serializable {

		@Key
		public String formatted_address;

		@Key
		public String formatted_phone_number;

		@Key
		public Geometry geometry;
		
		@Key
		public String name;
		
		@Key
		public String[] types;
		
		@Key
		public String vicinity;
		
		@Key
		public String website;
	}

	public static class Geometry implements Serializable {
		@Key
		public Location location;
	}

	public static class Location implements Serializable {
		@Key
		public double lat;
		@Key
		public double lng;
	}
}
