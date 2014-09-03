/**
 * 
 */
package com.epro.psmobile.fragment;


import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Customer;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.location.PSMobileLocationManager;
import com.epro.psmobile.location.PSMobileLocationManager.OnPSMLoctaionListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @author nickmsft
 *
 */
public class CustomerMapFragment extends GMapBaseFragment implements 
com.epro.psmobile.fragment.ContentViewBaseFragment.BoardcastLocationListener
{

	

	private View customerMapView;
	private GoogleMap map;
	private PSMobileLocationManager locManager = null;
	private Marker myMarker;
	
	private Fragment mapFragment;
	private ArrayList<CustomerSurveySite> surveySites;
	private JobRequest jobRequest;
	
	private boolean hasShowCustomerSite = false;
	private Task task;
	
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
		try
		{
			customerMapView = inflater.inflate(R.layout.customer_map, container, false);
			initialMapView(customerMapView);
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

	private void initialMapView(View customerMapView)
	{
		try{
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
				MapsInitializer.initialize(getActivity());			
				
				FragmentManager fm = this.getChildFragmentManager();
				mapFragment =  fm.findFragmentById(R.id.mapview);
				if (mapFragment == null)
				{
					mapFragment = SupportMapFragment.newInstance();
					//SupportMapFragment supportFragment = (SupportMapFragment)fragment;
					fm.beginTransaction().replace(R.id.mapview, mapFragment).commit();
				}
		 
		 		
		 		setBoardcastLocationListener(this);
			
			
			Button btnMe = (Button)customerMapView.findViewById(R.id.btn_my_location);
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
			}
					
			Bundle bArgument = this.getArguments();
			if (bArgument != null)
			{
				jobRequest =  (JobRequest)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
				task = (Task)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
				String customerCode = jobRequest.getCustomerCode();
				PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this.getActivity());
				
				/*
				surveySites = dataAdapter.findCustomerSurveySite(
						   customerCode,jobRequest.getJobRequestID()
						);*/

				surveySites = dataAdapter.findCustomerSurveySite(
						task.getTaskID()
						);
			}
		}catch(Exception ex)
		{
			Log.d("DEBUG_D", ex.getMessage());			
		}
		
		
		
	}

	@Override
	public void onBoardcastLocationUpdated(final Location currentLocation) {
		// TODO Auto-generated method stub
		if (map == null){
			if (mapFragment != null)
				map = ((SupportMapFragment)mapFragment).getMap();
		}

		if (map != null)
		{
			if (!hasShowCustomerSite){
				this.showCustomerSites();
				this.hasShowCustomerSite = true;
			}
		}
		LatLng inspector = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
		
		float maxZoomLevel = 15f;

		if (myMarker == null){
			if (map != null){
				myMarker = map.addMarker(new MarkerOptions()
					.position(inspector)
					.title(getActivity().getString(R.string.inspector_title_display_mape))
					.snippet(getActivity().getString(R.string.inspector_snippet_display_map))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			}
		}else{
			myMarker.setPosition(inspector);
		}
		if (map != null)
		{
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(inspector, maxZoomLevel));
			map.animateCamera(CameraUpdateFactory.zoomTo(maxZoomLevel), 2000, null); 
		}
	}
	private void showCustomerSites(){
		if (surveySites != null)
		{
			for(CustomerSurveySite site : surveySites)
			{
				if ((site.getSiteLocLat() > 0)&&(site.getSiteLocLon() > 0))
				{
					LatLng siteLoc = new LatLng(site.getSiteLocLat(),site.getSiteLocLon());

					if (map != null)
					map.addMarker(new MarkerOptions()
						.position(siteLoc)
						.title(site.getCustomerName())
						.snippet(site.getSiteAddress())
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
				}
			}
		}

	}
}
