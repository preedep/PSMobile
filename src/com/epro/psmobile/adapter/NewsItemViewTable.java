package com.epro.psmobile.adapter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.epro.psmobile.data.News;

public class NewsItemViewTable extends Hashtable<String, ArrayList<News>> {

	/* (non-Javadoc)
	 * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public synchronized ArrayList<News> put(String key, ArrayList<News> value) {
		// TODO Auto-generated method stub
		return super.put(key, value);
	}

	public synchronized void addNews(News newsItem)
	{
		String date = newsItem.getPublishDate();
		if  (!this.containsKey(date)){
			put(date,new ArrayList<News>());
		}
		this.get(date).add(newsItem);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2718236402861755577L;

	public ArrayList<ListItemRow<News>> getItemRows(){
		ArrayList<ListItemRow<News>> newsRowItems  = new ArrayList<ListItemRow<News>>();
		Enumeration<String> dateList = keys();
		while(dateList.hasMoreElements())
		{
			String key = dateList.nextElement();
			ListItemRow<News> row = new ListItemRow<News>();
			row.setRowHeader(true);
			row.setTextRowHeader(key);
			row.setItem(null);
			newsRowItems.add(row);
			
			ArrayList<News> newsList = this.get(key);
			for(News newsItem : newsList)
			{
				row = new ListItemRow<News>();
				row.setRowHeader(false);
				row.setItem(newsItem);
				
				newsRowItems.add(row);
			}
		}
		return newsRowItems;
	}
}
