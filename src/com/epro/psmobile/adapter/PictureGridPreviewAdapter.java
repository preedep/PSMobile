/**
 * 
 */
package com.epro.psmobile.adapter;

import java.util.ArrayList;

import com.epro.psmobile.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * @author nickmsft
 *
 */
public class PictureGridPreviewAdapter extends BaseAdapter {

	private ArrayList<Bitmap> bmpList;
	private Context ctxt;
	private LayoutInflater inflater;
	/**
	 * 
	 */
	public PictureGridPreviewAdapter(Context ctxt,ArrayList<Bitmap> bmpList) {
		this.ctxt = ctxt;
		this.bmpList = bmpList;
		// TODO Auto-generated constructor stub
		inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bmpList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.bmpList.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View v = null;
		if (convertView == null)
	    {
	         // Customize superView here
			convertView = inflater.inflate(R.layout.picture_gridview_preview_item, null, false);			
			ImageView imgView = (ImageView)convertView.findViewById(R.id.img_gridview_preview_item);
			imgView.setImageBitmap((Bitmap)getItem(arg0));
	    }
		v = convertView;
		
		return v;
	}
	
	public void addNewBitmap(Bitmap bmp)
	{
		this.bmpList.add(bmp);
		this.notifyDataSetChanged();
	}

}
