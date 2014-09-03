package com.epro.psmobile.fragment;

import com.epro.psmobile.R;
import com.epro.psmobile.adapter.CustomerInfoDetailFragmentPageAdapter;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomerInfoDetailFragment extends ContentViewBaseFragment {

	private View currentView;
	private ViewPager pager;
	
	public static CustomerInfoDetailFragment newInstance(JobRequest jobRequest,Task task,boolean showInPlan)
	{
		CustomerInfoDetailFragment detail = new CustomerInfoDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
		bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, task);
		bundle.putBoolean(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL_IN_PLAN, showInPlan);
		detail.setArguments(bundle);
		return detail;		
	}
	/*
	public static CustomerInfoDetailFragment newInstance(JobRequest jobRequest){
		CustomerInfoDetailFragment detail = new CustomerInfoDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
		detail.setArguments(bundle);
		return detail;
	}*/
	private CustomerInfoDetailFragment() {
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currentView = inflater.inflate(R.layout.customer_detail_info_fragment, container, false);
		initial(currentView);
		return currentView;
	}
	private void initial(View view)
	{
		pager = (ViewPager)view.findViewById(R.id.customer_detail_info_viewpager);
		Bundle bArgument = this.getArguments();
		if (bArgument != null)
		{
			JobRequest jobRequest = (JobRequest)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
			Task task = (Task)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
			boolean showInPlan = 
					bArgument.getBoolean(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL_IN_PLAN);
			CustomerInfoDetailFragmentPageAdapter adapter = new CustomerInfoDetailFragmentPageAdapter(
					this.getSherlockActivity().getSupportFragmentManager(),task,jobRequest,showInPlan);
			pager.setAdapter(adapter);
		}
	}
}
