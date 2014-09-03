package com.epro.psmobile.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.ImageUtil;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParseException;
import com.larvalabs.svgandroid.SVGParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class InspectItemGridViewAdapter extends BaseAdapter {

	
	public interface GridItemClickListener
	{
		void onClick(InspectDataItem item);
	}
	private Context ctxt;	
	private ArrayList<InspectDataItem> inspectDataItems;
	private LayoutInflater inflater;
	private GridItemClickListener itemClickListener;
	
	public InspectItemGridViewAdapter(Context ctxt,
			ArrayList<InspectDataItem> inspectDataItems,
			GridItemClickListener itemClickListener)
	{
		this.ctxt = ctxt;
		this.inspectDataItems = inspectDataItems;
		inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.itemClickListener = itemClickListener;
		
		/*
		 * 
		 */
		for(InspectDataItem dataItem : inspectDataItems)
		{
			String svgFileName = dataItem.getImageFileName();
			
			if ((svgFileName != null) && (svgFileName.endsWith("svg"))){

			String fileSvg = SharedPreferenceUtil.getDownloadFolder(ctxt)+"/"+svgFileName;
			File f = new File(fileSvg);
			if (f.exists()){				
				try {
					SVG svg = SVGParser.getSVGFromInputStream(new FileInputStream(fileSvg));
					Bitmap bmpTmp = ImageUtil.drawableToBitmap(svg.createPictureDrawable());
					
					dataItem.setBmpThump(ImageUtil.getResizedBitmap(bmpTmp, 130, 130));
					dataItem.setSvgObj(svg);
					bmpTmp.recycle();					
				} catch (SVGParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		  }
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.inspectDataItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.inspectDataItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View vItem = null;
		
		if (arg1 == null)
		{
			vItem = inflater.inflate(R.layout.inspect_data_gridview_item, null, false);
			
			final InspectDataItem inspectItem = (InspectDataItem)getItem(arg0);
			//String svgFileName = inspectItem.getImageFileName();
			if (inspectItem.getBmpThump() != null)
			{
				ImageView imgView = (ImageView)vItem.findViewById(R.id.imv_inspect_item);
				//imgView.setImageDrawable(svg.createPictureDrawable());
				imgView.setImageBitmap(inspectItem.getBmpThump());
				imgView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
					// TODO Auto-generated method stub
						if (itemClickListener != null)
						{
							itemClickListener.onClick(inspectItem);
						}
					}});
//				ImageUtil.resizeVectorImgView(imgView, 65, 65);				
				TextView tvInspectItemName = (TextView)vItem.findViewById(R.id.tv_inspect_item_name);
				tvInspectItemName.setText(inspectItem.getInspectDataItemName().replace(" ", "\n"));
			}
		/*
		if ((svgFileName != null) && (svgFileName.endsWith("svg"))){
			
			try{
	
				String fileSvg = AppStateUtil.getDownloadFolder(ctxt)+"/"+CommonValues.SVG_IMAGE_FOLDER+"/"+svgFileName;
				File f = new File(fileSvg);
				if (f.exists()){
					
					SVG svg = SVGParser.getSVGFromInputStream(new FileInputStream(fileSvg));
					vItem = inflater.inflate(R.layout.inspect_data_gridview_item, arg2, false);
				
				
					ImageView imgView = (ImageView)vItem.findViewById(R.id.imv_inspect_item);
					imgView.setImageDrawable(svg.createPictureDrawable());
					imgView.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
						if (itemClickListener != null)
						{
							itemClickListener.onClick(inspectItem);
						}
						}
				
				});
				ImageUtil.resizeVectorImgView(imgView, 65, 65);				
				TextView tvInspectItemName = (TextView)vItem.findViewById(R.id.tv_inspect_item_name);
				tvInspectItemName.setText(inspectItem.getInspectDataItemName());
				
				}
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}			
     	  }
     	  */
		}else{
			vItem = arg1;
		}
		return vItem;
	}

}
