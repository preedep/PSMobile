package com.epro.psmobile.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.epro.psmobile.R;
import com.epro.psmobile.action.OnClickInspectDrawingView;
import com.epro.psmobile.action.OnResizedInspectItemDrawingView;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.LayoutItemScaleHistory;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.ImageUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.ReportInspectSummaryStatusHelper;
import com.epro.psmobile.util.ScreenUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.view.InspectItemViewState;
import com.epro.psmobile.view.InspectRelativeLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class DrawingViewBaseFragment extends ContentViewBaseFragment implements OnTouchListener {

	private int _xDelta;
	private int _yDelta;
	
//	private double mCurrAngle = 0;
//	private double mPrevAngle = 0;
//   private float xc = 0;
//    private float yc = 0;

//	private long startTime;
//	private int clickCount;
    //variable for calculating the total time
//	private long duration;
	    //constant for defining the time duration between the click that can be considered as double-tap
//	private static final int MAX_DURATION = 500;
	private double startAngle;
	
	private ArrayList<View> viewList;
	
	//private boolean bMovingRight = false;
	private int centerImgX;
	private int centerImgY;
	
	private int maxObjectId = 0;
	private ViewGroup vContainer;
	private OnClickInspectDrawingView onClickInspectDrawingView;
	private OnResizedInspectItemDrawingView onResizedInspectItemDrawingView;
	
	private double baseWidth = 100;
	private double baseHeight = 100;
	
	//public static int LONG_PRESS_TIME = 1500; // Time in miliseconds 

	//private Timer longpressTimer; //won't depend on a motion event to fire
    //private final int longpressTimeDownBegin = LONG_PRESS_TIME; //0.5 s
    //private boolean bCancelLongPress = false;
    private Point previousPoint;

    private int[] locWindows = new int[2];
	
    private ScrollView vScroll;
    private HorizontalScrollView hScroll;

    private static int centerXOfImageOnScreen;
    private static int centerYOfImageOnScreen;

    private Task currentTask;
    
    private View currentViewSelected;
    private AlertDialog.Builder builderSingleInspectListDlg;
    private boolean isCallFromLongPress = false;
    
    
    protected ViewGroup viewObjContainer;
	public DrawingViewBaseFragment() {
		// TODO Auto-generated constructor stub
		viewList = new ArrayList<View>();
	}

	public void setExternalScroll(ScrollView vScroll,HorizontalScrollView hScroll)
	{
		this.vScroll = vScroll;
		this.hScroll = hScroll;
	}
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		/*
		ActionBar actionBar = this.getSherlockActivity().getSupportActionBar();
		if (actionBar != null)
		{
			actionBar.hide();
		}
		Fragment f = this.getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
		if (f != null)
		{
			FragmentTransaction ft = 
					this.getSherlockActivity().getSupportFragmentManager().beginTransaction();
			ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
			ft.hide(f);
			ft.commit();
		}*/
		/*
		 * 
		 */
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
		
		try {
			currentTask = this.getArguments().getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
			CustomerSurveySite site = this.getArguments().getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
			
			
			//////////
			LayoutItemScaleHistory scale = dataAdapter.getLayoutScale(currentTask.getTaskCode(), site.getCustomerSurveySiteID());
			if (scale != null){
				this.setBaseWidth(scale.getSiteWidth());
				this.setBaseHeight(scale.getSiteLong());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.onCreateView(arg0, arg1, arg2);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		
	}
	/*
	@SuppressWarnings("deprecation")
   final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
	    public void onLongPress(MotionEvent e) {
	        Log.d("DEBUG_D_D", "Longpress detected");
	    }
	});*/

	final Handler handler = new Handler(); 
	
	Runnable mLongPressed = new Runnable() { 
	    public void run() { 
	        /*
             * show all object inspecting list 
             */
	        if (currentViewSelected == null)
	           return;
	        
	        Log.d("DEBUG_D_D", "Long press!");
            
	        
	        
	        /*find object inspecting*/
	        final ArrayList<View> intersectList = new ArrayList<View>();
	        intersectList.clear();
	        
	        if ((viewList != null) && (viewList.size() > 0))
	        {
	           InspectItemViewState viewStateCurrentView = null;
	           if (currentViewSelected.getTag() instanceof InspectItemViewState ){
	              viewStateCurrentView  = 
	                    (InspectItemViewState)currentViewSelected.getTag();
	              Rect currentViewRect = new Rect();
	              currentViewSelected.invalidate();
	              currentViewSelected.getBackground().copyBounds(currentViewRect);
	              
	              int[] locs = new int[2];
	              currentViewSelected.getLocationOnScreen(locs);
	              currentViewRect.left = locs[0];
	              currentViewRect.top = locs[1];
	              currentViewRect.right = currentViewRect.left + currentViewRect.right;
	              currentViewRect.bottom = currentViewRect.top + currentViewRect.bottom;
	              
	              Log.d("DEBUG_D_D", "currentViewRect -> "+currentViewRect.toShortString());
	              /*
	              currentViewRect.left = (int) viewStateCurrentView.getInspectDataObjectSaved().getInspectDataItemStartX();
	              currentViewRect.top = (int)viewStateCurrentView.getInspectDataObjectSaved().getInspectDataItemStartY();
	              currentViewRect.right = currentViewRect.left + (int)viewStateCurrentView.getInspectDataObjectSaved().getWidthObject();
	              currentViewRect.bottom = currentViewRect.top + (int)viewStateCurrentView.getInspectDataObjectSaved().getLongObject();
	              */
//	              currentViewSelected.getLocalVisibleRect(currentViewRect);
	              
	              for(View v : viewList){
	                  Object vObj = v.getTag();
	                  if (vObj instanceof InspectItemViewState)
	                  {
	                     InspectItemViewState viewState = (InspectItemViewState)vObj;

	                    
	                     if (!viewState.getInspectDataObjectSaved().isAuthorized()){
	                        continue;
	                     }
	                     /*what view intersect current view*/
	                     Rect eachViewRect = new Rect();
	                     //v.getLocalVisibleRect(eachViewRect);
	                     v.invalidate();
	                     v.getBackground().copyBounds(eachViewRect);
	                     
	                     locs = new int[2];
	                     v.getLocationOnScreen(locs);
	                     eachViewRect.left = locs[0];
	                     eachViewRect.top = locs[1];
	                     eachViewRect.right = eachViewRect.left + eachViewRect.right;
	                     eachViewRect.bottom = eachViewRect.top + eachViewRect.bottom;
	                     
	                     Log.d("DEBUG_D_D", "eachViewRect -> "+eachViewRect.toShortString());
	                     
	                     /*
	                     eachViewRect.left = (int) viewState.getInspectDataObjectSaved().getInspectDataItemStartX();
	                     eachViewRect.top = (int)viewState.getInspectDataObjectSaved().getInspectDataItemStartY();
	                     eachViewRect.right = currentViewRect.left + (int)viewState.getInspectDataObjectSaved().getWidthObject();
	                     eachViewRect.bottom = currentViewRect.top + (int)viewState.getInspectDataObjectSaved().getLongObject();
	                      */
	                     /*
	                     if (currentViewRect.intersect(eachViewRect))
	                     {
	                        intersectList.add(v);
	                     }else{
	                        if (currentViewRect.contains(eachViewRect)){
	                           intersectList.add(v);
	                        }
	                     }*/
	                     if (currentViewRect.contains(eachViewRect)){
                            intersectList.add(v);
                         }else if (Rect.intersects(currentViewRect, eachViewRect)){
                            intersectList.add(v);                            
                         }
	                  }
	               }

	           }
	           
	           if (intersectList.size() > 1)/* 1 is single object intersecting , no possible*/{
	              builderSingleInspectListDlg = new AlertDialog.Builder(getSherlockActivity());
	              builderSingleInspectListDlg.setTitle(R.string.title_list_obj_inspecting);
	              final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	                      getSherlockActivity(),
	                      android.R.layout.simple_list_item_1);
	              
	              for(View v : intersectList){
	                 InspectItemViewState item = (InspectItemViewState)v.getTag();
	                 
	                 StringBuilder strTextDisplay = new StringBuilder();

	                 strTextDisplay.append(item.getInspectDataObjectSaved().getInspectDataObjectID()+". ");
	                 if (!item.getInspectDataObjectSaved().getObjectName().isEmpty()){
	                    strTextDisplay.append(item.getInspectDataObjectSaved().getObjectName()+"," );
	                 }
	                 strTextDisplay.append(item.getInspectDataItem().getInspectDataItemName());
	                 /*
	                 arrayAdapter.add(item.getInspectDataObjectSaved().getInspectDataObjectID()+". " +
	                 		""+item.getInspectDataObjectSaved().getObjectName()+" , " +
	                 				""+item.getInspectDataItem().getInspectDataItemName());*/
	                 arrayAdapter.add(strTextDisplay.toString());
	              }
	              builderSingleInspectListDlg.setAdapter(arrayAdapter,new OnClickListener(){

	                 @Override
	                 public void onClick(DialogInterface dialog, int which) {
	                  // TODO Auto-generated method stub
	                     View v = intersectList.get(which);
//	                     v.bringToFront();
//	                     v.performClick();
//	                     setSelected(v,true,false);
	                     
	                  // Obtain MotionEvent object
	                     long downTime = SystemClock.uptimeMillis();
	                     long eventTime = SystemClock.uptimeMillis();
	                     
	                     int[] locs = new int[2];
	                     v.getLocationOnScreen(locs);
	                     float x = locs[0];
	                     float y = locs[1];
	                     // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
	                     int metaState = 0;
	                     
	                     isCallFromLongPress = true;
	                     MotionEvent motionEvent = MotionEvent.obtain(
	                         downTime, 
	                         eventTime, 
	                         MotionEvent.ACTION_DOWN, 
	                         x, 
	                         y, 
	                         metaState
	                     );

	                     // Dispatch touch event to view
	                     v.dispatchTouchEvent(motionEvent);
	                     
	                     isCallFromLongPress = false;
	                     
	                     dialog.dismiss();
	                 }
	              });
	              
	              
	              Toast toast = Toast.makeText(getSherlockActivity(),R.string.obj_intersecting_toast_dlg_popup, Toast.LENGTH_LONG);
	              toast.setGravity(Gravity.BOTTOM, 0, 0);
	              toast.show();
	              
	              builderSingleInspectListDlg.show();
	           }
	        }
	        
           
	    }   
	};
	
	public boolean onTouch(final View view, MotionEvent event) {
		// TODO Auto-generated method stub
		//Log.d("DEBUG_D", "OnTouch");
		boolean bRet = true;
		
		if (view.getId() == R.id.layout_container)
		{
			switch(event.getAction() & MotionEvent.ACTION_MASK)
			{
				case MotionEvent.ACTION_DOWN:{
				    currentViewSelected = null;
					for(View vInspectObj : viewList)
		            {
						setSelected(vInspectObj,true,false);
		            }
		            if (getOnClickInspectDrawingView() != null)
		            {
		            	getOnClickInspectDrawingView().onClickInspectItemDrawing(view,null);
		            }
				}
				break;
			}
		}
		if (view.getTag() instanceof InspectItemViewState)
		{

		    float mFactor = 1.0f;
		    if (viewObjContainer != null){
		       InspectRelativeLayout inspectLayout = (InspectRelativeLayout)viewObjContainer;
		       mFactor = inspectLayout.getCurrentScaleFactor();
		       
		       Log.d("DEBUG_D_D", "Factor -> "+mFactor);
		    }
		    
		    if (mFactor != 1.0f){
		       return false;
		    }
		    
			final int X = (int) (event.getRawX() / mFactor);
		    final int Y = (int) (event.getRawY() / mFactor);
		    
		    
		    
		    //Log.d("DEBUG_D","inspect item view X = "+X+" , Y = "+Y);
		    //ViewGroup vg = ((ViewGroup)view);
		    //View subView = vg.getChildAt(0);//get subview from framelayout
		    switch (event.getAction() & MotionEvent.ACTION_MASK)
		    {
		        case MotionEvent.ACTION_DOWN:
		        {
		            Log.d("DEBUG_D_D"," Action down ");
		            currentViewSelected  = view;/*save current view for use in long pressed delay*/
		            
		            if (!isCallFromLongPress){
		               handler.postDelayed(mLongPressed, 1000);
		            }
		        	previousPoint = null;
		        	view.bringToFront();
		        	RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		        	//FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
		        	
		        	/*
		        	int widhtObj = ((InspectItemViewState)view.getTag()).getInspectDataObjectSaved().getWidthObject();
		        	int heighObj = ((InspectItemViewState)view.getTag()).getInspectDataObjectSaved().getLongObject();
		        	
		        	lParams.width = (int)(widhtObj * mFactor);
		        	lParams.height = (int)(heighObj * mFactor);
		        	*/
		        	
                    //lParams.leftMargin = (int)(lParams.leftMargin * mFactor);
                    //lParams.leftMargin = (int)(lParams.leftMargin * mFactor);
		        	
		            _xDelta = X - (int)(lParams.leftMargin * mFactor);
		            _yDelta = Y - (int)(lParams.topMargin * mFactor);
		            
		            
		            //startTime = System.currentTimeMillis();
		            //clickCount++;
		           /*
		             - show border of view
		             - hide corner button
		            * */
	            	final InspectItemViewState vState = (InspectItemViewState)view.getTag();

		            for(View vInspectObj : viewList)
		            {
		            	InspectItemViewState vInspectObjState = (InspectItemViewState)vInspectObj.getTag();
		            	if (vInspectObjState.getObjectId() == vState.getObjectId())
		            	{	            		
		            		setSelected(view,true,false);
		            		view.invalidate();
		            	}else{
		            		setSelected(vInspectObj,true,false);
		            		vInspectObj.invalidate();
		            	}
		            }
		            
		            //setSelected(view,true,false);
		        	setVisibleConerButton(view,true);

		            //bRet = mGestureDetector.onTouchEvent(event);
		            
		            if (getOnClickInspectDrawingView() != null)
		            {
		            	//vState.setPressed(true);
		            	
			        	Log.d("DEBUG_D", "Action down objectId = "+vState.getObjectId());

			     			        	
		            	getOnClickInspectDrawingView().onClickInspectItemDrawing(view,
		            			vState
		            			);
		            	
		               	//setResizeInspectItemSize(view, vState.getImgWidth(), vState.getImgHeight(), true);

		            }
		            view.requestLayout();
		            view.invalidate();	
		        }
		        break;
		        case MotionEvent.ACTION_UP:
		        {
		           currentViewSelected = null;
		           isCallFromLongPress = false;
		           handler.removeCallbacks(mLongPressed);
		           // bCancelLongPress = true;//reset	     
		        	setSelected(view,true,false);
		        	setVisibleConerButton(view,true);
		        	
		        	InspectItemViewState vState = (InspectItemViewState)view.getTag();
	            	//vState.setPressed(false);
		        	
		        	Log.d("DEBUG_D", "Action up objectId = "+vState.getObjectId());
	            	
		        }
		         break;
		        case MotionEvent.ACTION_POINTER_DOWN:
		            break;
		        case MotionEvent.ACTION_POINTER_UP:
		            break;
		        case MotionEvent.ACTION_MOVE:
		        	Point currentPoint = new Point((int)event.getRawX(), (int)event.getRawY());

		            if(previousPoint == null){
		                previousPoint = currentPoint;
		                
		                Log.d("DEBUG_D", "currentPont.x = "+currentPoint.x+", currentPoint.y = "+currentPoint.y);
		                Log.d("DEBUG_D", "previousPont.x = "+previousPoint.x+", previousPoint.y = "+previousPoint.y);

		            }
		            int dx = Math.abs(currentPoint.x - previousPoint.x);
		            int dy = Math.abs(currentPoint.y - previousPoint.y);
		            double s =  Math.sqrt(dx*dx + dy*dy);
		            
		            Log.d("DEBUG_D", "MOVE dx = "+dx+", dy = "+dy+" , s = "+s);
		            boolean isActuallyMoving = (s >= 1.5); //we're moving
		            if (isActuallyMoving){
		               currentViewSelected = null;
		               isCallFromLongPress = false;
	                   handler.removeCallbacks(mLongPressed);
		            }
		            //if (isActuallyMoving)		            	
		            //	bCancelLongPress = true;//reset     
		            
		        	//if (bMovingRight)
		        	//{
		        	//	Log.d("DEBUG_D", "MOVE_RIGHT");
		        	//}
		        	RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		        	
		        	int subX =  X - _xDelta;
		        	int subY = Y - _yDelta;
		        	
		        	//if ((subX >= 5)&&(subY >= 5))
		        	if (isActuallyMoving)
		        	{
		        		if ((subX >= 0)&&(subY >= 0)){
		        			layoutParams.leftMargin = X - _xDelta;
		        			layoutParams.topMargin = Y - _yDelta;
		        			this.setLayoutModified(currentTask,false);
		        		}
		        	}		        	
		            Log.d("DEBUG_D", "layoutParams.leftMargin = "+layoutParams.leftMargin+" " +
		            		", layoutParams.topMargin = "+layoutParams.topMargin
		            		);
		            /*
		            layoutParams.rightMargin = -1 * (view.getLayoutParams().width);
		            layoutParams.bottomMargin = -1 * (view.getLayoutParams().height);
		            */
		            
		            view.setLayoutParams(layoutParams);
		            
		            InspectItemViewState state = (InspectItemViewState)view.getTag();
					if (!state.isViewRendered())
					{
						state.setFullHeightView(view.getLayoutParams().height);
						state.setFullWidthView(view.getLayoutParams().width);
						state.setViewRendered(true);						
					}
					
					state.getInspectDataObjectSaved().setInspectDataItemStartX(layoutParams.leftMargin);
					state.getInspectDataObjectSaved().setInspectDataItemStartY(layoutParams.topMargin);
					
					
					view.invalidate();
		            break;
		    }	
		}else if (view instanceof ImageButton)
		{
			int x = (int)event.getRawX();
			int y = (int)event.getRawY();
			
			ViewGroup vgInspectItemView = (ViewGroup)view.getParent();
			InspectItemViewState state = (InspectItemViewState)vgInspectItemView.getTag();
			
			//ImageView imgView = (ImageView)vgInspectItemView.findViewById(R.id.inspect_img);					
			 switch (event.getAction() & MotionEvent.ACTION_MASK) {
			 	case MotionEvent.ACTION_DOWN:
			 	{
	 				 
			 		switch(view.getId())
			 		{
			 			case R.id.btn_info:{
			 				/*
			 				 * show dialog
			 				 */
			 				Log.d("DEBUG_D", "Press info..");
			 			}
			 			break;
			 			case R.id.btn_rotate:
			 			{
			 				int[] locs = new int[2];
						     //imgView.getLocationOnScreen(locs);
			 				 vgInspectItemView.getLocationOnScreen(locs);
						     centerImgX = locs[0] + state.getImgWidth()/2;
						     centerImgY = locs[1] + state.getImgHeight()/2;
						     
						     //vgInspectItemView.clearAnimation();
						     
						     //xc = state.getImgWidth()/2;
						     //yc = state.getImgHeight()/2;
						     
						     //mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
						     //http://stackoverflow.com/questions/19271430/rotate-and-resize-the-image-view-with-single-finger-in-android
						     //http://stackoverflow.com/questions/6689320/how-to-get-center-of-an-imageview-which-is-a-circle
//						     startAngle = Math.toDegrees(Math.atan2(y - centerImgY , centerImgX - x));
//						     startAngle = Math.toDegrees(Math.atan2(x - centerImgX , centerImgY - y));
						     
						     
						     	ImageButton btnDel = (ImageButton)vgInspectItemView.findViewById(R.id.btn_del);
						     	ImageButton btnRotate = (ImageButton)vgInspectItemView.findViewById(R.id.btn_rotate);
						     
		                        int locations[] = new int[2];
		                        btnDel.getLocationOnScreen(locations);
		                        int xOpposite = locations[0];//btnDel.getLeft();
		                        int yOpposite = locations[1];//btnDel.getTop();

		                        int locationsConner[] = new int[2];
		                        btnRotate.getLocationOnScreen(locationsConner);
		                        int xConner = locationsConner[0]+btnRotate.getWidth();//btnRotate.getRight();
		                        int yConner = locationsConner[1]+btnRotate.getHeight();//btnRotate.getBottom();

		                        float centerX = (xConner + xOpposite)/2;
		                        float centerY = (yConner + yOpposite)/2;

		                        //int centerXOnImage=myImageView.getWidth()/2;
		                        //int centerYOnImage=myImageView.getHeight()/2;
		                        centerXOfImageOnScreen=(int)centerX;
		                        centerYOfImageOnScreen=(int)centerY;

		                        //imageWidth = obj.getWidth();
		                        //imageHeight = obj.getHeight();
		                        /*
		                        startAngle = (float)Math.toDegrees(
		                                Math.atan2(Y - centerYOfImageOnScreen , centerXOfImageOnScreen - X));
		                        */

		                        startAngle = (float)getAngle2(x,y);

						 	
						     //	private double calAngle(double x1,double y1,double x2,double y2)

						     //startAngle = calAngle(x,y,centerImgX,centerImgY);
						     
						     Log.d("DEBUG_D", "Action_down = "+startAngle+
						    		 " getQuadrant "+ getQuadrant(x,y)+
						    		 " \r\ncenterImgX = "+centerImgX + " , centerImgY = "+centerImgY + 
						    		 " \r\n===== "); 			
						     /*
						     double degrees = Math.toDegrees(Math.atan2(y - centerImgY , centerImgX - x));
						     double rotateTo = (float)((-1*degrees)+Math.abs(startAngle));

						     Log.d("DEBUG_D", "ACTION DOWN -> rotate to "+rotateTo+" start angel "+startAngle);
						     
						     vgInspectItemView.setRotation((float)rotateTo);
						     vgInspectItemView.invalidate();
						      */
						     
						     //startAngle = vgInspectItemView.getRotation();
						     //vgInspectItemView.setRotation((float) startAngle);
						     
						     
//						     double degrees = Math.toDegrees(Math.atan2(y - centerImgY , centerImgX - x));
//						     double rotateTo = (float)((-1*degrees)+Math.abs(startAngle));

//						     Log.d("DEBUG_D", "rotate to "+rotateTo);
						     
//						     vgInspectItemView.setRotation((float)rotateTo);

			 			}
			 			break;
			 			case R.id.btn_down:
			 			case R.id.btn_right:{
			 				//imgView.getLocationOnScreen(locWindows);
			 				vgInspectItemView.getLocationOnScreen(locWindows);
			 				
			 				//int paddingPixel = (int)ScreenUtil.convertDpToPixel(0, this.getActivity());
			 				int newHeight = vgInspectItemView.getLayoutParams().height;//imgView.getHeight()-(2*paddingPixel);//layoutHeight - adjustHeight;
						    int newWidth = vgInspectItemView.getLayoutParams().width;//imgView.getWidth()-(2*paddingPixel);  

						    
						    
						    /*
						    ScreenUtil.PixelInSize pz = ScreenUtil.convertPixelToRealSize(getActivity(),
						    		newHeight, 
						    		newWidth);
						    */
						    if (state.getInspectDataItem().isLine())
						    {
						    	/*
						    	 * line fix height
						    	 */
						    	if (state.getImgHeight() <= 0)
						    	{
						    		state.setImgHeight(newHeight);
						    	}
						    }else{
						    	state.setImgHeight(newHeight);
						    }
							state.setImgWidth(newWidth);
							
							vgInspectItemView.invalidate();
			 			}break;
			 		}
			 	}break;
			 	case MotionEvent.ACTION_MOVE:{					 		 

			 		switch(view.getId())
			 		{
			 			case R.id.btn_right:{
			 				
							//double angle = calAngle(x,centerImgX,y,centerImgY);
								
							//int quadrant = getQuadrant(newWidth,newHeight,locs[0],locs[1]);
							//Log.d("DEBUG_D"," angle = "+angle);

								
						     int newWidth = 0;
						     int newHeight = 0;
						     
						     int paddingPixel = (int)ScreenUtil.convertDpToPixel(0, this.getActivity());

						     int newX = (x - locWindows[0] - (2*paddingPixel));
						     int xx = newX * newX;
						     
						     int newY = (y - locWindows[1]);
						     int yy = newY * newY;
						     
						     newWidth = (int)Math.sqrt(xx + yy);
						     
						     newHeight = state.getImgHeight();
						     
							 Log.d("DEBUG_D", "new width = "+newWidth+", new height = "+newHeight);
							 try{
								 if ((ScreenUtil.convertPixelsToDp(newWidth,getActivity()) >= ScreenUtil.MIN_OBJ_WIDTH_DISPLAY_DP) &&
									  (ScreenUtil.convertPixelsToDp(newWidth,getActivity()) <= ScreenUtil.MAX_OBJ_WIDTH_DISPLAY_DP))
								 {
									 resizeBorderView(vgInspectItemView,
											 newWidth,
											 newHeight);							 
									 //vgInspectItemView.requestLayout();		
								 
									 state.setImgWidth(newWidth);
								 
									 setResizeInspectItemSize(vgInspectItemView, 
											 -1,
											 -1,
											 newWidth, 
											 newHeight, 
											 true);
									 
									 vgInspectItemView.invalidate();
								 }
							 }catch(Exception ex){ex.printStackTrace();}
			 			}
			 			break;
			 			case R.id.btn_down:{
					 		 //int[] locs = new int[2];
					 		 int paddingPixel = (int)ScreenUtil.convertDpToPixel(0, this.getActivity());						     
						     //imgView.getLocationOnScreen(locs);
					 		 int newWidth =  state.getImgWidth();//imgView.getWidth();//layoutWidth + adjustWidth;
							 
							 int newX = (x - locWindows[0]);
						     int xx = newX * newX;
						     
						     int newY = (y - locWindows[1]) - (2*paddingPixel);
						     int yy = newY * newY;
						     
						     int newHeight = (int)Math.sqrt(xx + yy);
						     
							 Log.d("DEBUG_D", "new height = "+newHeight);
							 try{
								 
								 if ((ScreenUtil.convertPixelsToDp(newHeight,getActivity()) >= ScreenUtil.MIN_OBJ_HEIGHT_DISPLAY_DP) &&
										  (ScreenUtil.convertPixelsToDp(newHeight,getActivity()) <= ScreenUtil.MAX_OBJ_HEIGHT_DISPLAY_DP))
								 {
									 resizeBorderView(vgInspectItemView,
											 newWidth,
											 newHeight);							 
									 
									 state.setImgHeight(newHeight);
								 
									 setResizeInspectItemSize(vgInspectItemView, 
											 -1,
											 -1,
											 newWidth, 
											 newHeight, true);
									 
									 vgInspectItemView.invalidate();
							     }
							 }catch(Exception ex){ex.printStackTrace();}
							 
			 			}break;
			 			case R.id.btn_rotate:{
			 				/*
						     double degrees = //Math.toDegrees(Math.atan2(x - centerImgX , centerImgY - y));
							 	
						    		 Math.toDegrees(Math.atan2(y - centerImgY , centerImgX - x));
						     
						     double beforeAngle = startAngle;
						     
						     double rotateTo = (float)((-1*degrees)+Math.abs(startAngle));

						     Log.d("DEBUG_D", "centerImgX = "+centerImgX + ", centerImgY = "+centerImgY +
						     		"\r\nrotate to "+rotateTo+" " +
						    		"\r\n before angel "+beforeAngle+						    	
						     		"\r\nstart angel "+startAngle + 
						     		" \r\ndegree "+degrees+
						     		" \r\ngetQuadrant "+ getQuadrant(x,y)+
						     		" \r\n=========\r\n");
						     		*/
			 				float degrees = (float)getAngle2(x,y);

//                          double rotateTo = (float)((-1*degrees)+Math.abs(startAngle));
//                          float rotateTo = (float)((1*degrees)+Math.abs(startAngle));

                          //double rotateTo = (Math.toDegrees(Math.atan2(centerYOfImageOnScreen - Y, centerXOfImageOnScreen - X)));

//                          double currentAngle = getAngle2(event.getX(), event.getY());
//                          obj.setRotation((float) (startAngle - currentAngle));
//                          startAngle = (float)currentAngle;
                            float rotateTo = (float)(degrees-startAngle);

//						     double rotateTo = calAngle(centerImgX,centerImgY,x,y);

						     /*
						     Matrix mRotate = new Matrix();
						     mRotate.setRotate((float) rotateTo,
						    		 (float)(state.getImgWidth()/2),
						    		 (float)(state.getImgHeight()/2));
	
						     vgInspectItemView.set*/
						     vgInspectItemView.setRotation((float)rotateTo);
//						     mPrevAngle = mCurrAngle;
//						     mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
//						     animate(vgInspectItemView,mPrevAngle, mCurrAngle, 0);

			 				/*
							 double angle = calAngle(x,centerImgX,y,centerImgY);
						     vgInspectItemView.setRotation((float) angle);
						     */
						     
						     vgInspectItemView.invalidate();
			 			}break;
			 		}
			 		
			 	}break;
			 	case MotionEvent.ACTION_UP:{
			 		 switch(view.getId())
			 		 {
			 		 	case R.id.btn_info:{
							if (getOnClickInspectDrawingView() != null)
				            {
								getOnClickInspectDrawingView().onClickLongInspectItemDrawing(
										vgInspectItemView,
										state);
								
			 		 			this.setLayoutModified(currentTask);
				            }
			 		 	}
			 		 	break;
			 		 	case R.id.btn_copy:{
							if (getOnClickInspectDrawingView() != null)
				            {
								getOnClickInspectDrawingView().onClickCopyInspectItemDrawing(
										state);
								
			 		 			this.setLayoutModified(currentTask);
				            }			 		 		
			 		 	}break;
			 		 	case R.id.btn_right:{
			 		 		if (state != null){
			 		 			//state.setImgWidth(imgView.getWidth());
			 		 			state.setImgWidth(vgInspectItemView.getLayoutParams().width);
			 		 			ScreenUtil.PixelInSize pz = ScreenUtil.convertPixelToRealSize(getActivity(),
			 		 					state.getInspectDataObjectSaved(),
			 		 					state.getImgWidth(), 
			 		 					state.getImgHeight());
			 		 			state.getInspectDataObjectSaved().setWidth(pz.sizeWidth);
			 		 			state.getInspectDataObjectSaved().setdLong(pz.sizeLong);
			 		 			
			 		 			this.setLayoutModified(currentTask);
			 		 		}
			 		 	}break;
			 		 	case R.id.btn_down:{
			 		 		if (state != null){
//			 		 		state.setImgHeight(imgView.getHeight());
			 		 			state.setImgHeight(vgInspectItemView.getLayoutParams().height);
			 		 			
			 		 			ScreenUtil.PixelInSize pz = ScreenUtil.convertPixelToRealSize(getActivity(),
			 		 					state.getInspectDataObjectSaved(),
			 		 					state.getImgWidth(), 
			 		 					state.getImgHeight());
			 		 			state.getInspectDataObjectSaved().setWidth(pz.sizeWidth);
			 		 			state.getInspectDataObjectSaved().setdLong(pz.sizeLong);

			 		 			this.setLayoutModified(currentTask);
			 		 		}
			 		 	}break;
			 		 	case R.id.btn_rotate:{
			 		 		//startAngle = vgInspectItemView.getRotation();
//			 		 		mPrevAngle = mCurrAngle = 0;
			 		 		if (state != null){
			 		 			state.getInspectDataObjectSaved().setAngle(vgInspectItemView.getRotation());
			 		 		}
		 		 			this.setLayoutModified(currentTask);

			 		 	}break;
			 		 	case R.id.btn_del:{
			 		 		
			 		 		try{
			 		 		Log.d("DEBUG_D", "delete object id = "+state.getObjectId());
			 		 		
			 		 		for(int i = 0 ; i < vContainer.getChildCount();i++)
			 		 		{
			 		 			View v  = vContainer.getChildAt(i);
			 		 			if (v.getTag() instanceof InspectItemViewState)
			 		 			{
			 		 				InspectItemViewState vStateItem = (InspectItemViewState)v.getTag();
			 		 				if (state.getObjectId() == vStateItem.getObjectId())
			 		 				{

			 		 				    if (!vStateItem.getInspectDataItem().isUniversalLayoutDropdown()){
			 		 				       vContainer.removeView(v);                                          
			 		 				    }
                                       
			 		 					for(View vItem : viewList)
			 		 					{
					 		 				InspectItemViewState vEachItem = (InspectItemViewState)vItem.getTag();
			 		 						if (vEachItem.getObjectId() == state.getObjectId()){
			 		 							/*
			 		 							 * delete picture
			 		 							 */
			 		 							if (vEachItem.getInspectDataItem().isCameraObject())
			 		 							{
			 		 								int photoID = vEachItem.getInspectDataObjectSaved().getPhotoID();
			 		 								String taskCode = vEachItem.getInspectDataObjectSaved().getTaskCode();
			 		 								int customerSurveySiteID = vEachItem.getInspectDataObjectSaved().getCustomerSurveySiteID();
			 		 								try{
			 		 									PSBODataAdapter dataAdapter = 
			 		 											PSBODataAdapter.getDataAdapter(getActivity());
			 		 									int rowEffected = dataAdapter.deletePhotoObjectSaveds(photoID, taskCode, customerSurveySiteID);
			 		 									Log.d("DEBUG_D", "Delete photo saved row effected -> "+rowEffected);
			 		 									viewList.remove(vItem);
			 		 								}catch(Exception ex){
			 		 									ex.printStackTrace();
			 		 								}
			 		 							}else if (vEachItem.getInspectDataItem().isUniversalLayoutDropdown())
			 		 							{
			 		 							   /*
			 		 							    * check have refer 
			 		 							    */
			 		 							   try{
			 		 							    PSBODataAdapter dataAdapter = 
                                                          PSBODataAdapter.getDataAdapter(getActivity());
                                                  
			 		 							     int jobRequestID = vEachItem.getInspectDataObjectSaved().getJobRequestID();
                                                     int customerSurveySiteID = vEachItem.getInspectDataObjectSaved().getCustomerSurveySiteID();
                                                  
                                                     ArrayList<JobRequestProduct> jrpList =
                                                           dataAdapter.findJobRequestProductsByJobRequestIDWithWarehouse(jobRequestID,customerSurveySiteID);
                                                      boolean hasrefer = false;
			 		 							      if (jrpList != null)
			 		 							      {
			 		 							         for(JobRequestProduct jrp : jrpList){
			 		 							            if (jrp.getInspectDataObjectID() == vEachItem.getInspectDataObjectSaved().getInspectDataObjectID()){
			 		 							               /*
			 		 							                * 
			 		 							                */
			 		 							               hasrefer = true;
			 		 							               break;
			 		 							            }
			 		 							         }
			 		 							         if (hasrefer){
			 		 							            /*
			 		 							             * show alert
			 		 							             */
			 		 							            MessageBox.showMessage(getSherlockActivity(), R.string.text_error_title, R.string.universal_error_has_refer);
			 		 							         }else{
    			 		 							         vContainer.removeView(v);
	                                                         viewList.remove(vItem);			 		 							            
			 		 							         }
			 		 							         ///////////////
			 		 							      }else{
			 		 							         vContainer.removeView(v);
			 		 							         viewList.remove(vItem);
			 		 							      }
			 		 							   }catch(Exception ex){}
			 		 							}
			 		 							else{
			 		 								viewList.remove(vItem);
			 		 							}
			 				 		 			this.setLayoutModified(currentTask);

			 		 							break;
			 		 						}
			 		 					}
			 		 					viewList.trimToSize();
			 		 					break;
			 		 				}
			 		 			}
			 		 		}
			 		 		}catch(Exception ex)
			 		 		{
			 		 			Log.d("DEBUG_D", ex.getMessage());
			 		 		}
			 		 		
			 		 	}break;
			 		 }
			 	}break;
			 }
		}
		return bRet;
		//return gestureDetector.onTouchEvent(event);
	}
	protected void setSelected(View inspectViewItem,
			boolean bSelected,
			boolean hideInfo){
		//View vImg = inspectViewItem.findViewById(R.id.inspect_view_body);
		if (bSelected)
		{
			InspectItemViewState viewState = (InspectItemViewState)inspectViewItem.getTag();
			if ((viewState.getInspectDataItem().isInspectObject()||
				viewState.getInspectDataItem().isGodownComponent()))
			{
				if (viewState.getInspectDataItem().isGodownComponent())
				{
					if (viewState.getInspectDataObjectSaved().isAuthorized()){
						inspectViewItem.setBackgroundResource(R.drawable.frame_godown_selected);						
					}else{
						inspectViewItem.setBackgroundResource(R.drawable.frame_godown_unauthorized_selected);												
					}
				}else{
					inspectViewItem.setBackgroundResource(R.drawable.frame_selected);	
				}
			}else
			{
				inspectViewItem.setBackgroundColor(0x000000);				
			}
		}else{
			inspectViewItem.setBackgroundColor(0x000000);
		}
		
    	setVisibleConerButton(inspectViewItem,false,hideInfo);
    	inspectViewItem.requestLayout();		
	}
	public void setResizeInspectItemSize(
			View inspectViewItem,
			double width/*real object*/,
			double height/*real object*/,
			int widthPixel,
			int heightPixel,
			boolean bShowSize)
	{
		TextView textWidth = (TextView)inspectViewItem.findViewById(R.id.inspect_view_body_width);
		TextView textHeight = (TextView)inspectViewItem.findViewById(R.id.inspect_view_body_height);
		if (bShowSize){
			InspectItemViewState viewState = null;
			if (inspectViewItem.getTag() instanceof InspectItemViewState)
			{
				viewState = (InspectItemViewState)inspectViewItem.getTag();

				if ((width > 0)&&(height > 0)){
					viewState.getInspectDataObjectSaved().setWidth(width);
					viewState.getInspectDataObjectSaved().setdLong(height);					

					ScreenUtil.SizeInPixel sizePixel = 
							ScreenUtil.calcObjectSizeByScale(getActivity(), width, height);
					

					if (viewState.getInspectDataItem().isGodownComponent())
					{
						textWidth.setVisibility(View.VISIBLE);
						textHeight.setVisibility(View.VISIBLE);
						
						String textTop = "o:"+viewState.getInspectDataObjectSaved().getInspectDataObjectID()+",";
						  textTop+= "w:"+DataUtil.numberFormat(width)+"-"+"h:"+DataUtil.numberFormat(viewState.getInspectDataObjectSaved().getHeight());
						String textLeftSide = "d:"+DataUtil.numberFormat(height);
						textWidth.setText(textTop);
						textHeight.setText(textLeftSide);
//						textWidth.setText(DataUtil.numberFormat(width)+" .m");
//						textHeight.setText(DataUtil.numberFormat(height)+" .m");
					}else{
					   if (viewState.getInspectDataItem().isInspectObject()){
					      textWidth.setVisibility(View.VISIBLE);
					      textWidth.setText("o:"+viewState.getInspectDataObjectSaved().getInspectDataObjectID());
					   }
					}

					viewState.getInspectDataObjectSaved().setWidthObject(sizePixel.widthPixel);
					viewState.getInspectDataObjectSaved().setLongObject(sizePixel.heightPixel);
					
					viewState.setImgWidth((int)viewState.getInspectDataObjectSaved().getWidthObject());
					viewState.setImgHeight((int)viewState.getInspectDataObjectSaved().getLongObject());

				}
				else{
					
					ScreenUtil.PixelInSize pixelInSize = 
							ScreenUtil.convertPixelToRealSize(getActivity(),
									viewState.getInspectDataObjectSaved(),
									widthPixel, heightPixel);
							
					if (viewState.getInspectDataItem().isGodownComponent())
					{
						textWidth.setVisibility(View.VISIBLE);
						textHeight.setVisibility(View.VISIBLE);
						
//						textWidth.setText(DataUtil.numberFormat(pixelInSize.sizeWidth)+" .m");
//						textHeight.setText(DataUtil.numberFormat(pixelInSize.sizeLong)+" .m");

						String textTop = "w:"+DataUtil.numberFormat(pixelInSize.sizeWidth)+"-"+"h:"+DataUtil.numberFormat(viewState.getInspectDataObjectSaved().getHeight());
						String textLeftSide = "d:"+DataUtil.numberFormat(pixelInSize.sizeLong);
						textWidth.setText(textTop);
						textHeight.setText(textLeftSide);

						viewState.getInspectDataObjectSaved().setWidth(pixelInSize.sizeWidth);
						viewState.getInspectDataObjectSaved().setdLong(pixelInSize.sizeLong);
					}

					viewState.getInspectDataObjectSaved().setWidthObject((int)widthPixel);
					viewState.getInspectDataObjectSaved().setLongObject((int)heightPixel);
					viewState.getInspectDataObjectSaved().setWidth(pixelInSize.sizeWidth);
					viewState.getInspectDataObjectSaved().setdLong(pixelInSize.sizeLong);
					
					
					viewState.setImgWidth((int)viewState.getInspectDataObjectSaved().getWidthObject());
					viewState.setImgHeight((int)viewState.getInspectDataObjectSaved().getLongObject());

				}
			}
			
		}
	}
	protected void startMoveItemListener(ViewGroup vgRootLayout){

		vContainer = vgRootLayout;
		if (viewList == null)
			viewList = new ArrayList<View>();
		
		viewList.clear();

		/////////////
		for(int i = 0; i < vgRootLayout.getChildCount();i++)
		{
			View v = vgRootLayout.getChildAt(i);
			if (v.getTag() instanceof InspectItemViewState)
			{
			  
				InspectItemViewState state = (InspectItemViewState)v.getTag();
				if (state.getObjectId() == -1)
				{
					state.setObjectId(maxObjectId++);
					Log.d("DEBUG_D", "Object id = "+state.getObjectId());
				}
				InspectDataItem dataItem = state.getInspectDataItem();

				if (
						dataItem.isGodownComponent() || dataItem.isComponentBuiding()
				)
				{
					if (state.getInspectDataObjectSaved().isAuthorized())
					{
						v.setOnTouchListener(this);				
						/*
						 *setup button corner listener
						 */
						setCornerButtonOnClick(v);
					}
				}else{
					v.setOnTouchListener(this);				
					/*
					 *setup button corner listener
					 */
					setCornerButtonOnClick(v);

				}
				Log.d("DEBUG_D", "inspect data item = " +
						""+dataItem.getInspectDataItemID()+" , " +
								"name = "+dataItem.getInspectDataItemName());
				
				if (dataItem.isCameraObject())
				{
					setCameraCornerButton(v);
				}else if (dataItem.isLine())
				{
					setLineCornerButton(v);
				}
				
				double width = -1;
				double height = -1;
				//double width= v.getLayoutParams().width;
				//double height = v.getLayoutParams().height;
				
				if (state.getInspectDataObjectSaved().getWidth() > 0){
					width = state.getInspectDataObjectSaved().getWidth();
				}
				
				if (state.getInspectDataObjectSaved().getdLong() > 0)
				{
					height = state.getInspectDataObjectSaved().getdLong();
				}
				/*
				if ((width > 0)&&(height > 0)){
	                ScreenUtil.SizeInPixel sizePixel = 
	                      ScreenUtil.calcObjectSizeByScale(getActivity(), width, height);
	                state.getInspectDataObjectSaved().setWidthObject(sizePixel.widthPixel);
	                state.getInspectDataObjectSaved().setWidthObject(sizePixel.heightPixel);				   
				}*/
              
				////////////////
				/*
				ScreenUtil.SizeInPixel sizeToPixel =  ScreenUtil.convertSizeInPixel(getActivity(), 
						state.getInspectDataObjectSaved(), 
						width,
						height);
				*/
				/*
				ScreenUtil.SizeInPixel sizeToPixel = ScreenUtil.calcObjectSizeByScale(getActivity(), width, height);
				
				setResizeInspectItemSize(v,
						sizeToPixel.widthPixel,
						sizeToPixel.heightPixel,
						true);
				*/
				
				 setResizeInspectItemSize(v, 
				       width,height,
						 state.getInspectDataObjectSaved().getWidthObject(), 
						 state.getInspectDataObjectSaved().getLongObject(),
						 true);
				 
				v.requestLayout();
				v.invalidate();
				viewList.add(v);
			}
		}
	}
	protected void setVisibleConerButton(View vInspectItemView,boolean bShow)
	{
		setVisibleConerButton(vInspectItemView,bShow,false);	
	}

	protected void setVisibleConerButton(View vInspectItemView,boolean bShow,boolean hideInfo)
	{
		ImageButton btnDown = (ImageButton)vInspectItemView.findViewById(R.id.btn_down);
		ImageButton btnRight = (ImageButton)vInspectItemView.findViewById(R.id.btn_right);
		ImageButton btnDel = (ImageButton)vInspectItemView.findViewById(R.id.btn_del);
		ImageButton btnRotate = (ImageButton)vInspectItemView.findViewById(R.id.btn_rotate);

		ImageButton btnInfo = (ImageButton)vInspectItemView.findViewById(R.id.btn_info);
		ImageButton btnCopy = (ImageButton)vInspectItemView.findViewById(R.id.btn_copy);
		if (bShow)
		{
			//btnDown.setVisibility(View.VISIBLE);
			//btnRight.setVisibility(View.VISIBLE);
			btnDel.setVisibility(View.VISIBLE);
			btnRotate.setVisibility(View.VISIBLE);
			btnInfo.setVisibility(View.VISIBLE);
			btnCopy.setVisibility(View.VISIBLE);
		}else{
			btnDown.setVisibility(View.GONE);
			btnRight.setVisibility(View.GONE);
			btnDel.setVisibility(View.GONE);
			btnRotate.setVisibility(View.GONE);
			btnInfo.setVisibility(View.GONE);
			btnCopy.setVisibility(View.GONE);
		}
		
		
		/*
		if (hideInfo)
		{
			btnInfo.setVisibility(View.GONE);
		}else{
			btnInfo.setVisibility(View.VISIBLE);			
		}*/
		
		InspectItemViewState state = (InspectItemViewState)vInspectItemView.getTag();
		if (state != null){
			InspectDataItem dataItem = state.getInspectDataItem();
			if (dataItem.isCameraObject())
			{
				setCameraCornerButton(vInspectItemView);
			}else if (dataItem.isLine())
			{
				setLineCornerButton(vInspectItemView);
			}
		}
		vInspectItemView.invalidate();
		
		
		//InspectItemViewState state = (InspectItemViewState)vInspectItemView.getTag();
		if (state != null){
			InspectDataItem dataItem = state.getInspectDataItem();
			if (dataItem.isComponentBuiding()){
				if (!dataItem.isCameraObject()){
					btnInfo.setVisibility(View.GONE);				
				}
			}
		}
	}
	protected void setCornerButtonOnClick(View vInspectItemView)
	{
		ImageButton btnDown = (ImageButton)vInspectItemView.findViewById(R.id.btn_down);
		ImageButton btnRight = (ImageButton)vInspectItemView.findViewById(R.id.btn_right);
		ImageButton btnDel = (ImageButton)vInspectItemView.findViewById(R.id.btn_del);
		ImageButton btnRotate = (ImageButton)vInspectItemView.findViewById(R.id.btn_rotate);
		ImageButton btnInfo = (ImageButton)vInspectItemView.findViewById(R.id.btn_info);
		ImageButton btnCopy = (ImageButton)vInspectItemView.findViewById(R.id.btn_copy);
		
		/*
		btnDown.setVisibility(View.VISIBLE);
		btnRight.setVisibility(View.VISIBLE);
		btnDel.setVisibility(View.VISIBLE);
		btnRotate.setVisibility(View.VISIBLE);
		*/
		//btnDown.setOnTouchListener(this);
		//btnRight.setOnTouchListener(this);
		btnDel.setOnTouchListener(this);
		btnRotate.setOnTouchListener(this);
		btnInfo.setOnTouchListener(this);
		btnCopy.setOnTouchListener(this);
		InspectItemViewState state = (InspectItemViewState)vInspectItemView.getTag();
		if (state != null){
			InspectDataItem dataItem = state.getInspectDataItem();
			if (dataItem.isComponentBuiding()){
				if (!dataItem.isCameraObject()){
					btnInfo.setVisibility(View.GONE);				
				}
			}
		}
	}
	protected void setCameraCornerButton(View vInspectItemView)
	{
		ImageButton btnDown = (ImageButton)vInspectItemView.findViewById(R.id.btn_down);
		ImageButton btnRight = (ImageButton)vInspectItemView.findViewById(R.id.btn_right);
//		ImageButton btnDel = (ImageButton)vInspectItemView.findViewById(R.id.btn_del);
//		ImageButton btnRotate = (ImageButton)vInspectItemView.findViewById(R.id.btn_rotate);
		
//		TextView textWidth = (TextView)vInspectItemView.findViewById(R.id.inspect_view_body_width);
//		TextView textHeight = (TextView)vInspectItemView.findViewById(R.id.inspect_view_body_height);
		
		btnDown.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		
//		textWidth.setVisibility(View.GONE);
//		textHeight.setVisibility(View.GONE);
	}
	protected void setLineCornerButton(View vInspectItemView)
	{
		ImageButton btnDown = (ImageButton)vInspectItemView.findViewById(R.id.btn_down);
//		ImageButton btnRight = (ImageButton)vInspectItemView.findViewById(R.id.btn_right);
//		ImageButton btnDel = (ImageButton)vInspectItemView.findViewById(R.id.btn_del);
//		ImageButton btnRotate = (ImageButton)vInspectItemView.findViewById(R.id.btn_rotate);
//		TextView textWidth = (TextView)vInspectItemView.findViewById(R.id.inspect_view_body_width);
//		TextView textHeight = (TextView)vInspectItemView.findViewById(R.id.inspect_view_body_height);

		btnDown.setVisibility(View.GONE);
//		textHeight.setVisibility(View.GONE);
		
	}
	protected void resizeBorderView(View view,int newWidth,int newHeight)
	{
		try{
			Log.d("DEBUG_D", "call resizeBorderView - > newWidth = "+newWidth+" , newHeight = "+newHeight);
			view.getLayoutParams().width = newWidth;
			view.getLayoutParams().height = newHeight;
			
			ImageView imgView = (ImageView)view.findViewById(R.id.inspect_img);
			RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams)imgView.getLayoutParams();
			
			int imgWidth = imgView.getDrawable().getIntrinsicWidth();
			int imgHeight = imgView.getDrawable().getIntrinsicHeight();
			
			rl.leftMargin = (int)(((double)newWidth/2)-((double)imgWidth/2));
			rl.topMargin = (int)(((double)newHeight/2)-((double)imgHeight/2));

			imgView.setLayoutParams(rl);
			
			view.invalidate();
			view.requestLayout();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	protected void resizeVectorImgView_out(ImageView imgView,
									   int newWidth,
									   int newHeight)	
	throws Exception
	{
		/*
		 PictureDrawable drawable = (PictureDrawable)imgView.getDrawable();
		 Bitmap bmpNew = Bitmap.createBitmap((newWidth),
				 (newHeight),
				 Bitmap.Config.ARGB_4444);
		 
		 
		 Canvas canvas = new Canvas(bmpNew);

		 Picture resizePicture = new Picture();

		 canvas = resizePicture.beginRecording(newWidth, newHeight);

		 canvas.drawPicture(drawable.getPicture(), new Rect(0,0,newWidth, newHeight));

		 resizePicture.endRecording();

		 Drawable vectorDrawing = new PictureDrawable(resizePicture);
		 
		 imgView.setImageDrawable(vectorDrawing);
		 imgView.setScaleType(ScaleType.CENTER);*/
			
		 //ImageUtil.resizeVectorImgView(imgView, newWidth, newHeight);/*not resized image*/
	}
	public void unselectAll(){
		for(View vInspectObj : viewList)
        {
        	setSelected(vInspectObj,true,true);
        }
	}
	private int getQuadrant(int width,int height, double xTouch, double yTouch) {
	    double x = xTouch - (width / 2d);
	    double y = width - yTouch - (height / 2d);
	    return getQuadrant(x,y);
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
	public double getAngle2(double xTouch,double yTouch)
    {
        double dx = xTouch - centerXOfImageOnScreen;
        double dy = -(yTouch - centerYOfImageOnScreen);

        double inRads = Math.atan2(dy,dx);

        // We need to map to coord system when 0 degree is at 3 O'clock, 270 at 12 O'clock
        if (inRads < 0)
            inRads = Math.abs(inRads);
        else
            inRads = 2*Math.PI - inRads;

        return Math.toDegrees(inRads);

    }
	private double calAngle(double x1,double y1,double x2,double y2)
	{
		double angle = Math.atan((x2-x1)/(y2-y1))/(Math.PI/180);
		if (angle > 0)
		{
			if (y2 < y1)
				return angle;
			else
				return 180 + angle;
		}else{
			if (x1 < x2)
				return 180+angle;
			else
				return 360 + angle;
		}
	}
	private void animate(View view,double fromDegrees, double toDegrees, long durationMillis) {
	    final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
	            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
	            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
	    rotate.setDuration(durationMillis);
	    rotate.setFillEnabled(true);
	    rotate.setFillAfter(true);
//	    mCircle.startAnimation(rotate);
	    view.startAnimation(rotate);
	}
	public OnClickInspectDrawingView getOnClickInspectDrawingView() {
		return onClickInspectDrawingView;
	}

	public void setOnClickInspectDrawingView(OnClickInspectDrawingView onClickInspectDrawingView) {
		this.onClickInspectDrawingView = onClickInspectDrawingView;
	}

	public OnResizedInspectItemDrawingView getOnResizedInspectItemDrawingView() {
		return onResizedInspectItemDrawingView;
	}

	public void setOnResizedInspectItemDrawingView(
			OnResizedInspectItemDrawingView onResizedInspectItemDrawingView) {
		this.onResizedInspectItemDrawingView = onResizedInspectItemDrawingView;
	}

	public double getBaseWidth() {
		return baseWidth;
	}

	public void setBaseWidth(double baseWidth) {
		this.baseWidth = baseWidth;
	}

	public double getBaseHeight() {
		return baseHeight;
	}

	public void setBaseHeight(double baseHeight) {
		this.baseHeight = baseHeight;
	}
	public ArrayList<View> getObjectViewList(){
		return viewList;
	}
	protected synchronized void setLayoutModified(Task currentTask,boolean bReset)
	{
       SharedPreferenceUtil.saveLayoutModified(getActivity(), true);
       if (bReset){
          ReportInspectSummaryStatusHelper.resetReportStatus(getActivity(), currentTask);
       }
   }
	protected synchronized void setLayoutModified(Task currentTask){
	   setLayoutModified(currentTask,true);
	}
}
