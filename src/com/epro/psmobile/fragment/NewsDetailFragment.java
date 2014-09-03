package com.epro.psmobile.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.epro.psmobile.R;
import com.epro.psmobile.data.News;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewsDetailFragment extends ContentViewBaseFragment {

	private ViewPager viewPager;
	private View currentView;
	private WebView web;
	private News news;
	
	private final static String KEY_NEWS = "news";
	private ProgressDialog alert;
	
	public static NewsDetailFragment newInstance(News news){
		NewsDetailFragment fragment = new NewsDetailFragment();
		if (news != null)
		{
			Bundle bArgument = new Bundle();
			bArgument.putParcelable(KEY_NEWS, news);
			fragment.setArguments(bArgument);
		}
		return fragment;
	}
	private NewsDetailFragment() {
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
		currentView = inflater.inflate(R.layout.ps_fragment_news_detail, container, false);
		initial(currentView);
		return currentView;
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initial(View view)
	{
		web = (WebView)view.findViewById(R.id.wv_news_detail);
		web.setWebViewClient(new WebViewClient(){

			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);
			}

			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				closeWaiting();
			}

			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				displayWaiting();
			}
			
		});
		Bundle bArgument = this.getArguments();
		if (bArgument != null){
			News news = (News)bArgument.get(KEY_NEWS);
		
			/*
			String base64 = Base64.encodeToString(news.getNewsContent().getBytes(), Base64.DEFAULT);
			web.getSettings().setJavaScriptEnabled(true);
			web.loadData(base64, "text/html; charset=utf-8", "base64");  */
//			web.loadData(news.getNewsContent(), "text/html", "utf-8");
			web.clearHistory();
			web.loadData(news.getNewsContent(), "text/html; charset=utf-8", "utf-8");
		}
		
		ImageButton btnBack = (ImageButton)view.findViewById(R.id.btn_news_previous_view);
		btnBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (viewPager != null)
				{
					viewPager.setCurrentItem(0);
				}
			}
			
		});
	}
	public ViewPager getViewPager() {
		return viewPager;
	}
	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
	}

	public void setNews(News news)
	{
		/*
		Bundle bArgument = new Bundle();
		bArgument.putParcelable(KEY_NEWS, news);
		setArguments(bArgument);
		*/
		this.news = news;
	}
	
	public void loadWebContent(){
		//Bundle bArgument = this.getArguments();
		if (this.news != null){
			TextView tvTitle = (TextView)currentView.findViewById(R.id.tv_news_detail_title);
			tvTitle.setText(news.getNewsTitle()+" ("+news.getPublishDate()+")");
			
			//String base64 = Base64.encodeToString(news.getNewsContent().getBytes(), Base64.DEFAULT);
			web.getSettings().setJavaScriptEnabled(true);
			web.getSettings().setDefaultTextEncodingName("utf-8");
			web.clearHistory();
			//web.loadData(base64, "text/html; charset=utf-8", "base64");  
			web.loadData(news.getNewsContent(), "text/html; charset=utf-8", "utf-8");
		}
	}
	
	private void displayWaiting(){
		this.getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				alert = new ProgressDialog(getActivity());
				alert.setMessage("Loading...");
				alert.setCancelable(false);
				alert.setIndeterminate(false);

				
				if (!alert.isShowing())
				{
					alert.show();
				}
			}
			
		});
		
	}
	
	private void closeWaiting(){
		
		this.getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (alert != null)
				{
					alert.dismiss();
					alert = null;
				}
			}
			
		});
		
	}
}
