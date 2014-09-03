package com.epro.psmobile.fragment;

import com.epro.psmobile.R;
import com.epro.psmobile.adapter.NewsFragmentPageAdapter;
import com.epro.psmobile.data.News;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class NewsPagerFragment extends ContentViewBaseFragment {

	private View currentView;
	private ViewPager viewPager;
	
	
	
	public NewsPagerFragment() {
		// TODO Auto-generated constructor stub
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
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currentView = inflater.inflate(R.layout.ps_fragment_news_pager, container, false);
		initial(currentView);
		return currentView;
	}

	private void initial(View view)
	{
		viewPager = (ViewPager)view.findViewById(R.id.news_view_pager);
		viewPager.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}
			
		});

		NewsFragmentPageAdapter adapter = new NewsFragmentPageAdapter(this.getChildFragmentManager(),viewPager);		
		viewPager.setAdapter(adapter);
	}
	public ViewPager getViewPager(){
		return viewPager;
	}
}
