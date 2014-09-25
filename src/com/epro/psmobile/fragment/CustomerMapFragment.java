/**
 * 
 */
package com.epro.psmobile.fragment;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Customer;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.googleplaces.GooglePlacesManager;
import com.epro.psmobile.googleplaces.Place;
import com.epro.psmobile.googleplaces.PlacesList;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.location.PSMobileLocationManager;
import com.epro.psmobile.location.PSMobileLocationManager.OnPSMLoctaionListener;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.MessageBox.MessageConfirmType;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.epro.psmobile.fragment.ContentViewBaseFragment.BoardcastLocationListener;
import com.epro.psmobile.fragment.MyMapFragment.MapCallback;
/**
 * @author nickmsft
 *
 */

class MyMapFragment extends SupportMapFragment
{
  private MapCallback callback;

 
  public void setMapCallback(MapCallback callback)
  {
    this.callback = callback;
  }
  public static interface MapCallback
  {
     public void onMapReady(GoogleMap map);
  }
  @Override public void onActivityCreated(Bundle savedInstanceState)
  {
     super.onActivityCreated(savedInstanceState);
     if(callback != null) callback.onMapReady(getMap());     
  }
}

public class CustomerMapFragment extends GMapBaseFragment implements 
 OnMyLocationChangeListener, 
 OnInfoWindowClickListener, OnMapLongClickListener, OnItemSelectedListener, OnClickListener
{

	

	//private static View customerMapView;
	private GoogleMap map;
	private PSMobileLocationManager locManager = null;
	private Marker myMarker;
	
	private SupportMapFragment mapFragment;
	private ArrayList<CustomerSurveySite> surveySites;
	private CustomerSurveySite currentSurveySite;
	private JobRequest jobRequest;
	
	private boolean hasShowCustomerSite = false;
	private Task task;
	
	//private com.epro.psmobile.googleplaces.PlacesList placesList;
	private String nextPage;
	
	private Location previousLocation;
	private ArrayList<Marker> customerSiteMarkList;
	private ArrayList<Marker> poiMarkList;
	private boolean zoomInFirstTime = false;
	private boolean zoomToSite = false;
	public static CustomerMapFragment newInstance(Task task,JobRequest jobRequest)
	{
		CustomerMapFragment customerMap = new CustomerMapFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
		bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, task);
		customerMap.setArguments(bundle);
		return customerMap;
	}
	/**
	 * 
	 */
	private CustomerMapFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	   View customerMapView = null;
		try
		{
			customerMapView = inflater.inflate(R.layout.customer_map, container, false);
			
			Button btnGoToLocation = (Button)customerMapView.findViewById(R.id.btn_go_to_location);
			btnGoToLocation.setOnClickListener(this);
			Button btnReset = (Button)customerMapView.findViewById(R.id.btn_reset_to_my_location);
			btnReset.setOnClickListener(this);
			
			Spinner customerSites = 
			      (Spinner)customerMapView.findViewById(R.id.sp_locations);
			if (customerSites != null)
			{
			    Bundle bArgument = this.getArguments();
	            if (bArgument != null)
	            {
	                jobRequest =  (JobRequest)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
	                task = (Task)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
	                PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this.getActivity());
	                surveySites = dataAdapter.findCustomerSurveySite(
	                        task.getTaskID()
	                        );
	            }
			   if (surveySites != null)
			   {
			      CustomerSurveySite[] arraySites = new CustomerSurveySite[surveySites.size()];
			      surveySites.toArray(arraySites);
			      
			      ArrayAdapter<CustomerSurveySite> spinnerArrayAdapter = new ArrayAdapter<CustomerSurveySite>(
			            getSherlockActivity(),
			            android.R.layout.simple_spinner_item,
			            arraySites) //selected item will look like a spinner set from XML
			      {

                  /* (non-Javadoc)
                   * @see android.widget.ArrayAdapter#getDropDownView(int, android.view.View, android.view.ViewGroup)
                   */
                  @Override
                  public View getDropDownView(int position, View convertView, ViewGroup parent) {
                     // TODO Auto-generated method stub
                     View v = View.inflate(getContext(), android.R.layout.simple_spinner_item, null);//super.getDropDownView(position, convertView, parent);
                     if (v != null){
                        TextView tv = (TextView)v.findViewById(android.R.id.text1);
                        if (surveySites.size()-1 >= position){
                           CustomerSurveySite site = surveySites.get(position);
                           tv.setText(site.getSiteAddress());                           
                        }
                     }
                     return v;
                  }

                  /* (non-Javadoc)
                   * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
                   */
                  @Override
                  public View getView(int position, View convertView, ViewGroup parent) {
                     // TODO Auto-generated method stub
                     View v = super.getView(position, convertView, parent);
                     TextView tv = (TextView)v.findViewById(android.R.id.text1);
                     CustomerSurveySite site = surveySites.get(position);
                     tv.setText(site.getSiteAddress());
                     return v;
                  }
			         
			      };
			      //spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

			      customerSites.setAdapter(spinnerArrayAdapter);
			      customerSites.setOnItemSelectedListener(this);
			      customerSites.setSelection(0);
			   }
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return customerMapView;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	

		
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		/*
		
		 Fragment fragment = (getFragmentManager().findFragmentById(R.id.mapview));  
	     FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	     ft.remove(fragment);
	     ft.commit();*/

	}

	/* (non-Javadoc)
    * @see com.epro.psmobile.fragment.ContentViewBaseFragment#onPause()
    */
   @Override
   public void onPause() {
      // TODO Auto-generated method stub
      super.onPause();
    }
   /* (non-Javadoc)
    * @see com.epro.psmobile.fragment.ContentViewBaseFragment#onResume()
    */
   @Override
   public void onResume() {
      // TODO Auto-generated method stub
      super.onResume();
      
      Fragment f = this.getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.mapview);
      if (f != null) 
      {
         this.getSherlockActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
      }
      
      zoomInFirstTime = false;
      //if (this.customerMapView != null)
      initialMapView();
   }
   private void initialMapView()
	{
		try
		{
		   /*
		   LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,new LocationListener(){

            @Override
            public void onLocationChanged(Location location) {
               // TODO Auto-generated method stub
               Log.d("DEBUG_D", "Location updateD");
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
		      
		   });*/

				/*
			  		google map version 2.0 required google play service 
				 */
				hasShowCustomerSite = false;
				
				int checkGooglePlayServices = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
				if (checkGooglePlayServices != ConnectionResult.SUCCESS) 
				{
					// google play services is missing!!!!
					/* Returns status code indicating whether there was an error. 
		    			Can be one of following in ConnectionResult: SUCCESS, SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED, SERVICE_DISABLED, SERVICE_INVALID.
					 */
						GooglePlayServicesUtil.getErrorDialog(
									checkGooglePlayServices, this.getActivity(), 1122).show();
		       
							return ;
				}
				MapsInitializer.initialize(getSherlockActivity());         
                
				
		        FragmentManager fm = getChildFragmentManager();
                Fragment f_mapView =  fm.findFragmentById(R.id.mapview);
                if (f_mapView == null)
                {
                    mapFragment = new MyMapFragment();//SupportMapFragment.newInstance();
                    fm.beginTransaction().replace(R.id.mapview, mapFragment).commit();
                }
				 //mapFragment = (SupportMapFragment)this.getSherlockActivity().getSupportFragmentManager().findFragmentById(
	             //      R.id.mapview);
	             if (mapFragment != null)
	             {
	               // map = mapFragment.getMap();
	                if (mapFragment instanceof MyMapFragment){
	                   ((MyMapFragment) mapFragment).setMapCallback(new MapCallback(){

                        @Override
                        public void onMapReady(GoogleMap map) {
                           // TODO Auto-generated method stub
                           if (map != null)
                           {
                              CustomerMapFragment.this.map = map;
                              map.clear();
                              map.setMyLocationEnabled(true);
                              map.getUiSettings().setMyLocationButtonEnabled(true);
                              map.setOnMyLocationChangeListener(CustomerMapFragment.this);
                              map.setOnMapLongClickListener(CustomerMapFragment.this);
                           }
                        }
	                      
	                   });
	                }
	                
	             }
	             /*
		        Button btnMe = (Button)customerMapView.findViewById(R.id.btn_my_location);
		        btnMe.setVisibility(View.INVISIBLE);*/
		        /*
		        if (btnMe != null){
		           btnMe.setOnClickListener(new OnClickListener(){
				   
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (myMarker != null)
						{
							com.google.android.gms.maps.model.CameraPosition.Builder builder = new CameraPosition.Builder();
				            builder.zoom(15f);
				            builder.target(myMarker.getPosition());				            
				            map.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

						}
					}
					
				});
			}*/
		    //setBoardcastLocationListener(this);
					
			Bundle bArgument = this.getArguments();
			if (bArgument != null)
			{
				jobRequest =  (JobRequest)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
				task = (Task)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
				PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this.getActivity());
				surveySites = dataAdapter.findCustomerSurveySite(
						task.getTaskID()
						);
				
				
				
				
			}
		}catch(Exception ex)
		{
			Log.d("DEBUG_D", ex.getMessage());			
		}
		
		
		
	}
	
	public void onCurrentLocationUpdated(final Location currentLocation) {
		// TODO Auto-generated method stub
		
	   Log.d("DEBUG_D", "onBoardcastLocationUpdated");
        if (map != null)
		{
			if (!hasShowCustomerSite){
				this.showCustomerSites();
				this.hasShowCustomerSite = true;
			}
		}
	    
	    
		LatLng inspector = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
		
		float maxZoomLevel = 15f;

		if (myMarker != null){
		    myMarker.remove();
		    myMarker = null;
		    System.gc();
		}		    
		    
		myMarker = map.addMarker(new MarkerOptions()
            .position(inspector)
            .title(getActivity().getString(R.string.inspector_title_display_mape))
            .snippet(getActivity().getString(R.string.inspector_snippet_display_map))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
		    
		if (map != null)
		{
		   if (!zoomInFirstTime){
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(inspector, maxZoomLevel));
			map.animateCamera(CameraUpdateFactory.zoomTo(maxZoomLevel), 2000, null); 
			zoomInFirstTime = true;
		   }
		}
	    
	}
	private void showCustomerSites()
	{
	    if (customerSiteMarkList == null){
	       customerSiteMarkList = new ArrayList<Marker>();
	    }
		if (surveySites != null)
		{
		    for(Marker m : customerSiteMarkList){
		       m.remove();
		    }
			for(CustomerSurveySite site : surveySites)
			{
				if ((site.getSiteLocLat() > 0)&&(site.getSiteLocLon() > 0))
				{
					LatLng siteLoc = new LatLng(site.getSiteLocLat(),site.getSiteLocLon());

					if (map != null){
					      Marker m = map.addMarker(new MarkerOptions()
					         .position(siteLoc)
					         .title(site.getCustomerName())
					         .snippet(site.getSiteAddress())
					         .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
					      
					      customerSiteMarkList.add(m);
					}
				}
			}
		}

	}
	class GooglePlacesResult{
	   public boolean isSuccess;
	   public String error;
	   public PlacesList places;
	}
	class LoadGooglePlaces extends AsyncTask<GeoPoint,Void,GooglePlacesResult>{

         @Override
         protected GooglePlacesResult doInBackground(GeoPoint... params) {
            // TODO Auto-generated method stub
            
            String types = "gas_station";//getString(R.string.key_word_search_poi);

            // Radius in meters - increase this value if you don't find any
            // places
            double radius = 10000; // 10km. 
            GeoPoint currentPoint = params[0];
            
            GooglePlacesManager googlePlaces = new GooglePlacesManager();
            GooglePlacesResult result = new GooglePlacesResult();
            try{
               if ((nextPage == null)||(nextPage.trim().isEmpty())){
                  result.places = googlePlaces.search(
                        (double) (currentPoint.getLatitudeE6() / 1E6),
                        (double) (currentPoint.getLongitudeE6() / 1E6),
                        radius, types);
                  nextPage = result.places.next_page_token;                  
                  result.isSuccess = true;
               }else{
                  result.places = googlePlaces.getNextPageToken(nextPage);
                  nextPage = result.places.next_page_token;
                  result.isSuccess = true;
               }
            }catch(Exception ex){
               ex.printStackTrace();
               result.isSuccess = false;
               result.error = ex.getMessage();
            }
            return result;
         }

         /* (non-Javadoc)
          * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
          */
         @Override
         protected void onPostExecute(GooglePlacesResult result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result.isSuccess){
               /*
                * show pin here
                */
               if (poiMarkList == null){
                  poiMarkList = new ArrayList<Marker>();
               }
               for(Marker m : poiMarkList){
                  m.remove();
               }
               ////////////
               if (result.places.status.equalsIgnoreCase("OK")){
                  List<Place> places = 
                        result.places.results;
                  for(Place p : places){
                     double lat = p.geometry.location.lat;
                     double lng = p.geometry.location.lng;
                     LatLng poiLoc = new LatLng(lat,lng);

                     Marker m = map.addMarker(new MarkerOptions()
                     .position(poiLoc)
                     .title(p.name)
                     .snippet(p.formatted_address)
                     .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_holed_violet_normal)));
    
                     poiMarkList.add(m);
                     ////////////////
                  }  
               }

            }else{
               /*show alert message*/
            }
         }
	}
   @Override
   public void onMyLocationChange(Location location) {
      // TODO Auto-generated method stub
         Log.d("DEBUG_D", "my location change");
         
         if (zoomToSite)return;
         
         
         int lat = (int) (location.getLatitude() * 1E6);
         int lng = (int) (location.getLongitude() * 1E6);
         
         
         GeoPoint point = new GeoPoint(lat, lng);
         onCurrentLocationUpdated(location);
         
         if (previousLocation == null){
            previousLocation = location;
            nextPage = null;
            new LoadGooglePlaces().execute(point);
         }else{
            if (previousLocation.distanceTo(location) >= 500){
               previousLocation = location;
               nextPage = null;
               new LoadGooglePlaces().execute(point);
            }
         }
   }
   @Override
   public void onInfoWindowClick(Marker marker) {
      // TODO Auto-generated method stub
      
   }
   /*
   @Override
   public void activate(OnLocationChangedListener listener) {
      // TODO Auto-generated method stub
      mListener = listener;  
   }
   @Override
   public void deactivate() {
      // TODO Auto-generated method stub
      mListener = null;
   }*/
   @Override
   public void onMapLongClick(LatLng latLng) {
      // TODO Auto-generated method stub
      if (poiMarkList == null)return;
      if (myMarker == null)return;
      
      for(final Marker marker : poiMarkList) {
         if(Math.abs(marker.getPosition().latitude - latLng.latitude) < 0.05 && Math.abs(marker.getPosition().longitude - latLng.longitude) < 0.05) {
            // Toast.makeText(MapActivity.this, "got clicked", Toast.LENGTH_SHORT).show(); //do some stuff
             MessageBox.showMessageWithConfirm(getSherlockActivity(), "", getString(R.string.open_route_guide), new MessageBox.MessageConfirmListener() {
               
               @Override
               public void onConfirmed(MessageConfirmType confirmType) {
                  // TODO Auto-generated method stub
                  if (confirmType == MessageConfirmType.OK){
                     String uri = String.format(Locale.getDefault(), 
                           "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", myMarker.getPosition().latitude, 
                           myMarker.getPosition().longitude, myMarker.getTitle(), marker.getPosition().latitude, marker.getPosition().longitude, marker.getTitle());
                     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                     intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                     
                     
                     StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
                     StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
                         .permitDiskWrites()
                         .build());

                     startActivity(intent);
                     
                     StrictMode.setThreadPolicy(old);
                  }
               }
            });
             break;
         }
     }
   }
   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      // TODO Auto-generated method stub
      Object obj = parent.getSelectedItem();
      if (obj instanceof CustomerSurveySite){
         currentSurveySite = (CustomerSurveySite)obj;
      }
   }
   @Override
   public void onNothingSelected(AdapterView< ? > arg0) {
      // TODO Auto-generated method stub
      
   }
   @Override
   public void onClick(View v) {
      // TODO Auto-generated method stub
      int id = v.getId();
      if (id == R.id.btn_go_to_location)
      {
         if ((customerSiteMarkList != null)&&(currentSurveySite != null))
         {
            for(Marker m : customerSiteMarkList){
               if (m.getSnippet().equalsIgnoreCase(currentSurveySite.getSiteAddress())){
                  /*
                   * zoom to
                   */
                  map.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 15));
                  map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null); 

                  
                  int lat = (int) (m.getPosition().latitude * 1E6);
                  int lng = (int) (m.getPosition().longitude * 1E6);
                  
                  
                  GeoPoint point = new GeoPoint(lat, lng);
                 
                  zoomToSite = true;
                  nextPage = null;
                  new LoadGooglePlaces().execute(point);
                  
                  break;
               }
            }
         }
      }else if (id == R.id.btn_reset_to_my_location){
         zoomToSite = false;
         zoomInFirstTime = false;
         previousLocation = null;
      }
   }
}
