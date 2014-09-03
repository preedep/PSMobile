/**
 * 
 */
package com.epro.psmobile.dialog;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.R.layout;
import com.epro.psmobile.adapter.JobPlanCustomerInfoHelper;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Customer;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.GeoLocUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author thrm0006
 *
 */
public class JobPlanDetailDialog extends DialogFragment {

	private View currentView;
	private Location currentLocation;
	private Task task = null;	
	protected BroadcastReceiver mLocationReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			currentLocation = arg1.getParcelableExtra(InstanceStateKey.KEY_BDX_LOC_UPDATED);
			
			if (currentLocation != null){
				Log.d("DEBUG_D", "JobPlanDetailDialog:BroadcastReceiver:onReceive -> " +
						"lat = "+currentLocation.getLatitude()+" , lon = "+currentLocation.getLongitude());
				
				if ((currentView != null)&&(task != null))
					setupTaskStatusDisplay(currentView,task);
				
			}
		}		
	};
	public static JobPlanDetailDialog newInstance(Task task){
		JobPlanDetailDialog frag = new JobPlanDetailDialog();

		Bundle bArgument = new Bundle();
		bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, task);
		frag.setArguments(bArgument);
		return frag;
	}
	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
//		this.setStyle(STYLE_NO_TITLE, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.getDialog().setTitle(R.string.job_detail_calendar_dlg_title);
		currentView = inflater.inflate(R.layout.calendar_dashboard_job_dialog, container, false);
		initial(currentView);
		return currentView;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		
		this.getActivity().unregisterReceiver(mLocationReceiver);
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//getDialog().getWindow().setLayout(400, 500);
	}
	private void initial(View v)
	{
		if (v != null)
		{
			//get argument
			//mNum = getArguments().getInt("num");
			
			IntentFilter filter = new IntentFilter(InstanceStateKey.BDX_LOC_UPDATED_ACTION);
			this.getActivity().registerReceiver(mLocationReceiver, filter);
			
			task = (Task)this.getArguments().getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);

			//Customer customer = task.getJobRequest().getCustomer();
			/*
			if (customer.getCustomerSiteList().size() == 0)
			{
				PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
				try {
					ArrayList<CustomerSurveySite> surveySites =  dataAdapter.findCustomerSurveySite(customer.getCustomerID());
					
					customer.addCustomerSiteListAll(surveySites);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
			setupTaskStatusDisplay(v,task);
			
			JobPlanCustomerInfoHelper.fillCustomerInfo(getActivity(), 
					task, 
					v);
			
			Button btnOk = (Button)v.findViewById(R.id.btn_cal_dashboard_ok);
			btnOk.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					JobPlanDetailDialog.this.dismiss();
				}
				
			});
		}
	}
    private void setupTaskStatusDisplay(View vContent,Task currentTask)
    {
    	TextView tvTime = (TextView)vContent.findViewById(R.id.tv_device_access_time);
    	if (tvTime != null){
//    		String textTime = currentTask.getTaskStartTime()+" - "+currentTask.getTaskEndTime();
//    		tvTime.setText(textTime);
    		String textTime = "";
    		if (currentTask.getTaskEndTime().isEmpty()){
				textTime = currentTask.getTaskStartTime();				
			}else{
				textTime = currentTask.getTaskStartTime()+" - "+currentTask.getTaskEndTime();
			}
    		tvTime.setText(textTime);
    	}
    	final String remark = currentTask.getRemark();
		if ((remark != null)&&(!remark.isEmpty()))
		{
			//have remark
			TextView tv_action_about = (TextView)vContent.findViewById(R.id.tv_action_about);
			tv_action_about.setText(getActivity().getString(R.string.inspect_have_remark));
			
			View vBtnClickRemark = vContent.findViewById(R.id.btn_action_show_remark);
			vBtnClickRemark.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(getActivity())
				    .setTitle(R.string.inspect_have_remark_dlg_title)
				    .setMessage(remark)
				    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            // continue with delete
				        }
				     })
				     .show();
				}
				
			});
		}
    	if (this.currentLocation != null)
    	{
    		TextView tvDistance = (TextView)vContent.findViewById(R.id.tv_distance_location_place);
			
			try{
				ArrayList<CustomerSurveySite> customerSurveySiteList = 
					currentTask.getJobRequest().getCustomerSurveySiteList();
				
				CustomerSurveySite firstSite = customerSurveySiteList.get(0);

				Location siteLocation = new Location(firstSite.getCustomerSurveySiteID()+"");
				siteLocation.setLatitude(firstSite.getSiteLocLat());
				siteLocation.setLongitude(firstSite.getSiteLocLon());
				float distance = GeoLocUtil.distFrom(currentLocation, siteLocation);
				distance = distance / 1000;
				
				String textDistance = this.getActivity().getString(R.string.distance_from_current_location,
						String.format( "%.2f", distance));
				
				tvDistance.setText(textDistance);
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
    	}
    }
}
