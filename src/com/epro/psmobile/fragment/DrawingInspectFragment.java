/**
 * 
 */
package com.epro.psmobile.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.HtmlPreviewActivity;
import com.epro.psmobile.InspectPhotoEntryActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.action.OnChangedInspectDataEntryPanel;
import com.epro.psmobile.action.OnClickInspectDataItem;
import com.epro.psmobile.action.OnClickInspectDrawingView;
import com.epro.psmobile.action.OnResizedInspectItemDrawingView;
import com.epro.psmobile.adapter.InspectItemListAdapter;
import com.epro.psmobile.adapter.InspectItemListAdapter.InspectItemToolboxType;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataGroupType;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectDataSVGResult;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.data.TransactionStmtHolder;
import com.epro.psmobile.dialog.InspectDataEntryDialog;
import com.epro.psmobile.dialog.InspectDataEntryDialog.InspectDataEntryType;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.location.PSMobileLocationManager;
import com.epro.psmobile.location.PSMobileLocationManager.OnPSMLoctaionListener;
import com.epro.psmobile.util.ActivityUtil;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.HTMLInspectPreviewUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.ReportInspectSummaryStatusHelper;
import com.epro.psmobile.util.SerializationUtils;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.ImageUtil;
import com.epro.psmobile.util.ImageUtil.InspectSaveFilter;
import com.epro.psmobile.util.ScreenUtil;
import com.epro.psmobile.view.InspecItemViewCreator;
import com.epro.psmobile.view.InspectItemViewState;
import com.epro.psmobile.view.InspectRelativeLayout;
import com.epro.psmobile.view.PanAndZoomListener;
import com.epro.psmobile.view.PanAndZoomListener.Anchor;
import com.larvalabs.svgandroid.SVGParseException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
/**
 * @author thrm0006
 *
 */
