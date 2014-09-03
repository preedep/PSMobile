package com.epro.psmobile.location;

import android.location.Criteria;

public class LocationProviderCriteria {

	public LocationProviderCriteria() {
		// TODO Auto-generated constructor stub
	}
	

	
	/** this criteria will settle for less accuracy, high power, and cost */
	public static Criteria createCoarseCriteria() {

	  Criteria c = new Criteria();
	  c.setAccuracy(Criteria.ACCURACY_COARSE);
	  c.setAltitudeRequired(false);
	  c.setBearingRequired(false);
	  c.setSpeedRequired(false);
	  c.setCostAllowed(true);
	  c.setPowerRequirement(Criteria.POWER_HIGH);
	  return c;

	}

	/** this criteria needs high accuracy, high power, and cost */
	public static Criteria createFineCriteria() {

	  Criteria c = new Criteria();
	  c.setAccuracy(Criteria.ACCURACY_FINE);
	  c.setAltitudeRequired(false);
	  c.setBearingRequired(false);
	  c.setSpeedRequired(false);
	  c.setCostAllowed(true);
	  c.setPowerRequirement(Criteria.POWER_HIGH);
	  return c;

	}
}
