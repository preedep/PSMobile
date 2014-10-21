/**
 * 
 */
package com.epro.psmobile.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
/*
import uk.co.jasonfry.android.tools.ui.PageControl;
import uk.co.jasonfry.android.tools.ui.SwipeView;
import uk.co.jasonfry.android.tools.ui.SwipeView.OnPageChangedListener;
*/
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.InspectHistoryFragmentPagerAdapter;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectHistory;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author thrm0006
 * @param <Fragment>
 *
 */
@SuppressWarnings("hiding")
public class InspectHistoryFragment<Fragment> extends ContentViewBaseFragment implements OnPageChangeListener  {

	private View currentView;
	private InspectHistoryFragmentPagerAdapter mPagerAdapter;
	//private PageControl mPageControl;
	//private SwipeView mSwipeView;
	/**
	 * 
	 */
	public InspectHistoryFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		currentView = inflater.inflate(R.layout.inspect_history_fragment, container, false);
		
//		initial(currentView);
		try {
			initialisePaging(currentView);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentView;
	}
	
	/**
     * Initialise the fragments to be paged
     */
    private void initialisePaging(View view) 
    throws Exception
    {
    	ViewPager pager = null;
    	Bundle bArgument = this.getArguments();
		List<InspectHistoryPageItemFragment> fragments = new Vector<InspectHistoryPageItemFragment>();

		//ViewPager onlyLayout = (ViewPager)view.findViewById(R.id.inspect_history_viewpager);
		//ViewPager lastResult = (ViewPager)view.findViewById(R.id.inspect_history_last_result_viewpager);

		pager = (ViewPager)view.findViewById(R.id.inspect_history_viewpager);

		if (bArgument != null)
		{
			
			pager.setOnPageChangeListener(this);
			fragments = getFragments();
		
			
				if (pager.getAdapter() != null)
				{
					this.mPagerAdapter = (InspectHistoryFragmentPagerAdapter)pager.getAdapter();
					this.mPagerAdapter.setFragments(fragments);
					this.mPagerAdapter.notifyDataSetChanged();
				}else{
					this.mPagerAdapter  = new InspectHistoryFragmentPagerAdapter(this.getActivity(),
							this.getFragmentManager(),
							fragments);
					
					pager.setAdapter(this.mPagerAdapter);	
					this.mPagerAdapter.notifyDataSetChanged();
				}				
			
		}
	}
    
    private List<InspectHistoryPageItemFragment> getFragments() throws Exception
    {
    	List<InspectHistoryPageItemFragment> fragments = new Vector<InspectHistoryPageItemFragment>();
    	
    	Bundle bArgument = this.getArguments();
    	if (bArgument != null)
    	{
    		Task task = (Task)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);

        
    		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
    
    		/*
    		ArrayList<InspectHistory> inspectHistoryList = dataAdapter.getInspectHistory(task.getJobRequest().getJobRequestID(), task.getTaskNo());
    		if (inspectHistoryList != null)
    		{
    			for(InspectHistory history : inspectHistoryList)
    			{
    				InspectHistoryPageItemFragment fragment = new InspectHistoryPageItemFragment();
				    
    				Bundle b_argument = new Bundle();
    				b_argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_INSPECT_HISTORY_ITEM, history);
    				b_argument.putAll(bArgument);
    				fragment.setArguments(b_argument);					
    				fragments.add(fragment);
    			}						
    		}
    		*/
    		/*
    		ArrayList<CustomerSurveySite> customerSurveySiteList =
    				dataAdapter.findCustomerSurveySite(task.getJobRequest().getCustomerCode(),
    						task.getJobRequest().getJobRequestID()
    						);*/
    		
    		ArrayList<CustomerSurveySite> customerSurveySiteList =
    				dataAdapter.findCustomerSurveySite(
    						task.getTaskID()
    						);

    		if (customerSurveySiteList != null)
    		{
    			int count = 0;
    			for(CustomerSurveySite site : customerSurveySiteList)
    			{
    				/*
    				 * lasted result i'll show a fragment
    				 */
    				if (!this.getArguments().getBoolean(InstanceStateKey.KEY_ARGUMENT_IS_SHOW_JUST_LAYOUT_BUILDING, false))
    				{
    					count++;
    				}
    				if (count == 2)break;
    				
    				InspectHistoryPageItemFragment fragment = new InspectHistoryPageItemFragment();
    				Bundle b_argument = new Bundle();
    				bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, site);
    				b_argument.putAll(bArgument);
    				fragment.setArguments(b_argument);					
    				fragments.add(fragment);
    			}
    		}
    		
    	}
    	return fragments;
    	
    }

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		try {
//			mPagerAdapter.setFragments(getFragments());
//			mPagerAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
