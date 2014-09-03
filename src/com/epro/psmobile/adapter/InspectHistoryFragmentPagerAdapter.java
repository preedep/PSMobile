/**
 * 
 */
package com.epro.psmobile.adapter;

import java.util.ArrayList;
import java.util.List;

import com.epro.psmobile.fragment.InspectHistoryPageItemFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author thrm0006
 *
 */
public class InspectHistoryFragmentPagerAdapter extends FragmentStatePagerAdapter {

	private List<InspectHistoryPageItemFragment> fragments;
	private SparseArray<View> views = new SparseArray<View>();
	private Context context;

	/*
	 http://thepseudocoder.wordpress.com/2011/10/05/android-page-swiping-using-viewpager/
	 */
	/**
	 * @param arg0
	 */
	public InspectHistoryFragmentPagerAdapter(
			Context context,
			FragmentManager arg0,
			List<InspectHistoryPageItemFragment> fragments) {
		super(arg0);
		// TODO Auto-generated constructor stub
		this.fragments = fragments;
		this.context = context;
	}
	/**
	 * @param arg0
	 */
	public InspectHistoryFragmentPagerAdapter(FragmentManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Fragment f =  this.fragments.get(arg0);
		Log.d("DEBUG_D", "PagerAdapter getItem = "+arg0);
		return f;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub		
		return this.fragments.size();
	}
	
	public void setFragments(List<InspectHistoryPageItemFragment> fragmentList)
	{
		this.fragments = fragmentList;
	}
}
