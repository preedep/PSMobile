package com.epro.psmobile.fragment;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.util.Log;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidGridAdapter;
import com.epro.psmobile.adapter.DashboardCalendarAdapter;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.da.PSBODataAdapter.WhereInStatus;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.key.params.InstanceStateKey;

public class DashboardCalendarFragment extends CaldroidFragment {

	private DashboardCalendarAdapter adapter;
	public static DashboardCalendarFragment newInstance(WhereInStatus whereInStatus)
	{
		DashboardCalendarFragment calendar = new DashboardCalendarFragment();
		Calendar cal = Calendar.getInstance();

		Bundle bArgument = new Bundle();
		bArgument.putInt(InstanceStateKey.KEY_ARGUMENT_CALENDAR_JOB_WHERE_IN, whereInStatus.getCode());		
		bArgument.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
		bArgument.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		bArgument.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
		bArgument.putBoolean(CaldroidFragment.FIT_ALL_MONTHS, false);

		calendar.setArguments(bArgument);
		
		return calendar;
	}
	private DashboardCalendarFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(getActivity());
		ArrayList<Task> tasks = null;
		try {
			WhereInStatus whereInStatus = WhereInStatus.NONE;
			if (getArguments() != null)
			{
				int iCode = getArguments().getInt(InstanceStateKey.KEY_ARGUMENT_CALENDAR_JOB_WHERE_IN);
				whereInStatus = WhereInStatus.getWhereInStatus(iCode);
			}
			tasks = dbAdapter.getAllTasks(whereInStatus);
			if (tasks != null)
			{
				Log.d("DEBUG_D", "Tasks sizes = "+tasks.size());
				
				for(Task t : tasks)
				{
					JobRequest jobRequest = t.getJobRequest();
					/*
					ArrayList<CustomerSurveySite> surveySites = 
							dbAdapter.findCustomerSurveySite(
									jobRequest.getCustomerCode(), jobRequest.getJobRequestID());
*/
					ArrayList<CustomerSurveySite> surveySites = 
							dbAdapter.findCustomerSurveySite(
									t.getTaskID()
									);

					t.getJobRequest().addCustomerSurveySiteList(surveySites);
				}
				
			}
			//tasks = dbAdapter.getAllTasks();
		} catch (Exception ex){
			ex.printStackTrace();
		}
		adapter =  new DashboardCalendarAdapter(getActivity(), 
				month, 
				year, 
				getCaldroidData(), 
				extraData,
				tasks);
		
		return adapter;
	}
	public void sortByTaskStatus(TaskStatus status,WhereInStatus whereInStatus)
	{
		if (adapter != null)
		{
			ArrayList<Task> tasks = null;			
			PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(getActivity());
			try {
				tasks = dbAdapter.getAllTasks(status, whereInStatus);
				addCustomerSurveySiteList(tasks,dbAdapter);
				
				adapter.setTasks(tasks);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public void sortByInspectTypeId(int inspectTypeId,WhereInStatus whereInStatus)
	{
		if (adapter != null)
		{
			ArrayList<Task> tasks = null;
			PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(getActivity());

			try {
				if (inspectTypeId >= 0)
					tasks = dbAdapter.getAllTasksByInspectID(inspectTypeId, whereInStatus);
				else
					tasks = dbAdapter.getAllTasks(whereInStatus);
				
				addCustomerSurveySiteList(tasks,dbAdapter);
				
				adapter.setTasks(tasks);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addCustomerSurveySiteList(ArrayList<Task> taskList,PSBODataAdapter dataAdapter)
	throws Exception
	{
		if (taskList != null)
		{
			for(Task t : taskList)
			{
				/*
				ArrayList<CustomerSurveySite> surveySites = dataAdapter.findCustomerSurveySite(
						t.getJobRequest().getCustomerCode(),
						t.getJobRequest().getJobRequestID());*/
				ArrayList<CustomerSurveySite> surveySites = dataAdapter.findCustomerSurveySite(
						t.getTaskID()
						);

				t.getJobRequest().addCustomerSurveySiteList(surveySites);
			}
		}
	}
}
