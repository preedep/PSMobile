package com.epro.psmobile;

import java.util.ArrayList;

import com.epro.psmobile.view.InspectItemViewState;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class PsLayoutBaseActivity extends PsBaseActivity implements OnTouchListener {
	private int _xDelta;
	private int _yDelta;
	
	private long startTime;
	private int clickCount;
    //variable for calculating the total time
	private long duration;
	    //constant for defining the time duration between the click that can be considered as double-tap
	private static final int MAX_DURATION = 500;
	private double startAngle;
	
	private ArrayList<View> viewList;
	
	private boolean bMovingRight = false;
	private int centerImgX;
	private int centerImgY;
	
	
	public PsLayoutBaseActivity() {
		// TODO Auto-generated constructor stub
		viewList = new ArrayList<View>();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#addContentView(android.view.View, android.view.ViewGroup.LayoutParams)
	 */
	@Override
	public void addContentView(View view, LayoutParams params) {
		// TODO Auto-generated method stub
		super.addContentView(view, params);
		/*
		if (view.getTag() instanceof InspectItemViewState)
		{
			view.setOnTouchListener(this);
		}
		viewList.add(view);
		*/
	}

	protected void startMoveItemListener(ViewGroup vgRootLayout){
		
		for(int i = 0; i < vgRootLayout.getChildCount();i++)
		{
			View v = vgRootLayout.getChildAt(i);
			if (v.getTag() instanceof InspectItemViewState)
			{
				v.setOnTouchListener(this);
				
				/*
				 *setup button corner listener
				 */
				setCornerButtonOnClick(v);
			}
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		//Log.d("DEBUG_D", "OnTouch");
		if (view.getTag() instanceof InspectItemViewState)
		{
			final int X = (int) event.getRawX();
		    final int Y = (int) event.getRawY();
		    
		    
		    //ViewGroup vg = ((ViewGroup)view);
		    //View subView = vg.getChildAt(0);//get subview from framelayout
		    switch (event.getAction() & MotionEvent.ACTION_MASK) {
		        case MotionEvent.ACTION_DOWN:
		        {
		        	
		        	view.bringToFront();
		        	RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		        	//FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
		            _xDelta = X - lParams.leftMargin;
		            _yDelta = Y - lParams.topMargin;
		            
		            //startTime = System.currentTimeMillis();
		            //clickCount++;
		           /*
		             - show border of view
		             - hide corner button
		            * */
		            view.setBackgroundResource(R.drawable.frame_selected);
		            setVisibleConerButton(view,false);
		            view.requestLayout();
		           
		        }
		            break;
		        case MotionEvent.ACTION_UP:
		        {
		        	/*
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
			                break;             
			        }*/
		        	view.setBackgroundColor(0x00000000);
		        	setVisibleConerButton(view,true);
		        	view.requestLayout();
		        	
		        }
		         break;
		        case MotionEvent.ACTION_POINTER_DOWN:
		            break;
		        case MotionEvent.ACTION_POINTER_UP:
		            break;
		        case MotionEvent.ACTION_MOVE:
		        	
		        	if (bMovingRight)
		        	{
		        		Log.d("DEBUG_D", "MOVE");
		        	}
		        	RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		            layoutParams.leftMargin = X - _xDelta;
		            layoutParams.topMargin = Y - _yDelta;
		            layoutParams.rightMargin = -250;
		            layoutParams.bottomMargin = -250;
		            view.setLayoutParams(layoutParams);
		            view.requestLayout();	
		        	InspectItemViewState state = (InspectItemViewState)view.getTag();
					if (!state.isViewRendered())
					{
						state.setFullHeightView(view.getHeight());
						state.setFullWidthView(view.getWidth());
						state.setViewRendered(true);
					}
		            //Log.d("DEBUG_D", "view w="+view.getWidth()+" h = "+view.getHeight());
		            break;
		    }	
		}else if (view instanceof ImageButton)
		{
			int x = (int)event.getRawX();
			int y = (int)event.getRawY();
			
			ViewGroup vgInspectItemView = (ViewGroup)view.getParent();
			InspectItemViewState state = (InspectItemViewState)vgInspectItemView.getTag();
			
			ImageView imgView = (ImageView)vgInspectItemView.findViewById(R.id.inspect_img);					
			 switch (event.getAction() & MotionEvent.ACTION_MASK) {
			 	case MotionEvent.ACTION_DOWN:{
	 				 
			 		switch(view.getId())
			 		{
			 			case R.id.btn_rotate:
			 			{
			 				int[] locs = new int[2];
						     imgView.getLocationOnScreen(locs);
						     centerImgX = locs[0] + state.getImgWidth()/2;
						     centerImgY = locs[1] + state.getImgHeight()/2;
						     startAngle = Math.toDegrees(Math.atan2(y - centerImgY , centerImgX - x));
						     Log.d("DEBUG_D", "Action_down = "+startAngle); 						     
			 			}
			 			break;
			 		}
			 	}break;
			 	case MotionEvent.ACTION_MOVE:{					 		 

			 		switch(view.getId())
			 		{
			 			case R.id.btn_right:{
					 		 int[] locs = new int[2];
						     imgView.getLocationOnScreen(locs);

						     int newWidth = 0;
						     int newHeight = 0;
						     
						     
						     newWidth =  x - (locs[0]);//layoutWidth + adjustWidth;
						     newHeight = imgView.getHeight();//layoutHeight - adjustHeight;			
				    	 
						     
							 Log.d("DEBUG_D", "new width = "+newWidth);
							 resizeVectorImgView(imgView,newWidth,newHeight);							 
							 vgInspectItemView.requestLayout();			 			
			 			}
			 			break;
			 			case R.id.btn_down:{
					 		 int[] locs = new int[2];
						     imgView.getLocationOnScreen(locs);
					 		 int newWidth =  imgView.getWidth();//layoutWidth + adjustWidth;
							 int newHeight = state.getImgHeight() + (y - (locs[1]));//layoutHeight - adjustHeight;
							 Log.d("DEBUG_D", "new height = "+newHeight);
							 resizeVectorImgView(imgView,newWidth,newHeight);							 
							 vgInspectItemView.requestLayout();			 						 				
			 			}break;
			 			case R.id.btn_rotate:{
//						     float rotate = vgInspectItemView.getRotation();
//						     rotate = (float)getAngle(state.getImgWidth(),state.getImgHeight(),x,y);
						     
						     /*
						      double currentAngle = getAngle(event.getX(), event.getY());
                			 rotateDialer((float) (startAngle - currentAngle));
                			 startAngle = currentAngle;
						      */
//						     float currentAngle =  (float)getAngle(centerImgY,centerImgX,x,y);
						     
						     //Log.d("DEBUG_D", "ROTATE startAngel="+startAngle+" currentAngle="+currentAngle);
//						     float rotate = vgInspectItemView.getRotation();

//						     rotate = (float)(startAngle-currentAngle);
						     //
						     // atan2(mouseY - cirleCenterY, circleCenterX - mouseX);
			 				/*
						     StringBuilder t = new StringBuilder();
						     t.append("ptCenterImgX = "+centerImgX);
						     t.append(",ptCenterImgY = "+centerImgY);
						     t.append(", X = "+x+", Y = "+y);
						     Log.d("DEBUG_D", t.toString());
						     */
						     double degrees = Math.toDegrees(Math.atan2(y - centerImgY , centerImgX - x));
						     Log.d("DEBUG_D", "Degrees = "+(-1*degrees));
//						     rotate += degrees;
//						     vgInspectItemView.setRotation((float)(startAngle - currentAngle));
						     vgInspectItemView.setRotation((float)((-1*degrees)+Math.abs(startAngle)));
						     //startAngle = currentAngle;
						     
			 			}break;
			 		}
			 		
			 	}break;
			 	case MotionEvent.ACTION_UP:{
			 		 switch(view.getId())
			 		 {
			 		 	case R.id.btn_right:{
			 		 		state.setImgWidth(imgView.getWidth());
			 		 	}break;
			 		 	case R.id.btn_down:{
			 		 		state.setImgHeight(imgView.getHeight());
			 		 	}break;
			 		 	case R.id.btn_rotate:{
			 		 		//startAngle = vgInspectItemView.getRotation();
			 		 	}break;
			 		 }
			 	}break;
			 }

/*			 
			switch(view.getId())
			{
				case R.id.btn_right:{
					//Log.d("DEBUG_D", "RIGHT x="+x+" y="+y);					
				}
				break;
				case R.id.btn_down:{
					Log.d("DEBUG_D", "DOWN");
					
				}break;
				case R.id.btn_del:{
					Log.d("DEBUG_D", "DEL");
					
				}break;
				case R.id.btn_rotate:{
					Log.d("DEBUG_D", "ROTATE");

				}break;
			}
		}
		*/
		
		
		}
		return true;
	}
	
	private void setVisibleConerButton(View vInspectItemView,boolean bShow)
	{
		ImageButton btnDown = (ImageButton)vInspectItemView.findViewById(R.id.btn_down);
		ImageButton btnRight = (ImageButton)vInspectItemView.findViewById(R.id.btn_right);
		ImageButton btnDel = (ImageButton)vInspectItemView.findViewById(R.id.btn_del);
		ImageButton btnRotate = (ImageButton)vInspectItemView.findViewById(R.id.btn_rotate);
		
		if (bShow)
		{
			//btnDown.setVisibility(View.VISIBLE);
			//btnRight.setVisibility(View.VISIBLE);
			btnDel.setVisibility(View.VISIBLE);
			btnRotate.setVisibility(View.VISIBLE);
		}else{
			btnDown.setVisibility(View.INVISIBLE);
			btnRight.setVisibility(View.INVISIBLE);
			btnDel.setVisibility(View.INVISIBLE);
			btnRotate.setVisibility(View.INVISIBLE);
		}
	}
	private void setCornerButtonOnClick(View vInspectItemView)
	{
		ImageButton btnDown = (ImageButton)vInspectItemView.findViewById(R.id.btn_down);
		ImageButton btnRight = (ImageButton)vInspectItemView.findViewById(R.id.btn_right);
		ImageButton btnDel = (ImageButton)vInspectItemView.findViewById(R.id.btn_del);
		ImageButton btnRotate = (ImageButton)vInspectItemView.findViewById(R.id.btn_rotate);

		
		btnDown.setOnTouchListener(this);
		btnRight.setOnTouchListener(this);
		btnDel.setOnTouchListener(this);
		btnRotate.setOnTouchListener(this);
		
		
	}
/*
	@Override
	public boolean onLongClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId())
		{
			case R.id.btn_right:{
				Log.d("DEBUG_D", "RIGHT");
				int[] locs = new int[2];
				view.getLocationOnScreen(locs);
				Log.d("DEBUG_D", "RIGHT  x="+locs[0]+" y="+locs[1]);
				bMovingRight = true;
			}
			break;
			case R.id.btn_down:{
				Log.d("DEBUG_D", "DOWN");
				
			}break;
			case R.id.btn_del:{
				Log.d("DEBUG_D", "DEL");
				
			}break;
			case R.id.btn_rotate:{
				Log.d("DEBUG_D", "ROTATE");

			}break;
		}
		return true;
	}
*/
	private void resizeVectorImgView(ImageView imgView,int newWidth,int newHeight)
	{
		 PictureDrawable drawable = (PictureDrawable)imgView.getDrawable();
		 Bitmap bmpNew = Bitmap.createBitmap(newWidth, newHeight,Bitmap.Config.ARGB_4444);
		 
		 
		 Canvas canvas = new Canvas(bmpNew);

		 Picture resizePicture = new Picture();

		 canvas = resizePicture.beginRecording(newWidth, newHeight);

		 canvas.drawPicture(drawable.getPicture(), new Rect(0,0,newWidth, newHeight));

		 resizePicture.endRecording();

		 Drawable vectorDrawing = new PictureDrawable(resizePicture);
		 
		 imgView.setImageDrawable(vectorDrawing);
	}
	/**
	 * @return The angle of the unit circle with the image view's center
	 */
	private double getAngle(int width,int height, double xTouch, double yTouch) {
	    double x = xTouch - (width / 2d);
	    double y = width - yTouch - (height / 2d);
	    
	    switch (getQuadrant(x, y)) {
	        case 1:
	            return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
	        case 2:
	            return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
	        case 3:
	            return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
	        case 4:
	            return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
	        default:
	            return 0;
	    }
	}
	/**
	 * @return The selected quadrant.
	 */
	private static int getQuadrant(double x, double y) {
	    if (x >= 0) {
	        return y >= 0 ? 1 : 4;
	    } else {
	        return y >= 0 ? 2 : 3;
	    }
	}
}
