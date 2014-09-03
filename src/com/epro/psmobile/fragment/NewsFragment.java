package com.epro.psmobile.fragment;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.adapter.ListItemRow;
import com.epro.psmobile.adapter.NewsFragmentPageAdapter;
import com.epro.psmobile.adapter.NewsItemAdapter;
import com.epro.psmobile.adapter.NewsItemAdapter.NewsItemReadMoreHandler;
import com.epro.psmobile.adapter.NewsItemViewTable;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.News;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NewsFragment extends ContentViewBaseFragment implements NewsItemReadMoreHandler
{

	public enum NewsFilterBy
	{
		ALL,
		TODAY,
		DAY7,
		DAY15
	};	
	private View currentView;
	private ViewPager viewPager;
	private ListView lsNews;
	public static NewsFragment newInstance(){
		NewsFragment fragment = new NewsFragment();
		
		
		return fragment;
	}
	private NewsFragment() {
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
		currentView = inflater.inflate(R.layout.ps_fragment_news, container, false);
		initial(currentView);
		return currentView;
	}

	public ViewPager getViewPager() {
		return viewPager;
	}
	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
	}
	private void initial(View view)
	{
		lsNews = (ListView)view.findViewById(R.id.lv_news);
		
		showNewsPublishByFilter(NewsFilterBy.ALL);
		
	}
	
	public void showNewsPublishByFilter(NewsFilterBy filterBy)
	{
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
		try {
			ArrayList<News> newsList = null;
			if (filterBy == NewsFilterBy.TODAY){
				newsList = dataAdapter.getAllNewsPublishNow();
			}else if (filterBy == NewsFilterBy.DAY7){
				newsList = dataAdapter.getAllNewsPublishPeriod(7);
			}else if (filterBy == NewsFilterBy.DAY15){
				newsList = dataAdapter.getAllNewsPublishPeriod(15);				
			}else{
				newsList = dataAdapter.getAllNewsPublish();				
			}
			if (newsList != null){
				NewsItemViewTable table = new NewsItemViewTable();
				for(News newsItem : newsList)
				{				
					table.addNews(newsItem);
				}
				NewsItemAdapter adapter = new NewsItemAdapter(this.getActivity(),
						table.getItemRows(),this);
				lsNews.setAdapter(adapter);
			  }else{
				 lsNews.setAdapter(null);
			  }
		 } 
		 catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onReadMore(ListItemRow<News> currentNews) {
		// TODO Auto-generated method stub
		if (viewPager != null)
		{
			NewsFragmentPageAdapter adapter = (NewsFragmentPageAdapter)viewPager.getAdapter();
			adapter.setItemRowSelected(currentNews);
			adapter.notifyDataSetChanged();
			viewPager.setCurrentItem(1, true);
		}
	}
}
