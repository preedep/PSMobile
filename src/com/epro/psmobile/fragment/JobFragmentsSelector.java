package com.epro.psmobile.fragment;

import com.epro.psmobile.util.SharedPreferenceUtil;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class JobFragmentsSelector {

	public class FragmentItem
	{
		Fragment fragment;
		String tagName;
	}
	private FragmentItem calendarView;
	private FragmentItem contentListView;
	
	public JobFragmentsSelector() {
		// TODO Auto-generated constructor stub
	}
	public void addCalendarFragment(DashboardCalendarFragment f,String tagName){
		addCalendarFragment(f,tagName,null);
	}
	public void addCalendarFragment(DashboardCalendarFragment f,String tagName,Bundle bundle)
	{
		if (calendarView == null)
			calendarView = new FragmentItem();
		
		calendarView.fragment = f;
		calendarView.tagName = tagName;
		if (calendarView.fragment != null)
		{
			if (bundle != null)
			{
				calendarView.fragment.setArguments(bundle);
			}
		}
	}
	public void addContentListFragment(ContentViewBaseFragment f,String tagName)
	{
		addContentListFragment(f,tagName);
	}
	public void addContentListFragment(ContentViewBaseFragment f,String tagName,Bundle bundle)
	{
		if (contentListView == null)
			   contentListView = new FragmentItem();
		
		contentListView.fragment = f;
		contentListView.tagName = tagName;
		
		if (contentListView.fragment != null)
		{
			if (bundle != null)
			{
				contentListView.fragment.setArguments(bundle);
			}
		}
		
	}
	public Fragment getFragmentSelected(Context context){
		/*
		if (!AppStateUtil.checkDashboardIsListView(context))
		{
			if (contentListView != null)
				return contentListView.fragment;
		}else{
			if (calendarView != null)
				return calendarView.fragment;
		}*/
		return null;
	}

}
