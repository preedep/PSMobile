package com.epro.psmobile.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import com.epro.psmobile.R;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.util.ImageUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.CommonValues;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParseException;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class InspecItemViewCreator implements  OnTouchListener {

	private int _xDelta;
	private int _yDelta;
	private Context ctxt;
	private View inspectItemView;
	private int resID;
	private LayoutInflater inflater;
	private Timer longPressTimer; //won't depend on a motion event to fire
	private final int longpressTimeDownBegin = 500; //0.5 s
	private boolean bPressed = false;
	private int clickCount = 0;
	    //variable for storing the time of first click
	private long startTime;
	    //variable for calculating the total time
	private long duration;
	    //constant for defining the time duration between the click that can be considered as double-tap
	private static final int MAX_DURATION = 500;
	
	public InspecItemViewCreator(Context ctxt,int resID)
	{
		this.ctxt = ctxt;
		this.resID = resID;
		inflater = (LayoutInflater)ctxt.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
	}
	public View createInspectItemView()
	{
		return createInspectItemView(null,null);
	}
	public View createInspectItemView(InspectDataItem inspectDataItem){
		return createInspectItemView(inspectDataItem,null);
	}
	public View createInspectItemView(InspectDataItem inspectDataItem,
			InspectDataObjectSaved dataSaved)
	{
		return createInspectItemView(inspectDataItem,dataSaved,-1,-1);
	}
	public View createInspectItemView(InspectDataItem inspectDataItem,
			InspectDataObjectSaved dataSaved,int width,int height)
	{

		View vRoot  = inflater.inflate(R.layout.ps_inspect_item_view, null);
		this.inspectItemView = vRoot.findViewById(R.id.root_inspect_view_body);
		if (this.inspectItemView != null)
		{
			/*
			 * load svg image 
			 */
			SVG svg  = null;

			if (inspectDataItem.getSvgObj() == null)
			{
					String svgFileName = inspectDataItem.getImageFileName();				
					if ((svgFileName != null) && (svgFileName.endsWith("svg"))){
						//int lastIdx = svgFileName.indexOf(".svg");
						//String mDrawableName = svgFileName.substring(0, lastIdx);
						//int resID = ctxt.getResources().getIdentifier(mDrawableName , "raw", ctxt.getPackageName());		
						//svg = SVGParser.getSVGFromResource(this.ctxt.getResources(), resID);
//						String fileSvg = AppStateUtil.getDownloadFolder(ctxt)+"/"+CommonValues.SVG_IMAGE_FOLDER+"/"+svgFileName;
						String fileSvg = SharedPreferenceUtil.getDownloadFolder(ctxt)+"/"+svgFileName;

						try {
							svg = SVGParser.getSVGFromInputStream(new FileInputStream(fileSvg));
						} catch (SVGParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

			}
			else{
				svg = inspectDataItem.getSvgObj();
			}
			
			ImageView imgView = (ImageView)this.inspectItemView.findViewById(R.id.inspect_img);
			imgView.setScaleType(ScaleType.CENTER);
			if (inspectDataItem.isGodownComponent())
			{
				imgView.setVisibility(View.INVISIBLE);
			}
			PictureDrawable picDrawable = svg.createPictureDrawable();
			//Bitmap bmp = ImageUtil.drawableToBitmap(svg.createPictureDrawable());
			//Bitmap newBmp = ImageUtil.getResizedBitmap(bmp, svg.getPicture().getWidth(), svg.getPicture().getHeight());
			if (width == -1){
				this.inspectItemView.getLayoutParams().width = picDrawable.getPicture().getWidth()+10;
				Log.d("DEBUG_D", "this.inspectItemView.getLayoutParams().width = "+this.inspectItemView.getLayoutParams().width);
			}else{
				this.inspectItemView.getLayoutParams().width = width;
			}
			
			if (height == -1){
				this.inspectItemView.getLayoutParams().height = picDrawable.getPicture().getHeight()+10;
				Log.d("DEBUG_D", "this.inspectItemView.getLayoutParams().height = "+this.inspectItemView.getLayoutParams().height);
			}else{
				this.inspectItemView.getLayoutParams().height = height;
			}
			
//			RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams)imgView.getLayoutParams();
//			rl.leftMargin += (width/2)+(svg.getPicture().getWidth()/2);
//			rl.topMargin += (height/2)+(svg.getPicture().getHeight()/2);

//			this.inspectItemView.requestLayout();
			
//			RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams)inspectItemView.getLayoutParams();
			
			imgView.setImageDrawable(picDrawable);

			RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(imgView.getLayoutParams());
			//rl.leftMargin += ((inspectItemView.getLayoutParams().width/2)-(svg.getPicture().getWidth()/2));
			//rl.topMargin += ((inspectItemView.getLayoutParams().height/2)-(svg.getPicture().getHeight()/2));
			rl.leftMargin = ((inspectItemView.getLayoutParams().width/2)-(svg.getPicture().getWidth()/2));
			rl.topMargin = ((inspectItemView.getLayoutParams().height/2)-(svg.getPicture().getHeight()/2));
			
			imgView.setLayoutParams(rl);
			
			
			
//			imgView.setImageBitmap(newBmp);
//			bmp.recycle();
//			bmp = null;
//			System.gc();
			

			//imgView.setOnTouchListener(this);
			
			/*
			 ViewGroup parent = (ViewGroup) mViewParent.getParent();
			int index = parent.indexOfChild(mViewParent);
			parent.removeView(mViewParent);
			 **/
			
			ViewGroup parent = (ViewGroup)this.inspectItemView.getParent();
			int index = parent.indexOfChild(this.inspectItemView);
			parent.removeView(this.inspectItemView);
			
			InspectItemViewState state = null;
			if (dataSaved != null)
				state = new InspectItemViewState(dataSaved);
			else
				state = new InspectItemViewState();
			
//			state.setImgWidth(svg.getPicture().getWidth());
//			state.setImgHeight(svg.getPicture().getHeight());
			
			state.setImgWidth(this.inspectItemView.getLayoutParams().width);
			state.setImgHeight(this.inspectItemView.getLayoutParams().height);
			state.setFullWidthView(this.inspectItemView.getLayoutParams().width);
			state.setFullHeightView(this.inspectItemView.getLayoutParams().height);

			
			state.setInspectDataItem(inspectDataItem);
			
			if (state.getInspectDataObjectSaved().getWidthObject() < 0){
				state.getInspectDataObjectSaved().setWidthObject(state.getImgWidth());
			}
			if (state.getInspectDataObjectSaved().getLongObject() < 0){
				state.getInspectDataObjectSaved().setLongObject(state.getImgHeight());
			}
			
			this.inspectItemView.setTag(state);
			
			this.inspectItemView.invalidate();

			//this.inspectItemView.invalidate();
			this.inspectItemView.requestLayout();
			return this.inspectItemView;
		}
		return null;
	}
	@Override
	public boolean onTouch(final View view, MotionEvent event) {
		// TODO Auto-generated method stub
		//boolean bRet = false;
		 switch(event.getAction() & MotionEvent.ACTION_MASK)
	     {
			case MotionEvent.ACTION_DOWN:{
				Log.d("DEBUG_D", "DOWN = "+bPressed);
				startTime = System.currentTimeMillis();
	            clickCount++;
	            bPressed = true;
				/*
				
				bPressed = true;
				longPressTimer = new Timer();
				longPressTimer.schedule(new TimerTask(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						Activity activity = (Activity)ctxt;
						activity.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								ViewGroup vg = (ViewGroup)(view.getParent()).getParent();
								//View subViewBorder = view.findViewById(R.id.rl_border);
								Drawable drawable = vg.getBackground();
								if (drawable instanceof ColorDrawable)
								{
									ColorDrawable color = (ColorDrawable)drawable;
									if (color.getColor() == 0x000000)
									{
										color.setColor(0xFFFFFF);
									}else{
										color.setColor(0x000000);
									}
								}
							}
							
						});						
					}
		            //whatever happens on a longpress
		        }, longpressTimeDownBegin);
				
				*/
			}
			break;
			
			case MotionEvent.ACTION_UP:{
				Log.d("DEBUG_D", "UP = "+bPressed);
				/*
				bPressed = false;
				if (longPressTimer != null){
					longPressTimer.cancel();
					longPressTimer = null;
				}*/
				
			    long time = System.currentTimeMillis() - startTime;
		        duration=  duration + time;
		        if(clickCount == 1)
		        {
		                if(duration<= MAX_DURATION)
		                {
		                    //Toast.makeText(captureActivity.this, "double tap",Toast.LENGTH_LONG).show();		                	
		                }
	                	 clickCount = 0;
			             duration = 0;
			             bPressed = false;

		                break;             
		        }
			}break;
		}
		return bPressed;
	}	
}
