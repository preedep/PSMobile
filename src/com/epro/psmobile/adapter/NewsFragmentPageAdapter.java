package com.epro.psmobile.adapter;

import com.epro.psmobile.data.News;
import com.epro.psmobile.fragment.NewsDetailFragment;
import com.epro.psmobile.fragment.NewsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class NewsFragmentPageAdapter extends FragmentStatePagerAdapter {

	private ViewPager viewPager;
	private ListItemRow<News> itemRowSelected;
	private NewsFragment newsFragment;
	private NewsDetailFragment detailFragment;
	public NewsFragmentPageAdapter(FragmentManager fm,ViewPager viewPager) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.viewPager = viewPager;
		
		newsFragment =  NewsFragment.newInstance();
		detailFragment =  NewsDetailFragment.newInstance(null);
	}

	public ListItemRow<News> getItemRowSelected() {
		return itemRowSelected;
	}

	public void setItemRowSelected(ListItemRow<News> itemRowSelected) {
		this.itemRowSelected = itemRowSelected;
		
		detailFragment.setNews(this.itemRowSelected.getItem());
		detailFragment.loadWebContent();
	}

	public NewsFragment getNewsFragment(){
		return newsFragment;
	}
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		if (arg0 == 0){
//			NewsFragment newsFragment =  NewsFragment.newInstance();
			newsFragment.setViewPager(viewPager);
			return newsFragment;
		}else{
			News news = null;
			if (itemRowSelected != null)
				news = itemRowSelected.getItem();
			
//			NewsDetailFragment detailFragment =  NewsDetailFragment.newInstance(news);
			detailFragment.setViewPager(viewPager);
			return detailFragment;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
