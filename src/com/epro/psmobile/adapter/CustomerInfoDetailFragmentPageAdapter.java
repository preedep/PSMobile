/**
 * 
 */
package com.epro.psmobile.adapter;

import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.fragment.CustomerMapFragment;
import com.epro.psmobile.fragment.JobRequestPlanDetailFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author nickmsft
 *
 */
public class CustomerInfoDetailFragmentPageAdapter extends
		FragmentStatePagerAdapter {

	private final static int CUSTOMER_INFO_PAGE_COUNT = 2;
	
	private JobRequest jobRequest;
	private Task task;
	private boolean showInPlan = false;
	/**
	 * @param fm
	 */
	public CustomerInfoDetailFragmentPageAdapter(FragmentManager fm,Task task,
			JobRequest jobRequest,
			boolean showInPlan) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.jobRequest = jobRequest;
		this.task = task;
		this.showInPlan = showInPlan;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment f = null;
		if (position == 0){
			f = JobRequestPlanDetailFragment.newInstance(jobRequest,task,showInPlan);
		}else if (position == 1){
			try{
				f = CustomerMapFragment.newInstance(task,jobRequest);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return f;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return CUSTOMER_INFO_PAGE_COUNT;
	}

}
