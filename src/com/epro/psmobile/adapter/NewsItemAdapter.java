/**
 * 
 */
package com.epro.psmobile.adapter;

import java.util.ArrayList;

import org.xml.sax.XMLReader;

import com.epro.psmobile.R;
import com.epro.psmobile.data.News;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author nickmsft
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class NewsItemAdapter extends BaseAdapter {

	public interface NewsItemReadMoreHandler
	{
		void onReadMore(ListItemRow<News> currentNews);
	}
	private Context context;
	private ArrayList<ListItemRow<News>> itemRows;
	private LayoutInflater inflater;
	private NewsItemReadMoreHandler readMoreHandler;
	/**
	 * 
	 */
	public NewsItemAdapter(Context context,
			ArrayList<ListItemRow<News>> itemRows,
			NewsItemReadMoreHandler readMoreHandler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.itemRows = itemRows;
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.readMoreHandler = readMoreHandler;

	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemRows.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return itemRows.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = null;
		if (convertView == null)
		{
			final ListItemRow<News> rowItem = (ListItemRow<News>)this.getItem(position);
			if (rowItem.isRowHeader())
			{
				v = inflater.inflate(R.layout.news_list_header_item, parent, false);
				TextView tv = (TextView)v.findViewById(R.id.tv_news_list_header_item);
				tv.setText(rowItem.getTextRowHeader());
			}else{
				v = inflater.inflate(R.layout.news_list_content_item, parent, false);
				
				TextView tvNewsTitle = (TextView)v.findViewById(R.id.tv_lis_item_news_title);
				tvNewsTitle.setText(rowItem.getItem().getNewsTitle());
				
				Button btnReadMore = (Button)v.findViewById(R.id.btn_news_read_more);
				btnReadMore.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (readMoreHandler != null)
						{
							readMoreHandler.onReadMore(rowItem);
						}
					}
					
				});
				/*
				WebView wbView = (WebView)v.findViewById(R.id.wv_list_item_content);
				wbView.getSettings().setJavaScriptEnabled(true);
				wbView.loadData(rowItem.getItem().getNewsContent(), "text/html; charset=utf-8", "base64"); 
				 */
				 /*
				TextView wbView = (TextView)v.findViewById(R.id.wv_list_item_content);
				wbView.setText(Html.fromHtml(rowItem.getItem().getNewsContent(), new ImageGetter(){

					@Override
					public Drawable getDrawable(String source) {
						// TODO Auto-generated method stub
						byte[] data = Base64.decode(source, Base64.DEFAULT);
			            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);                
			            return new BitmapDrawable(context.getResources(), bitmap);
					}
					
				}, null));*/
			}
		}else{
			v = convertView;
		}
		return v;
	}

}