public class DrawingInspectFragment extends DrawingViewBaseFragment 
implements  OnItemClickListener, 
			OnClickInspectDataItem, 
			OnClickInspectDrawingView, 
			OnResizedInspectItemDrawingView, OnChangedInspectDataEntryPanel, OnClickListener, OnPSMLoctaionListener {

	private View currentView;
	private View layoutContainer;
	
	private float mx, my;
    private float curX, curY;

    private int lastObjectId = -1;
    
	private ArrayList<InspectDataGroupType> groupTypes = null;
	private InspectItemListAdapter adapter = null;

	//private ArrayList<Bitmap> bmpList = new ArrayList<Bitmap>();
	private ListView lvGroupMenus;
	
	//private int previousMenuIdx;
	//private boolean isShownFirstInspectPanel;
	
	private JobRequest currentJobRequest;
	private CustomerSurveySite customerSurveySite;
	private Task currentTask;
	private InspectDataEntryDialog inspectDataEntryDialog;
	private InspectItemViewState currentItemState;
	private View vInspectToolBoxPanel;
	//private ScaleGestureDetector mScaleDetector;
	private final static int WIDTH_INSPECT_PANEL = 241;
	
	private PSMobileLocationManager locManager;
	private Location currentLocation;
	
	private ScrollView vScroll;
    private HorizontalScrollView hScroll;

    private View vcontrols_full_screen;
    private SeekBar seekBarScale;
    private Button btnRestore;
    public boolean isShowFullScreen = false;
    public boolean isUniversalLayout = false;
    
    private int hX = 0;
    private int hY = 0;
    private int vX = 0;
    private int vY = 0;
	/**
	 * 
	 */
    /*
	private class OnPinchListener extends SimpleOnScaleGestureListener {

	    float startingSpan; 
	    float endSpan;
	    float startFocusX;
	    float startFocusY;


	    public boolean onScaleBegin(ScaleGestureDetector detector) {
	        startingSpan = detector.getCurrentSpan();
	        startFocusX = detector.getFocusX();
	        startFocusY = detector.getFocusY();
	        return true;
	    }


	    public boolean onScale(ScaleGestureDetector detector) {
	    	if (layoutContainer instanceof InspectRelativeLayout){
		        ((InspectRelativeLayout)layoutContainer).scale(detector.getCurrentSpan()/startingSpan, startFocusX, startFocusY);	    		
	    	}
	        return true;
	    }

	    public void onScaleEnd(ScaleGestureDetector detector) {
	    	if (layoutContainer instanceof InspectRelativeLayout){
	    		((InspectRelativeLayout)layoutContainer).restore();
	    	}
	    }
	}*/
	public DrawingInspectFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		
		setHasOptionsMenu(true);
		
		super.onActivityCreated(arg0);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		super.onCreateView(arg0, arg1, arg2);
		
		currentView = arg0.inflate(R.layout.ps_activity_draw_layout, arg1, false);
		
		vScroll = (ScrollView) currentView.findViewById(R.id.v_scroll_draw_layout);
        hScroll = (HorizontalScrollView) currentView.findViewById(R.id.h_scroll_draw_layout);

        super.setExternalScroll(vScroll, hScroll);
   

		layoutContainer = currentView.findViewById(R.id.layout_container);
		
		if (layoutContainer instanceof InspectRelativeLayout)
		{
		   super.viewObjContainer = (ViewGroup)layoutContainer;
		   
			/*
			OnPinchListener onPichListener = new OnPinchListener();
		    mScaleDetector = new ScaleGestureDetector(getActivity(), onPichListener);
		    layoutContainer.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return mScaleDetector.onTouchEvent(arg1);
				}
		    	
		    });*/
			
			/*
			FrameLayout view = new FrameLayout(this.getActivity());

			FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 
					FrameLayout.LayoutParams.MATCH_PARENT, 
					Gravity.TOP | Gravity.LEFT);

			view.addView(layoutContainer,fp);

			view.setOnTouchListener(
					new PanAndZoomListener(view,
							layoutContainer,
							Anchor.TOPLEFT));
			*/
		
			
		}
		/*
		currentView.setOnTouchListener(
				new PanAndZoomListener((RelativeLayout)currentView,
					layoutContainer,
					Anchor.CENTER));*/
		/*
		ViewGroup vG = (ViewGroup)currentView.findViewById(R.id.root_layout);
		PictureDrawable pictureDrawable =  ImageUtil.createPictureDrawable(getActivity(), "sample_layout.svg");
		vG.setBackgroundDrawable(pictureDrawable);
		*/
		
		initialView(currentView);
		initialInspectPanel(currentView);
		
		
		this.restoreAllInspectDrawingObjects();
		
		
		this.setOnClickInspectDrawingView(this);
		this.setOnResizedInspectItemDrawingView(this);
		
		/*
		FrameLayout view = null;
		
		view = new FrameLayout(this.getActivity());

		FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 
				FrameLayout.LayoutParams.MATCH_PARENT, 
				Gravity.TOP | Gravity.LEFT);

		view.addView(currentView,fp);

		view.setOnTouchListener(
				new PanAndZoomListener(view,
						currentView,
						Anchor.TOPLEFT));
		*/
		return currentView;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (locManager != null)
		{
			locManager.stopRequestLocationUpdated();
			locManager = null;
			System.gc();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locManager = new PSMobileLocationManager(this.getActivity());
		try {
			locManager.startPSMLocationListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void initialView(View currentView)
	{

	   //if (currentView == null)return;
       
       vcontrols_full_screen = currentView.findViewById(R.id.controls_full_screen);
       initialControlsFullScreen(vcontrols_full_screen);
   
		/*
		 Bundle bundle = new Bundle();
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, menuItem.customerSurveySite);
					drawingFragment.setArguments(bundle);

		 */
		Bundle bArgument =  this.getArguments();
		if (bArgument != null)
		{
			this.currentJobRequest = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
			this.customerSurveySite = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);			
			this.currentTask = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
			this.isShowFullScreen = bArgument.getBoolean(InstanceStateKey.KEY_ARGUMENT_SCREEN_STATE, false);
			this.isUniversalLayout = bArgument.getBoolean(InstanceStateKey.KEY_ARGUMENT_IS_UNIVERSAL_LAYOUT,false);
			
			/*
			 * remove intent
			 **/
			
			if (this.isShowFullScreen)
			{
               showNormalScren();			  
			}else{
			   showFullScreen();
			}
			
			
			if (seekBarScale == null)
		       seekBarScale = (SeekBar)vcontrols_full_screen.findViewById(R.id.seek_scale);
		           
		    if (btnRestore == null)
		       btnRestore = (Button)vcontrols_full_screen.findViewById(R.id.btn_restore);
		           
		    
		     btnRestore.setOnClickListener(new OnClickListener(){

		            @Override
		            public void onClick(View v) {
		               // TODO Auto-generated method stub
		               if (seekBarScale != null){
		                  seekBarScale.setProgress(seekBarScale.getMax()-50);
		               }
		            }
		              
		           });
		           //seekBarScale.setProgress(seekBarScale.getProgress()-50);

		           
		           seekBarScale.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

		            @Override
		            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		               // TODO Auto-generated method stub
		               if (layoutContainer instanceof InspectRelativeLayout)
		               {
		                  progress -= 50;
		                  
		                  InspectRelativeLayout layout = (InspectRelativeLayout)layoutContainer;
		                  if (progress == 0){
		                     layout.restore();
		                  }else{
		                     layout.setScaleFactor(InspectRelativeLayout.DEFAULT_FACTOR+(progress * 0.02f));                     
		                  }
		                  layout.requestLayout();
		                  layout.invalidate();
		               }
		            }

		            @Override
		            public void onStartTrackingTouch(SeekBar seekBar) {
		               // TODO Auto-generated method stub
		               
		            }

		            @Override
		            public void onStopTrackingTouch(SeekBar seekBar) {
		               // TODO Auto-generated method stub
		               
		            }
		              
		           });
		     ////////////////      
			/////////////////////
			/*
			 * setup background
			 */
			if (this.customerSurveySite != null)
			{
				String lastSVGFile = this.customerSurveySite.getLayoutSVGPath();
				if (!lastSVGFile.isEmpty())
				{
					String fileImg = SharedPreferenceUtil.getDownloadFolder(getActivity());
					//fileImg += CommonValues.DOWNLOAD_FOLDER_IMGS;
					fileImg += "/"+lastSVGFile;
					/*
					 * show image
					 */
					Bitmap bmpLastLayout = null;
					if (fileImg.endsWith(".svg"))
					{
						try {
							PictureDrawable drawable = ImageUtil.createPictureDrawableFromSVGFile(fileImg);
							int widthScreen = ScreenUtil.getDeviceScreen(getActivity()).width;
							int heightScreen = ScreenUtil.getDeviceScreen(getActivity()).height;
							Bitmap bmp = ImageUtil.resizeVectorImg(drawable, widthScreen, heightScreen);
//							((com.epro.psmobile.view.InspectRelativeLayout)currentView).setBackgroundDrawable(new BitmapDrawable(this.getResources(),bmpLastLayout));
							((com.epro.psmobile.view.InspectRelativeLayout)layoutContainer).setBmpBackground(bmp);
							((com.epro.psmobile.view.InspectRelativeLayout)layoutContainer).invalidate();
						} catch (SVGParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						 Bitmap bmp = BitmapFactory.decodeFile(fileImg);
						 bmpLastLayout = ImageUtil.getResizedBitmapScale(bmp, 1024, 800);
						 bmp.recycle();
						((com.epro.psmobile.view.InspectRelativeLayout)currentView).setBackgroundDrawable(new BitmapDrawable(this.getResources(),bmpLastLayout));
					}
				}
			}
			///////////////////
		}
		
		if (layoutContainer != null)
			layoutContainer.setOnTouchListener(this);
		
		final View vgInspectPanel = currentView.findViewById(R.id.view_inspect_item_panel);
		if (vgInspectPanel != null)
		{
			vgInspectPanel.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){

				@Override
				public void onGlobalLayout() {
					// TODO Auto-generated method stub
					LayoutParams layoutParam = vgInspectPanel.getLayoutParams();
					if (layoutParam instanceof RelativeLayout.LayoutParams)
					{
						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layoutParam;
						params.topMargin = 0;
						vgInspectPanel.setLayoutParams(layoutParam);	
						vgInspectPanel.requestLayout();
				    }
				}
				
			});
		}
		
		Button btnClose = (Button)currentView.findViewById(R.id.btn_inspect_panel_close);
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				vInspectToolBoxPanel  = getActivity().findViewById(R.id.view_inspect_item_panel);
				if (vInspectToolBoxPanel.getVisibility() == View.VISIBLE){
					vInspectToolBoxPanel.setVisibility(View.GONE);
				}else{
					vInspectToolBoxPanel.setVisibility(View.VISIBLE);
					showInspectItemPanel(vInspectToolBoxPanel);
				}
			}
			
		});
		
	}
	private void enableTitleAndBackkey(boolean bShown){
		
		final View vShowTitle = currentView.findViewById(R.id.rl_show_inspect_group_name);			
		ImageButton btnBack = (ImageButton)currentView.findViewById(R.id.btn_inspect_group_back);

		if (bShown){
			vShowTitle.setVisibility(View.VISIBLE);
			btnBack.setVisibility(View.VISIBLE);
		}
		else
		{
			vShowTitle.setVisibility(View.INVISIBLE);
			btnBack.setVisibility(View.INVISIBLE);
		}
	}
	private void initialInspectPanel(View rootFragmentView)
	{
		enableTitleAndBackkey(false);
		
		PSBODataAdapter adapter = PSBODataAdapter.getDataAdapter(getActivity());
		try {
			groupTypes =  adapter.getAllInspectDataGroupType(currentJobRequest.getInspectType().getInspectTypeID());
			/*
			 * sorting
			 */
			
			if (groupTypes != null)
			{
				
				Locale locale = new Locale("th","TH");
				final Collator collator = java.text.Collator.getInstance(locale);
				Collections.sort(groupTypes, new Comparator<InspectDataGroupType>(){

					@Override
					public int compare(InspectDataGroupType lhs,
							InspectDataGroupType rhs) {
						// TODO Auto-generated method stub
						return collator.compare(lhs.getInspectDataGroupTypeName(), rhs.getInspectDataGroupTypeName());
					}
				});

				for(InspectDataGroupType groupType : groupTypes)
				{
					ArrayList<InspectDataItem> inspectDataItemList =  adapter.findInspectDataItemsByGroupId(
					      groupType.getInspectDataGroupTypeID(),
					      currentJobRequest.getInspectType().getInspectTypeID());
					
					if (inspectDataItemList != null)
					{
	                    Collections.sort(inspectDataItemList, new Comparator<InspectDataItem>(){

	                        @Override
	                        public int compare(InspectDataItem lhs,
	                                InspectDataItem rhs) {
	                            // TODO Auto-generated method stub
	                            return collator.compare(lhs.getInspectDataItemName() ,
	                                    rhs.getInspectDataItemName());
	                        }
	                    });
	                    groupType.addInspectDataItemList(inspectDataItemList);
					   
					}
				}
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		lvGroupMenus = (ListView)rootFragmentView.findViewById(R.id.lv_inspect_group_menu);
		
		if (groupTypes != null)
		{
			String[] group_menu = new String[groupTypes.size()];
			for(int i = 0; i < groupTypes.size();i++)
			{
				group_menu[i] = groupTypes.get(i).getInspectDataGroupTypeName();
			}
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(), 
					R.layout.simple_list_item,group_menu);

			lvGroupMenus.setAdapter(arrayAdapter);				
			lvGroupMenus.setOnItemClickListener(this);
		}		
	}
	@Override
	public void onInspectItemClicked(InspectDataGroupType currentGroupType,
			InspectDataItem currentItem) {
		// TODO Auto-generated method stub
		
		InspecItemViewCreator view = new InspecItemViewCreator(this.getActivity(),0);	
		View vItem = view.createInspectItemView(currentItem);
		
		if (layoutContainer != null)
		{
		    int objIdMax = 0;
		    ViewGroup vg = (ViewGroup)layoutContainer;
		    for(int i = 0; i < vg.getChildCount();i++){
		       View vEach = vg.getChildAt(i);
		       if (vEach.getTag() instanceof InspectItemViewState){
		          InspectItemViewState state = (InspectItemViewState)vEach.getTag();
		          if (state.getObjectId() > objIdMax){
		             objIdMax = state.getObjectId();
		          }
		       }
		    }
		    ((InspectItemViewState)vItem.getTag()).setObjectId(objIdMax+1);
		    
			if (layoutContainer instanceof ViewGroup)
			{
				((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
				.setAddObjectFlag("Y");

				if (this.currentJobRequest != null){
					((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
					.setJobRequestID(this.currentJobRequest.getJobRequestID());
				}
				if (this.customerSurveySite != null)
				{
					((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
					.setCustomerSurveySiteID(this.customerSurveySite.getCustomerSurveySiteID());
					
					((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
					.setSiteAddressKey(this.customerSurveySite.getSiteAddressKey());
					
					((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
					.setTeamIDObjectOwner(SharedPreferenceUtil.getTeamID(getActivity()));
				}
				if (this.currentTask != null)
				{
					((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
					.setTaskCode(this.currentTask.getTaskCode());
				}
				//resizeBorderView(vItem,vItem.getLayoutParams().width,vItem.getLayoutParams().height);
				super.setSelected(vItem, true,false);

				
				int X = 0;
				int Y = 0;
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vItem.getLayoutParams();
				
				if ((hScroll != null)&&(vScroll != null)){
					//int maxWidth = (int)this.getActivity().getResources().getDimension(R.dimen.draw_layout_width);
					//int maxDeep = (int)this.getActivity().getResources().getDimension(R.dimen.draw_layout_deep);
					
					//int vscrollX = vScroll.getScrollX();
					int vscrollY = vScroll.getScrollY();
					
					int hscrollX = hScroll.getScrollX();
					//int hscrollY = hScroll.getScrollY();
					
					DisplayMetrics dm = new DisplayMetrics();
					getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
					int w = dm.widthPixels; // etc...
					int h = dm.heightPixels;
					
					X = (int)(((double)w/2)+hscrollX);
					Y = (int)(((double)h/2)+vscrollY);
					X = X - (int)((double)layoutParams.width/2);
					Y = Y - (int)((double)layoutParams.height/2);
				}
				layoutParams.leftMargin = X;
				layoutParams.topMargin = Y;
				vItem.setLayoutParams(layoutParams);
				((ViewGroup)layoutContainer).addView(vItem);
				vItem.invalidate();

	            
				startMoveItemListener(((ViewGroup)layoutContainer));
				
				if (vInspectToolBoxPanel == null)
				{
					vInspectToolBoxPanel  = getActivity().findViewById(R.id.view_inspect_item_panel);	
				}
				vInspectToolBoxPanel.invalidate();

				/*
				 * save to database
				 */
				try 
				{
				    super.setLayoutModified(currentTask);
                    getDataAdapter().updateInspectDataObjectSaved(
                           ((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
                           );
				}
				catch (Exception e) {
				   // TODO Auto-generated catch block
                  e.printStackTrace();
                  MessageBox.showMessage(getActivity(), R.string.message_box_title_error, e.getMessage());
               }
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		//mScaleDetector.onTouchEvent(event);//zoom
//		return super.onTouch(v, event);
		

		if ((vScroll != null)&&(hScroll != null)){
			if (v instanceof InspectRelativeLayout)
			{
				float curX, curY;

		        switch (event.getAction()) {

		            case MotionEvent.ACTION_DOWN:
		                mx = event.getX();
		                my = event.getY();
		                super.onTouch(v, event);
		                break;
		            case MotionEvent.ACTION_MOVE:
		                curX = event.getX();
		                curY = event.getY();
		                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
		                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
		                
		                this.vX = (int)(mx - curX);
		                this.vY = (int)(my - curY);
		                this.hX = (int)(mx - curX);
		                this.hY = (int)(my - curY);
		                
		                mx = curX;
		                my = curY;
		                break;
		            case MotionEvent.ACTION_UP:
		                curX = event.getX();
		                curY = event.getY();
		                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
		                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
		                break;
		        }

		        
		        if (this.vInspectToolBoxPanel != null){
					if (this.vInspectToolBoxPanel.isShown()){
						this.vInspectToolBoxPanel.invalidate();
						this.vInspectToolBoxPanel.requestLayout();

					}
				}

		        return true;

		        
			}else{
				vScroll.requestDisallowInterceptTouchEvent(true);
				hScroll.requestDisallowInterceptTouchEvent(true);
				

		        
		        if (this.vInspectToolBoxPanel != null){
					if (this.vInspectToolBoxPanel.isShown()){
						this.vInspectToolBoxPanel.invalidate();
						this.vInspectToolBoxPanel.requestLayout();
					}
				}

		        /*
		         * 
		         */
		        View vTouched = v;
		        
		        if(v.getTag() instanceof InspectItemViewState)
		        {
			        switch (event.getAction() & MotionEvent.ACTION_MASK) 
			        {
			        	case MotionEvent.ACTION_DOWN:{
		            	
			        		ArrayList<View> viewList = super.getObjectViewList();
			        		ArrayList<View> viewSelectedList = new ArrayList<View>();
			        		
			        		if (viewList != null)
			        		{
			        			float touchX = event.getRawX();
			        			float touchY = event.getRawY();
			        			for(View viewChild : viewList)
			        			{
			        				if (isPointInsideView(touchX,touchY,viewChild))
			        				{
			        					viewSelectedList.add(viewChild);
			        				}
			        			}
			        			//try to select from z-order
			        			for(View viewChildMatched : viewSelectedList)
			        			{
		        					InspectItemViewState state = (InspectItemViewState)viewChildMatched.getTag();
		        					lastObjectId = state.getInspectDataObjectSaved().getInspectDataObjectID();		        							

		        					if (!state.getInspectDataItem().isGodownComponent())
		        					{
		        						if (lastObjectId != state.getInspectDataObjectSaved().getInspectDataObjectID())
		        						{
		        							lastObjectId = state.getInspectDataObjectSaved().getInspectDataObjectID();		        							

		        							vTouched = viewChildMatched;
		        							viewChildMatched.bringToFront();
		        						}
		        						break;
		        					}/*
		        					else if (state.getInspectDataItem().isGodownComponent()){
		        						if (state.getInspectDataObjectSaved().isAuthorized()){
		        							return false;
		        						}
		        					}*/
			        			}
			        		}
			        	}
			        	break;
			        }
		        }
		        /*
				InspectItemViewState state = (InspectItemViewState)viewChild.getTag();
				if (!state.getInspectDataItem().isGodownComponent())
				{
					if (lastObjectId != state.getInspectDataObjectSaved().getInspectDataObjectID())
					{
						lastObjectId = state.getInspectDataObjectSaved().getInspectDataObjectID();		        							
						vTouched = viewChild;
						vTouched.bringToFront();
					}
					break;		        				
				}*/
		        return super.onTouch(vTouched,event);
			}
		}else
		{

	        
	        if (this.vInspectToolBoxPanel != null){
				if (this.vInspectToolBoxPanel.isShown()){
					this.vInspectToolBoxPanel.invalidate();
					this.vInspectToolBoxPanel.requestLayout();

				}
			}

	        /*
	         * 
	         */
	        View vTouched = v;

	        /*
	        View vTouched = v;
	        ArrayList<View> viewList = super.getObjectViewList();
	        if (viewList != null)
	        {
	        	float touchX = event.getRawX();
	        	float touchY = event.getRawY();
	        	for(View viewChild : viewList)
	        	{
	        		if (isPointInsideView(touchX,touchY,viewChild))
	        		{
	        			vTouched = viewChild;
	        			vTouched.bringToFront();
	        			break;
	        		}
	        	}
	        }*/
			return super.onTouch(vTouched,event);

		}
			
	}
	/**
	 * Determines if given points are inside view
	 * @param x - x coordinate of point
	 * @param y - y coordinate of point
	 * @param view - view object to compare
	 * @return true if the points are within view bounds, false otherwise
	 */
	private boolean isPointInsideView(float x, float y, View view){
	    int location[] = new int[2];
	    view.getLocationOnScreen(location);
	    int viewX = location[0];
	    int viewY = location[1];

	    //point is inside view bounds
	    if(( x > viewX && x < (viewX + view.getWidth())) &&
	            ( y > viewY && y < (viewY + view.getHeight()))){
	        return true;
	    } else {
	        return false;
	    }
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
		// TODO Auto-generated method stub
		if (groupTypes != null)
		{
			InspectDataGroupType groupType =  groupTypes.get(position);
			if ((groupType.getInspectDataItemList() == null)||(groupType.getInspectDataItemList().size() == 0))
			   return;
			
			adapter = new InspectItemListAdapter(this.getActivity(),InspectItemToolboxType.INSPECT_ITEM);
			adapter.setGroupType(groupType);
			adapter.setClickInspectItemListener(this);
			adapter.setOnChangedInspectDataEntryPanel(null);
			
			
			ListView lvGroupMenus = (ListView)currentView.findViewById(R.id.lv_inspect_group_menu);
			lvGroupMenus.setAdapter(adapter);		
			lvGroupMenus.setOnItemClickListener(null);
			
			
			final View vShowTitle = currentView.findViewById(R.id.rl_show_inspect_group_name);			
			vShowTitle.setVisibility(View.VISIBLE);
			
			View vTitleGroup = vShowTitle.findViewById(R.id.tv_show_group_name);
			if (vTitleGroup != null)
			{
				((TextView)vTitleGroup).setText(groupType.getInspectDataGroupTypeName());
			}
			
			ImageButton btnBack = (ImageButton)currentView.findViewById(R.id.btn_inspect_group_back);
			if (btnBack != null)
			{
				btnBack.setVisibility(View.VISIBLE);
				btnBack.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						vShowTitle.setVisibility(View.GONE);
						initialInspectPanel(currentView);
					}
					
				});
			}
		}
	}

	@Override
	public void onClickInspectItemDrawing(View v,InspectItemViewState dataItem) {
		// TODO Auto-generated method stub
		Log.d("DEBUG_D", "onClickInspectItemDrawing");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
        setLayoutModified(currentTask);

		switch (requestCode) {
			case InstanceStateKey.RESULT_INSPECT_PHOTO_ENTRY:{
				if (resultCode == Activity.RESULT_OK)
				{
					int photoId = data.getIntExtra(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID, -1);
					//InspectDataSavedSpinnerDisplay spinnerDisplay = data.getParcelableExtra(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_SELECTED_PHOTO);
					if (currentItemState != null)
					{
						currentItemState.getInspectDataObjectSaved().setPhotoID(photoId);
						/*
						 * update
						 */
						try {
						      super.getDataAdapter().updateInspectDataObjectSaved(
						            currentItemState.getInspectDataObjectSaved());
						}
						catch (Exception e) {
                           // TODO Auto-generated catch block
						   e.printStackTrace();
						   MessageBox.showMessage(getActivity(),
						         R.string.message_box_title_error,
						         e.getMessage());						   
                        }
					}
				}
			}
			break;
			/*
			case InstanceStateKey.RESULT_LOAD_IMAGE_FROM_GALLERY:{
				if (resultCode == Activity.RESULT_OK)
				{
					Uri selectedImage = data.getData();
		            String[] filePathColumn = { MediaStore.Images.Media.DATA };
		 
		            Cursor cursor = null;
		            
		            try{
		            	cursor = this.getActivity().getContentResolver().query(selectedImage,
		                    filePathColumn, null, null, null);
		            	cursor.moveToFirst();
		 
		            	int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            	String picturePath = cursor.getString(columnIndex);
		            	
		            	Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
		            	
		            	if (bitmap != null)
		            	{
		            		//bmpList.add(bitmap);
		            		Date d = new Date();		            		
		            		ImageUtil.saveImageFile(getActivity(),d.getTime()+".jpg" , bitmap);
		            		
			            	adapter.reloadPictureGridPreview(bitmap);
			            	 if (lvGroupMenus != null){
			            		 lvGroupMenus.invalidateViews();
				            }
		            	}
		            	
		            }catch(Exception ex){}
		            finally{
		            	if (cursor != null)
		            		cursor.close();
		            }
				}
			}
			break;
			case InstanceStateKey.TAKE_PICTURE:
			{
				if (resultCode == Activity.RESULT_OK) {
					if (adapter != null)
					{
						Uri selectedImage = adapter.getImageUri();
						getActivity().getContentResolver().notifyChange(selectedImage, null);
			            
						ContentResolver cr = getActivity().getContentResolver();
				        Bitmap bitmap = null;
				        try {
				                 bitmap = android.provider.MediaStore.Images.Media
				                 .getBitmap(cr, selectedImage);

				             if (bitmap != null)
				             {
//				            	 bmpList.add(bitmap);
				            	 Date d = new Date();		            		
				            	 ImageUtil.saveImageFile(getActivity(),d.getTime()+".jpg" , bitmap);

				            	 
				            	 adapter.reloadPictureGridPreview(bitmap);
				            	 if (lvGroupMenus != null){
				            		 lvGroupMenus.invalidateViews();
					            }
				             }
				        } catch (Exception e) {
				                Log.e("Camera", e.toString());
				        }
					}
				}
			}break;	       */
		}
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater,InspectOptMenuType.INSPECT_LOCATION);	
		
		if (isUniversalLayout){
		   menu.findItem(R.id.menu_switch_to_list).setVisible(true);
		}
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		    case R.id.menu_switch_to_list:{
		       if (SharedPreferenceUtil.getLayoutModified(getSherlockActivity()))
		       {
		            MessageBox.showMessage(getSherlockActivity(), 
		                    R.string.text_error_title, 
		                    R.string.text_error_layout_not_saved_yet);
		        }else{
		           if (getSherlockActivity().getSupportFragmentManager().getBackStackEntryCount() > 1)
		           {
		              getSherlockActivity().getSupportFragmentManager().popBackStack();
		              
		           }
		        }
		    }break;
			case R.id.menu_drawing_edit:{
				/*
				View v  = currentView.findViewById(R.id.view_inspect_item_panel);
				if (v.getVisibility() == View.VISIBLE){
					v.setVisibility(View.GONE);
				}else{
					v.setVisibility(View.VISIBLE);
					showInspectItemPanel(v);
				}*/
				showInspectToolbox();
			}break;
			case R.id.menu_drawing_full_screen:{
				//int widthViewFragment = 0;				
			   toggleScreen();
				if (vInspectToolBoxPanel == null)
				{
					vInspectToolBoxPanel  = getActivity().findViewById(R.id.view_inspect_item_panel);	
				}
				showInspectItemPanel(this.vInspectToolBoxPanel);
			}break;
			case R.id.menu_drawing_save:{

			   
				new DoAsyncTaskSaveInspect().execute();
			}break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void showInspectItemPanel(View v)
	{
		showInspectItemPanel(v,0);
	}
	private void showInspectItemPanel(View v,
			int widthListFragment)
	{
		if (this.currentView != null)
		{
			Fragment f = this.getFragmentManager().findFragmentById(R.id.menu_group_frag);
			widthListFragment = f.getView().getWidth();
			
			int width = this.currentView.getWidth();
			
			int panelWidth = (v.getWidth() == 0)?WIDTH_INSPECT_PANEL:v.getWidth();
			
			int x = width - (panelWidth);
			int y = this.getActivity().getActionBar().getHeight();
			
			ViewGroup.LayoutParams layoutParam =  v.getLayoutParams();
			if (layoutParam instanceof RelativeLayout.LayoutParams){
				RelativeLayout.LayoutParams r_layout = (RelativeLayout.LayoutParams)layoutParam;
				r_layout.leftMargin = x-3;
				r_layout.topMargin = y;
				v.setLayoutParams(r_layout);
				v.invalidate();
			}
		}
	}

	@Override
	public void onInspecItemDrawingViewResized(InspectItemViewState item,
			double width, 
			double height) {
		// TODO Auto-generated method stub
		
		item.getInspectDataObjectSaved().setWidth(width/this.getBaseWidth());
		item.getInspectDataObjectSaved().setdLong(height/this.getBaseHeight());

		/*
		if (lvGroupMenus != null)
		{
			InspectItemListAdapter listAdapter = (InspectItemListAdapter)lvGroupMenus.getAdapter();
			if (listAdapter.getCurrentToolboxType() == InspectItemListAdapter.InspectItemToolboxType.INSPECT_DATA_ENTRY)
			{
				listAdapter.setEntryField(InspectItemListAdapter.ENTRY_FIELD_WIDTH, (width/this.getBaseWidth())+"");
				listAdapter.setEntryField(InspectItemListAdapter.ENTRY_FIELD_LONG, (height/this.getBaseHeight())+"");
				
			}
		}*/
	}
	protected void restoreAllInspectDrawingObjects(){
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this.getActivity());
		@SuppressWarnings("unused")
		ArrayList<InspectDataObjectSaved> objSavedList = null;
		try {
			if ((this.currentJobRequest != null)&&
				(this.customerSurveySite != null)){
				/*
				objSavedList =  dataAdapter.getInspectDataObjectSaved(this.currentJobRequest.getJobRequestID(), 
						this.customerSurveySite.getCustomerSurveySiteID());
				*/
				objSavedList =  dataAdapter.getInspectDataObjectSaved(this.currentTask.getTaskCode(), 
						this.customerSurveySite.getCustomerSurveySiteID());

				if (objSavedList != null)
				{
					//repaint all object on screen

					ArrayList<InspectDataItem> inspectDataItemList =  dataAdapter.getAllInspectDataItem();
					
					for(InspectDataObjectSaved dataSaved : objSavedList)
					{
						InspectDataItem inspectItem = null;
						
						for(InspectDataItem item : inspectDataItemList)
						{
							if (item.getInspectDataItemID() == dataSaved.getInspectDataItemID()){
								inspectItem = item;
								break;
							}
						}
						
						if (inspectItem != null){
							InspecItemViewCreator view = new InspecItemViewCreator(this.getActivity(),0);	
							
							/*
							 ScreenUtil.SizeInPixel sip = 
					            		ScreenUtil.convertSizeInPixel(getActivity(), 
					            				dataSaved, 
					            				dataSaved.getWidth(), 
					            				dataSaved.getdLong());
							 */
							/*
							ScreenUtil.SizeInPixel sip =
									ScreenUtil.calcObjectSizeByScale(getActivity(),
											dataSaved.getWidth(),
											dataSaved.getdLong());
											*/
							final View vItem = view.createInspectItemView(inspectItem,
									dataSaved,
									dataSaved.getWidthObject(),
									dataSaved.getLongObject());
							
							if (layoutContainer != null)
							{
								if (layoutContainer instanceof ViewGroup)
								{
									if (this.currentJobRequest != null){
										((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
										.setJobRequestID(this.currentJobRequest.getJobRequestID());
									}
									if (this.customerSurveySite != null)
									{
										((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
										.setCustomerSurveySiteID(this.customerSurveySite.getCustomerSurveySiteID());					
										
										((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
										.setSiteAddressKey(this.customerSurveySite.getSiteAddressKey());
										
										((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
										.setTeamIDObjectOwner(SharedPreferenceUtil.getTeamID(getActivity()));
									}
									if (this.currentTask != null)
									{
										((InspectItemViewState)vItem.getTag()).getInspectDataObjectSaved()
										.setTaskCode(this.currentTask.getTaskCode());
									}
									
						            //int inspectWidth = sip.widthPixel;
						            //int inspectHeight = sip.heightPixel;
										
									RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vItem.getLayoutParams();
						            layoutParams.leftMargin = (int)dataSaved.getInspectDataItemStartX();
						            layoutParams.topMargin = (int)dataSaved.getInspectDataItemStartY();
						            
						            /*
						            ImageView imgView = (ImageView)vItem.findViewById(R.id.inspect_img);					
									if (imgView != null)
									{
										if (!inspectItem.isCameraObject()){
																						
										}
									}
						             */
						           // layoutParams.rightMargin = -1 * (inspectWidth);
						           // layoutParams.bottomMargin = -1 * (inspectHeight);
						            
						            
						           // Log.d("DEBUG_D","Restore object x = "+layoutParams.leftMargin+", y = "+layoutParams.topMargin);
						           // Log.d("DEBUG_D", "object width = "+inspectWidth+" , height = "+inspectHeight);
						            
//						            layoutParams.width = inspectWidth;
//						            layoutParams.height = inspectHeight;
						            
						            vItem.setLayoutParams(layoutParams);
						            
								    //imgView.invalidate();
						           	
						            //final ImageView imgView = (ImageView)vItem.findViewById(R.id.inspect_img);
									//RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams)imgView.getLayoutParams();
									//rl.leftMargin += (inspectWidth/2)+(imgView.getWidth()/2);
									//rl.topMargin += (inspectHeight/2)+(imgView.getHeight()/2);
									//imgView.setLayoutParams(rl);

									//imgView.setVisibility(View.GONE);
									
						            setSelected(vItem,true,false);
						            setVisibleConerButton(vItem,false);
									vItem.setRotation((float)dataSaved.getAngle());

									/*
									resizeBorderView(vItem,
											inspectWidth,
											inspectHeight);
									*/
//						            ImageView imgView = (ImageView)vItem.findViewById(R.id.inspect_img);
//						            imgView.setScaleType(ScaleType.)
//									vItem.invalidate();

									/*
						            ImageView imgView = (ImageView)vItem.findViewById(R.id.inspect_img);
									RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams)imgView.getLayoutParams();
									rl.leftMargin += (inspectWidth/2)+(imgView.getWidth()/2);
									rl.topMargin += (inspectHeight/2)+(imgView.getHeight()/2);
*/
//									imgView.setLayoutParams(rl);
									
									//imgView.invalidate();
									((ViewGroup)layoutContainer).addView(vItem);

									//imgView.setVisibility(View.VISIBLE);

									vItem.invalidate();
									

//									vItem.requestLayout();
//									vItem.invalidate();
									
									
									startMoveItemListener(((ViewGroup)layoutContainer));
								}
								
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	private synchronized boolean saveAllDataEntry() throws Exception{
		
	    boolean bRet = true;
		if (layoutContainer != null)
		{
			if (layoutContainer instanceof ViewGroup)
			{
				getActivity().runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						unselectAll();
						
						
						if (seekBarScale != null){
			                  seekBarScale.setProgress(seekBarScale.getMax()-50);
			            }
					}
					
				});
				PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this.getActivity());
                
                
				
				@SuppressWarnings("unused")
				final ArrayList<InspectDataObjectSaved> inspectDataObjectSavedList = new ArrayList<InspectDataObjectSaved>();
				ArrayList<InspectItemViewState> stateList = new ArrayList<InspectItemViewState>();
				ViewGroup vG = (ViewGroup)layoutContainer;
				if (vG.getChildCount() > 0)
				{

					for(int i = 0 ; i < vG.getChildCount();i++)
					{
						View vInspect = vG.getChildAt(i);
						if (vInspect.getTag() instanceof InspectItemViewState)
						{
							InspectItemViewState vState = (InspectItemViewState)vInspect.getTag();
							stateList.add(vState);
							InspectDataObjectSaved dataSaved = vState.getInspectDataObjectSaved();//.setInspectDataObjectID(vState.getObjectId());
							int objectId = vState.getObjectId();
							dataSaved.setInspectDataObjectID(objectId);

							/*
							 * recalculate
							 */
							double dValues = dataSaved.getQty() * dataSaved.getMarketPrice();
							dataSaved.setValue(dValues);
							/////////////////////
							
							inspectDataObjectSavedList.add(SerializationUtils.cloneObject(dataSaved));
						}
					}
					if (inspectDataObjectSavedList.size() > 0)
					{
						//PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this.getActivity());
						/*
						 * 
						 */			
						ArrayList<TransactionStmtHolder> stmt = new ArrayList<TransactionStmtHolder>();
						stmt.addAll(inspectDataObjectSavedList);
						//int rowEffected = dataAdapter.deleteStatementHolderList(stmt);
						//rowEffected = dataAdapter.insertStatementHolderList(stmt);
						int rowEffected = dataAdapter.updateStatementHolderList(stmt, stmt);
						
						Log.d("DEBUG_D", "row effected = "+rowEffected);						
					}	
					else{
					   /*no objects*/
	                   dataAdapter.deleteAllInspectDataObjectSaved(currentTask.getTaskCode(),
	                         customerSurveySite.getCustomerSurveySiteID());
					}
					
					generateInspectHtmlFile(dataAdapter,vG,stateList);
				}else{
				   /*clear*/
				   dataAdapter.deleteAllInspectDataObjectSaved(currentTask.getTaskCode(), 
				         customerSurveySite.getCustomerSurveySiteID());
				   generateInspectHtmlFile(dataAdapter,vG,stateList);
				   
				}
			}
		}
		return bRet;
	}
	private void generateInspectHtmlFile(PSBODataAdapter dataAdapter,View vG,ArrayList<InspectItemViewState> stateList)
	throws Exception
	{
	   /*
        * 
        * save all views
        */
       Date d = new Date();
       long l_time = d.getTime();
       //String fileName = l_time+".jpg";
       String fileName = DataUtil.regenerateTaskCodeForMakeFolder(currentTask.getTaskCode());
       fileName += "-";
       fileName += customerSurveySite.getCustomerSurveySiteID();
       
       String htmlFileName = fileName + ".html";
       
       fileName += ".jpg";
   
       
       String savedToFile = ImageUtil.captureViewToFile(getActivity(),
               currentTask, 
               customerSurveySite, 
               vG, 
               fileName, 
               InspectSaveFilter.SAVE_ALL_ITEM);
       
       Log.d("DEBUG_D", "Saved to file -> "+ savedToFile);
       
       if ((savedToFile != null)&&(!savedToFile.isEmpty()))
       {
           
           InspectDataSVGResult svgResult = new InspectDataSVGResult();
           svgResult.setTaskCode(currentTask.getTaskCode());
           svgResult.setTaskDuplicateNo(currentTask.getTaskDuplicatedNo());
           svgResult.setCustomerSurveySiteID(customerSurveySite.getCustomerSurveySiteID());
           svgResult.setSvgResultFullLayout(savedToFile);
//         svgResult.setSvgResultLayoutOnly(savedLayoutToFile);
           
           if (currentLocation != null)
           {
               svgResult.setLocationLatitude(currentLocation.getLatitude());
               svgResult.setLocationLongitude(currentLocation.getLongitude());
           }
           
           int rowEffected = dataAdapter.updateInspectDataSVGResult(svgResult);
           if (this.currentTask != null)
           {
               dataAdapter.updateTaskStatus(this.currentTask.getTaskCode(),
                       currentTask.getTaskDuplicatedNo(),
                       TaskStatus.LOCAL_SAVED);
           }
           /*
            * write html 
            */
           HTMLInspectPreviewUtil.writeHtml(getActivity(), 
                   htmlFileName, 
                   savedToFile, stateList);
           
       }else{
           //MessageBox.showMessage(getActivity(), "Error", "Can't generate inspect result file");
           throw new Exception("Can't generate inspect result file");
       }
	}

	@Override
	public void onDataFieldChanged(InspectItemViewState viewState,String fieldName, String value) {
		// TODO Auto-generated method stub
		if (viewState != null)
		{
			Log.d("DEBUG_D", "KEY = "+fieldName+" , VAL = "+value);
			try{
				if (Double.parseDouble(value) > 0){
					if (fieldName.equalsIgnoreCase(InspectItemListAdapter.ENTRY_FIELD_WIDTH))
					viewState.getInspectDataObjectSaved().setWidth(Double.parseDouble(value));
				}
			}catch(Exception ex){}

			try{
				if (Double.parseDouble(value) > 0){				
					if (fieldName.equalsIgnoreCase(InspectItemListAdapter.ENTRY_FIELD_LONG))
						viewState.getInspectDataObjectSaved().setdLong(Double.parseDouble(value));
				}
			}catch(Exception ex){}


			try{
				if (Double.parseDouble(value) > 0){
					if (fieldName.equalsIgnoreCase(InspectItemListAdapter.ENTRY_FIELD_HEIGHT))
						viewState.getInspectDataObjectSaved().setHeight(Double.parseDouble(value));
				}
			}catch(Exception ex){}

			try{
				if (Double.parseDouble(value) > 0){
					if (fieldName.equalsIgnoreCase(InspectItemListAdapter.ENTRY_FIELD_VALUE))
						viewState.getInspectDataObjectSaved().setValue(Double.parseDouble(value));
				}
			}catch(Exception ex){}

		}
	}

	@Override
	public void onClickLongInspectItemDrawing(final View v ,InspectItemViewState dataItem) {
		// TODO Auto-generated method stub
		if (dataItem != null)
		{
			currentItemState = dataItem;
			
			if (dataItem.getInspectDataItem().isCameraObject())
			{
				int photoId = dataItem.getInspectDataObjectSaved().getPhotoID();
				
				Bundle argument = new Bundle();
				argument.putInt(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID, photoId);
				ArrayList<InspectDataObjectSaved> dataSaved = new ArrayList<InspectDataObjectSaved>();
				
				
				ViewGroup vG = (ViewGroup)layoutContainer;
				
			
				
				if (vG.getChildCount() > 0)
				{
					for(int i = 0 ; i < vG.getChildCount();i++)
					{
						View vInspect = vG.getChildAt(i);
						if (vInspect.getTag() instanceof InspectItemViewState)
						{
							InspectItemViewState vState = (InspectItemViewState)vInspect.getTag();
							dataSaved.add(vState.getInspectDataObjectSaved());
						}
					}
				}
				argument.putParcelableArrayList(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_SAVED, dataSaved);
				argument.putString(InstanceStateKey.KEY_ARGUMENT_TASK_CODE, this.currentTask.getTaskCode());
				argument.putInt(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY_ID, this.customerSurveySite.getCustomerSurveySiteID());
				if (this.isUniversalLayout){
				  // argument.putBoolean(InstanceStateKey.KEY_ARGUMENT_TAKE_GENERAL_PHOTO, true);
				}
				ActivityUtil.startNewActivityWithResult(getActivity(),
						InspectPhotoEntryActivity.class, 
						argument, 
						InstanceStateKey.RESULT_INSPECT_PHOTO_ENTRY);
			}
			else
			{
				if (inspectDataEntryDialog != null)
				{
					if (inspectDataEntryDialog.isVisible()){
						inspectDataEntryDialog.dismiss();
						inspectDataEntryDialog = null;
						System.gc();
					}
				}
				
				
				InspectDataObjectSaved godownSaved = null;
				View vGownDown = null;
				InspectDataObjectSaved objectEntry = dataItem.getInspectDataObjectSaved();
				ArrayList<View> objectLists =  this.getObjectViewList();
				for(View vChildObj : objectLists)
				{
					if (vChildObj.getTag() instanceof InspectItemViewState){
						InspectItemViewState viewState = (InspectItemViewState)vChildObj.getTag();
						if (viewState.getInspectDataItem().isGodownComponent())
						{
							if (objectEntry.getInspectDataObjectID() == viewState.getInspectDataObjectSaved().getInspectDataObjectID())
							{
								break;
							}
							Rect godownBounds = new Rect();
							Rect inspectObjectBounds = new Rect();
							vChildObj.invalidate();
							v.invalidate();
							vChildObj.getBackground().copyBounds(godownBounds);
							v.getBackground().copyBounds(inspectObjectBounds);
							
							
							godownBounds.left += viewState.getInspectDataObjectSaved().getInspectDataItemStartX();
							godownBounds.top += viewState.getInspectDataObjectSaved().getInspectDataItemStartY();
							godownBounds.right += viewState.getInspectDataObjectSaved().getInspectDataItemStartX();
							godownBounds.bottom += viewState.getInspectDataObjectSaved().getInspectDataItemStartY();
							
							
							inspectObjectBounds.left += objectEntry.getInspectDataItemStartX();
							inspectObjectBounds.top += objectEntry.getInspectDataItemStartY();
							inspectObjectBounds.right += objectEntry.getInspectDataItemStartX();
							inspectObjectBounds.bottom += objectEntry.getInspectDataItemStartY();

							
							boolean intersect = godownBounds.contains(
									inspectObjectBounds);
							if (intersect){
								Log.d("DEBUG_D", "Intersect");
								godownSaved = viewState.getInspectDataObjectSaved();
								vGownDown = vChildObj;
								godownSaved.setBounds(godownBounds.left,godownBounds.top,godownBounds.top,godownBounds.bottom);
								break;
							}
						}
					}
				}
				
				double remainCapacity = 0;
				double sumCapacityUsing = 0;
				if (vGownDown != null)
				{
					double maxCapacity = godownSaved.getQty();// max capacity
					for(View vChildObj : objectLists)
					{
						InspectItemViewState viewState = (InspectItemViewState)vChildObj.getTag();
						if (viewState.getInspectDataItem().isInspectObject())
						{
							Rect inspectObjectBounds = new Rect();
							vChildObj.invalidate();
							vChildObj.getBackground().copyBounds(inspectObjectBounds);
							
							inspectObjectBounds.left += viewState.getInspectDataObjectSaved().getInspectDataItemStartX();
							inspectObjectBounds.top += viewState.getInspectDataObjectSaved().getInspectDataItemStartY();
							inspectObjectBounds.right += viewState.getInspectDataObjectSaved().getInspectDataItemStartX();
							inspectObjectBounds.bottom += viewState.getInspectDataObjectSaved().getInspectDataItemStartY();
							
							if (godownSaved.getBounds().contains(inspectObjectBounds))
							{
								sumCapacityUsing += viewState.getInspectDataObjectSaved().getQty();/*capacity + over - lost*/
							}
						}
					}
					remainCapacity = maxCapacity - sumCapacityUsing;
				}
				
				
				inspectDataEntryDialog = InspectDataEntryDialog.newInstance(
						this.currentTask.getJobRequest(),
						customerSurveySite,
						dataItem.getInspectDataItem(),
						objectEntry,	
						godownSaved /*compare max width , height , deep*/,
						remainCapacity,
						isUniversalLayout?
						      InspectDataEntryType.UNIVERSAL_LAYOUT:
						         InspectDataEntryType.NORMAL_ENTRY
						);

			
				inspectDataEntryDialog.setOnClickInspectDataEntryDialog(new InspectDataEntryDialog.OnClickInspectDataEntryDialog() {
				
					@Override
					public void onClickSaveDataInspectDataEntry(
							InspectDataItem inspectDataItem, 
							InspectDataObjectSaved dataSaved) 
					{
					// TODO Auto-generated method stub
					/*
					 * save to object
					 */
						
						try {
							
							/*
							 * setup new width , height
							 */
							double width = dataSaved.getWidth();
							double height = dataSaved.getdLong();


							ScreenUtil.SizeInPixel sizeToPixel = 
									ScreenUtil.calcObjectSizeByScale(getActivity(), width, height);
							
							dataSaved.setWidthObject(sizeToPixel.widthPixel);
							dataSaved.setLongObject(sizeToPixel.heightPixel);

							getDataAdapter().updateInspectDataObjectSaved(dataSaved);
							
							
							resizeBorderView(v,
									(int)sizeToPixel.widthPixel,
									(int)sizeToPixel.heightPixel);
							
							
							setResizeInspectItemSize(v,width,height,-1,
									-1,true);
							
							v.invalidate();
							
							
							/*
							    save each a item 
							 */
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			});
			if (!inspectDataEntryDialog.isVisible())
			inspectDataEntryDialog.show(getChildFragmentManager(), InspectDataEntryDialog.class.getName());
		}
	  }
	}
	private void initialControlsFullScreen(View vControlsFullScreen)
	{
		ImageButton btnShowToolBox = (ImageButton)vControlsFullScreen.findViewById(R.id.btn_controls_full_screen_inspect_toolbox);
		ImageButton btnFullScreen = (ImageButton)vControlsFullScreen.findViewById(R.id.btn_controls_full_screen_back_to_screen);
		ImageButton btnSaved = (ImageButton)vControlsFullScreen.findViewById(R.id.btn_controls_full_screen_save);
		ImageButton btnPreview = (ImageButton)vControlsFullScreen.findViewById(R.id.btn_controls_full_screen_preview);
		
		btnShowToolBox.setOnClickListener(this);
		btnFullScreen.setOnClickListener(this);
		btnSaved.setOnClickListener(this);
		btnPreview.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.btn_controls_full_screen_preview:{
				Bundle bArgument = new Bundle();
				bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, this.currentTask);
				bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, this.customerSurveySite);
				ActivityUtil.startNewActivity(getActivity(), 
						HtmlPreviewActivity.class, 
						bArgument);
			}break;
			case R.id.btn_controls_full_screen_inspect_toolbox:{
				showInspectToolbox();
			}break;
			case R.id.btn_controls_full_screen_back_to_screen:{
			   toggleScreen();
			}break;
			case R.id.btn_controls_full_screen_save:{
/*				
				try {
					saveAllDataEntry();			
					 //if true == have layout modified and not save yet
					SharedPreferenceUtil.saveLayoutModified(getActivity(), false);//reset flag
					MessageBox.showSaveCompleteMessage(getActivity());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					MessageBox.showMessage(getSherlockActivity(), "Error", e.getMessage());
				}
*/				new DoAsyncTaskSaveInspect().execute();
			}break;
		}
	}
	
	private void showInspectToolbox(){
		View v  = currentView.findViewById(R.id.view_inspect_item_panel);
		if (v.getVisibility() == View.VISIBLE){
			v.setVisibility(View.GONE);
		}else{
			v.setVisibility(View.VISIBLE);
			showInspectItemPanel(v);
		}
	}
	private void showFullScreen()
	{
       Fragment f = getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
       if (f != null)
       {
           FragmentTransaction ft = 
                   getSherlockActivity().getSupportFragmentManager().beginTransaction();

           ActionBar actionBar = getSherlockActivity().getSupportActionBar();
           ft.hide(f);
           actionBar.hide();
           isShowFullScreen = true;
           
           vcontrols_full_screen.setVisibility(View.VISIBLE);
           vcontrols_full_screen.invalidate();
           vcontrols_full_screen.requestLayout();
           
          
           
           View v = getActivity().findViewById(R.id.dummy_padding);
           v.setVisibility(View.GONE);
           currentView.requestLayout();
           
           ft.commit();
       }	   
	}

	
	private void showNormalScren(){
	   Fragment f = getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
       if (f != null)
       {
           FragmentTransaction ft = 
                   getSherlockActivity().getSupportFragmentManager().beginTransaction();
           
           ActionBar actionBar = getSherlockActivity().getSupportActionBar();
           actionBar.show();
           
           isShowFullScreen = false;
           //setScreenMode(false);
           View v = getActivity().findViewById(R.id.dummy_padding);
           v.setVisibility(View.VISIBLE);
           ft.show(f);                                     
           vcontrols_full_screen.setVisibility(View.GONE);
           currentView.requestLayout();
           ft.commit();
       }       
	}
	private void toggleScreen()
	{
		Fragment f = getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
		if (f != null)
		{
			FragmentTransaction ft = 
					getSherlockActivity().getSupportFragmentManager().beginTransaction();

			ActionBar actionBar = getSherlockActivity().getSupportActionBar();

			if (f.isVisible())
			{
				ft.hide(f);
				
				actionBar.hide();
				
				isShowFullScreen = true;
				//setScreenMode(true);
				vcontrols_full_screen.setVisibility(View.VISIBLE);
				View v = getActivity().findViewById(R.id.dummy_padding);
				v.setVisibility(View.GONE);
				currentView.requestLayout();
			}else{
				actionBar.show();
				
				isShowFullScreen = false;
				//setScreenMode(false);
				View v = getActivity().findViewById(R.id.dummy_padding);
				v.setVisibility(View.VISIBLE);
				ft.show(f);										
				vcontrols_full_screen.setVisibility(View.GONE);
				currentView.requestLayout();
			}
			ft.commit();
		}
		
		
		if (seekBarScale != null){
           seekBarScale.setProgress(seekBarScale.getMax()-50);
        }
	}
	/*
	private void setScreenMode(boolean fullscreen)
	{
		  if(fullscreen) {
	            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	        } else {
	        	getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        	getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	        }

	}*/

	@Override
	public void onLocationUpdated(Location location) {
		// TODO Auto-generated method stub
		this.currentLocation = location;
	}

	@Override
	public void onClickCopyInspectItemDrawing(InspectItemViewState dataItem) {
		// TODO Auto-generated method stub
		
//			InspectItemViewState object_cloned = SerializationUtils.cloneObject(dataItem);
//			object_cloned.setObjectId(-1);/*initial for increase*/
			InspecItemViewCreator creator = new InspecItemViewCreator(this.getActivity(),0);
			InspectDataObjectSaved inspectDataObjectSaved = 
					SerializationUtils.cloneObject(dataItem.getInspectDataObjectSaved());
			
			  int objIdMax = 0;
	            ViewGroup vg = (ViewGroup)layoutContainer;
	            for(int i = 0; i < vg.getChildCount();i++){
	               View vEach = vg.getChildAt(i);
	               if (vEach.getTag() instanceof InspectItemViewState){
	                  InspectItemViewState state = (InspectItemViewState)vEach.getTag();
	                  if (state.getObjectId() > objIdMax){
	                     objIdMax = state.getObjectId();
	                  }
	               }
	            }
	            
	        inspectDataObjectSaved.setInspectDataObjectID(objIdMax+1);
	            
			View newView = creator.createInspectItemView(
					dataItem.getInspectDataItem(),
					inspectDataObjectSaved,
					inspectDataObjectSaved.getWidthObject(),
					inspectDataObjectSaved.getLongObject());
			
			
			
			int X = 0;
			int Y = 0;
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) newView.getLayoutParams();
			
			if ((hScroll != null)&&(vScroll != null)){
				//int maxWidth = (int)this.getActivity().getResources().getDimension(R.dimen.draw_layout_width);
				//int maxDeep = (int)this.getActivity().getResources().getDimension(R.dimen.draw_layout_deep);
				
//				int vscrollX = vScroll.getScrollX();
				int vscrollY = vScroll.getScrollY();
				
				int hscrollX = hScroll.getScrollX();
//				int hscrollY = hScroll.getScrollY();
				
				DisplayMetrics dm = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
				int w = dm.widthPixels; // etc...
				int h = dm.heightPixels;
				
				X = (int)(((double)w/2)+hscrollX);
				Y = (int)(((double)h/2)+vscrollY);
				X = X - (int)((double)layoutParams.width/2);
				Y = Y - (int)((double)layoutParams.height/2);
			}
			layoutParams.leftMargin = X;
			layoutParams.topMargin = Y;
			newView.setLayoutParams(layoutParams);

			
			((ViewGroup)layoutContainer).addView(newView);
			newView.invalidate();
			startMoveItemListener(((ViewGroup)layoutContainer));
			super.setSelected(newView, true,false);


			
			 InspectItemViewState object_cloned = (InspectItemViewState)newView.getTag();
			 object_cloned.getInspectDataObjectSaved().setdLong(dataItem.getInspectDataObjectSaved().getdLong());
			 object_cloned.getInspectDataObjectSaved().setWidth(dataItem.getInspectDataObjectSaved().getWidth());
			 object_cloned.getInspectDataObjectSaved().setHeight(dataItem.getInspectDataObjectSaved().getHeight());

			 object_cloned.getInspectDataObjectSaved().setWidthObject(dataItem.getInspectDataObjectSaved().getWidthObject());
			 object_cloned.getInspectDataObjectSaved().setLongObject(dataItem.getInspectDataObjectSaved().getLongObject());
			 
			 if (object_cloned.getInspectDataItem().isCameraObject())
			 {
//				 object_cloned.getInspectDataObjectSaved().set
				 object_cloned.getInspectDataObjectSaved().setPhotoID(0);
				 object_cloned.getInspectDataObjectSaved().setPhotosSaved(null);
			 }
				/*
				double width = object_cloned.getInspectDataObjectSaved().getWidth();
				double height = object_cloned.getInspectDataObjectSaved().getdLong();


				ScreenUtil.SizeInPixel sizeToPixel = 
						ScreenUtil.calcObjectSizeByScale(getActivity(), width, height);
				
				object_cloned.getInspectDataObjectSaved().setWidthObject(sizeToPixel.widthPixel);
				object_cloned.getInspectDataObjectSaved().setLongObject(sizeToPixel.heightPixel);
				*/
				resizeBorderView(newView,
						/*sizeToPixel.widthPixel*/object_cloned.getInspectDataObjectSaved().getWidthObject(),
						object_cloned.getInspectDataObjectSaved().getLongObject());
				
				
				setResizeInspectItemSize(newView,
						object_cloned.getInspectDataObjectSaved().getWidth(),
						object_cloned.getInspectDataObjectSaved().getdLong(),-1,
						-1,true);
				
				newView.invalidate();
				////////////////////////////
				

			//}
				try 
                {
                    getDataAdapter().updateInspectDataObjectSaved(
                          object_cloned.getInspectDataObjectSaved()
                           );
                }
                catch (Exception e) {
                   // TODO Auto-generated catch block
                  e.printStackTrace();
                  MessageBox.showMessage(getActivity(), R.string.message_box_title_error, e.getMessage());
               }
		
	}
	public class TaskSaveInspectResult
	{
		public boolean bSuccessful;
		public String errorMessage;
	}
	class DoAsyncTaskSaveInspect extends AsyncTask<Void,Void,TaskSaveInspectResult>
	{
		private ProgressDialog progressDialog = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = 
					ProgressDialog.show(getActivity(), "", getString(R.string.text_save_processing));
			progressDialog.setCancelable(false);

		}
		@Override
		protected TaskSaveInspectResult doInBackground(Void... params) {
			// TODO Auto-generated method stub
			TaskSaveInspectResult result = new TaskSaveInspectResult();
			result.bSuccessful = false;
			result.errorMessage = "";
			
			try {
				/*
				 * 
				 */
			   
	             ReportInspectSummaryStatusHelper.resetReportStatus(getActivity(), currentTask);

				saveAllDataEntry();	
				
	               
				result.bSuccessful = true;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				result.bSuccessful = false;
				result.errorMessage = e.getMessage();
			}
			return result;
		}
		@Override
		protected void onPostExecute(TaskSaveInspectResult result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progressDialog != null)
			{
				progressDialog.dismiss();
			}
			/*
			 */
			if (result.bSuccessful){
				/*
				 * if true == have layout modified and not save yet
				 */
				SharedPreferenceUtil.saveLayoutModified(getActivity(), false);/*reset flag*/

				MessageBox.showSaveCompleteMessage(getActivity());
				
				
				Bundle bArgument = new Bundle();
				bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK,
										currentTask);
				bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY,
										customerSurveySite);
				ActivityUtil.startNewActivity(getActivity(),
						HtmlPreviewActivity.class, bArgument);
				
				
				
				getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
						Uri.parse("file://" + Environment.getExternalStorageDirectory())));
			}else{
				MessageBox.showMessage(getActivity(), 
						getActivity().getString(R.string.text_error_title),
						result.errorMessage);				
			}
		}
		
	}
}
