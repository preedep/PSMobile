package com.epro.psmobile.fragment;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.InspectPhotoEntryActivity;
import com.epro.psmobile.PhotoGalleryActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.UniversalCommentActivity;
import com.epro.psmobile.adapter.CarReportListEntryAdapter;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataObjectPhotoSaved;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.dialog.CarInspectDialog;
import com.epro.psmobile.form.xml.ReportListEntry;
import com.epro.psmobile.form.xml.ReportListEntryColumn;
import com.epro.psmobile.form.xml.ReportListEntryXMLReader;
import com.epro.psmobile.fragment.ContentViewBaseFragment.InspectOptMenuType;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.ActivityUtil;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.ImageUtil;
import com.epro.psmobile.util.InspectServiceSupportUtil;
import com.epro.psmobile.util.MessageBox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/*
 * for car , machine , etc.
 */
public abstract class InspectReportListFragment extends ContentViewBaseFragment implements MediaScannerConnectionClient
{
    protected static JobRequest jobRequest;
    protected static Task currentTask;
    protected static CustomerSurveySite customerSurveySite;
    protected int rowOffset;
	protected View currentView;
	protected boolean showInPaging;
	protected String keyFilter = "";

	private Uri imageUri;
	
	//public static View subView;
	//private JobRequestProduct currentJobRequestProduct;	
	private MediaScannerConnection conn;
	
	protected ArrayList<JobRequestProduct> jobRequestProducts;
	
	private LayoutInflater inflater = null;
	
	/*
	 * for default supported old function
	 */
	public static Fragment newInstance(JobRequest jobRequest,
          Task currentTask,
          CustomerSurveySite surveySite)
	{
	   return newInstance(jobRequest,currentTask,surveySite,0,false,"");
	}
	public static Fragment newInstance(JobRequest jobRequest,
			Task currentTask,
			CustomerSurveySite surveySite,
			int rowOffset,
			boolean showInPaging,
			String keyFilter)
	{
		Fragment fragment = null;
		if (jobRequest.getInspectType().getInspectTypeID() == InspectServiceSupportUtil.SERVICE_CAR_INSPECT)
		{
			fragment = new InspectCarReportListEntryFragment();
		}else{
		   if (!showInPaging){
		      fragment = new UniversalInspectListFragment();
		   }else{
		      fragment = new UniversalInspectListFragmentItem();
		   }
		}

		if (fragment != null)
		{
			Bundle bArgument = new Bundle();
			bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
			bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);
			bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, surveySite);
			bArgument.putInt(InstanceStateKey.KEY_ARGUMENT_ROW_OFFSET_LIST_DISPLAY, rowOffset);
			bArgument.putBoolean(InstanceStateKey.KEY_ARGUMENT_SHOW_IN_PAGING, showInPaging);
			bArgument.putString(InstanceStateKey.KEY_ARGUMENT_UNIVERSAL_KEY_FILTER, keyFilter);
			fragment.setArguments(bArgument);
		}
		return fragment;
	}

	
	public InspectReportListFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	     
	    inflater = arg0;
	    this.getSherlockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

	    jobRequest = this.getArguments().getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
	    currentTask = this.getArguments().getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
	    customerSurveySite = this.getArguments().getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
	    rowOffset = this.getArguments().getInt(InstanceStateKey.KEY_ARGUMENT_ROW_OFFSET_LIST_DISPLAY);
	    showInPaging = this.getArguments().getBoolean(InstanceStateKey.KEY_ARGUMENT_SHOW_IN_PAGING);
	    keyFilter = this.getArguments().getString(InstanceStateKey.KEY_ARGUMENT_UNIVERSAL_KEY_FILTER);
	    
	    
	    if (!showInPaging)
	    {
	        if (jobRequest.getInspectType().getInspectTypeID() == InspectServiceSupportUtil.SERVICE_CAR_INSPECT)
	        {
	           currentView = arg0.inflate(R.layout.ps_activity_report_list_entry_v2, arg1, false);
	        }else{
	           currentView = arg0.inflate(R.layout.universal_inspect_list_view_fragment, arg1, false);
	        }	       
	    }else{
	       /*show in paging use for universal only*/
	       currentView = arg0.inflate(R.layout.universal_inspect_list_view_fragment_item, arg1, false);	       
//	       InspectReportListFragment.subView = currentView;
	    }
	    
	    
		if (!showInPaging){
		   doPopupCheckIn();
		}else{
	      // initial(currentView);
		}
		//this.setRetainInstance(true);
		
		
		return currentView;
	}
	protected abstract void initial(View currentView);
	
	/* (non-Javadoc)
    * @see com.epro.psmobile.fragment.ContentViewBaseFragment#onPause()
    */
   @Override
   public void onPause() {
      // TODO Auto-generated method stub
      super.onPause();
   }
   /* (non-Javadoc)
    * @see com.epro.psmobile.fragment.ContentViewBaseFragment#onResume()
    */
   @Override
   public void onResume() {
      // TODO Auto-generated method stub
      if (this.showInPaging){
         if (inflater != null){
//            currentView = inflater.inflate(R.layout.universal_inspect_list_view_fragment_item, arg1, false);        
         }
      }
      super.onResume();
   }
   /* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	/*
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case InstanceStateKey.TAKE_PICTURE:
		{
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = getImageUri();
				getActivity().getContentResolver().notifyChange(selectedImage, null);
	            
				ContentResolver cr = getActivity().getContentResolver();
		        Bitmap bitmap = null;
		        try {
		                 bitmap = android.provider.MediaStore.Images.Media
		                 .getBitmap(cr, selectedImage);

		             
		           if (bitmap != null)
		           {
		        	
		        	   String folder = getPhotoFolder(currentJobRequestProduct);
		        	   File f_folder = new File(folder);
		        	   if (!f_folder.exists())
		        	   {
		        		   f_folder.mkdirs();
		        	   }
		        	   Date d = new Date();
		        	   String fileName = d.getTime()+".jpg";
		        	   ImageUtil.saveImageFileV2(getActivity(), folder, fileName, bitmap);//create full name of folder outside function
		           }
		        }catch(Exception ex)
		        {
		        	ex.printStackTrace();
		        }
			}
		  }
		   break;
	   }
	}
*/
	public Uri getImageUri() {
		return imageUri;
	}
	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}
	/*
	private String getPhotoFolder(JobRequestProduct jobRequestProduct)
	{
		
  	   // write image to folder
  	    
  	   String folder = 
 				Environment.getExternalStorageDirectory().getAbsolutePath();
  	   folder += "/";
  	   folder += CommonValues.INSPECT_PHOTO_FOLDER;
  	   folder += "/";
  	   folder += DataUtil.regenerateTaskCodeForMakeFolder(currentTask.getTaskCode());//.replace("/", "_");
  	   folder += "/";
  	   folder += jobRequest.getInspectType().getInspectTypeID();
  	   folder += "/";
  	   folder += customerSurveySite.getCustomerID();
  	   folder += "/";
  	   folder += jobRequestProduct.getProductRowID();
  	   folder += "/";
  	   
  	   return folder;
	}*/
	
	@Override
	public void onMediaScannerConnected() {
		// TODO Auto-generated method stub
	   /*
		String folder = getPhotoFolder(currentJobRequestProduct);
		File f_folder = new File(folder);
		conn.scanFile(f_folder.getAbsolutePath(), "image/*");*/
	}
	@Override
	public void onScanCompleted(String path, Uri uri)
    {
		// TODO Auto-generated method stub
		Log.d("DEBUG_D", "onScanCompleted");
		try
        {
            if (uri != null) 
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.fromFile(new File(path)));
                getActivity().startActivity(intent);
            }
        }
        finally 
        {
            conn.disconnect();
            conn = null;
        }
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater,InspectOptMenuType.ENTRY_FORM_AND_ZOOM);
		
		MenuItem mTakePhoto = menu.findItem(R.id.menu_car_inspect_general_take_photo);
		MenuItem mPlus = menu.findItem(R.id.menu_entry_add_new_row);
		if (mPlus != null)
		{
		   mPlus.setVisible(false);
		}
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		switch(id)
		{
		    case R.id.menu_switch_to_universal:
		    {/*
		       if (this.saveAllData())
		       {
		         this.doOpenUniversalLayout();
		       }*/
		       SaveParam saveParam = new SaveParam();
		       saveParam.needOpenUniversalLayout = true;
		       new SaveAsyncTask(true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, saveParam);
		    }break;
		    case R.id.menu_car_inspect_general_take_photo:{
		       this.doTakeGeneralPhoto();
		    }break;
			case R.id.menu_entry_full_screen:
			{
				toggleFullScreen();
				if (currentView != null)
				   currentView.requestLayout();				
			}break;
			case R.id.menu_entry_save:
			{
			   /*
				if (this.saveAllData())
				{
				   MessageBox.showSaveCompleteMessage(getActivity());
				}*/
			   if (!showInPaging)
			      new SaveAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			   
			}break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class SaveResult{
	   boolean isSuccess;
	   boolean needOpenUniversalLayout;
	}
	class SaveParam{
	   boolean needOpenUniversalLayout;
	}
	class SaveAsyncTask extends AsyncTask<SaveParam,Void,SaveResult>{

	   private boolean showWaiting = false;
	   private ProgressDialog dlg;
	   public SaveAsyncTask(){
	      this(false);
       }
	   public SaveAsyncTask(boolean showWaiting){
	      this.showWaiting = showWaiting;
	   }
	   /* (non-Javadoc)
	       * @see android.os.AsyncTask#onPreExecute()
	       */
	      @Override
	      protected void onPreExecute() {
	         // TODO Auto-generated method stub
	         super.onPreExecute();
	         if (showWaiting){
	            dlg = ProgressDialog.show(getSherlockActivity(), "", "Waiting...");
	            dlg.show();
	         }
	      }

      @Override
      protected SaveResult doInBackground(SaveParam... params) {
         // TODO Auto-generated method stub
         SaveResult saveResult = new SaveResult();
         saveResult.isSuccess = saveAllData();
         if ((params != null)&&(params.length > 0)){
            saveResult.needOpenUniversalLayout = params[0].needOpenUniversalLayout;
         }
         return saveResult;
      }

      /* (non-Javadoc)
       * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
       */
      @Override
      protected void onPostExecute(SaveResult result) {
         // TODO Auto-generated method stub
         super.onPostExecute(result);
         if (result.isSuccess)
         {
            if (result.needOpenUniversalLayout){
               if (dlg != null){
                  dlg.dismiss();
               }
               doOpenUniversalLayout();
            }else{
               MessageBox.showSaveCompleteMessage(getActivity());
            }
         }else{
            MessageBox.showMessage(getActivity(), R.string.text_error_title, R.string.text_insert_error_msg);
         }
      }

     
     
	   
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Intent intent = this.getActivity().getIntent();
		if (intent != null)
		{
			Bundle bundle = intent.getExtras();
			if (bundle != null)
			{
				onViewStateRestored(bundle);	
			}
		}
		super.onStart();
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	
	/* (non-Javadoc)
    * @see android.support.v4.app.Fragment#onDestroyView()
    */
   @Override
   public void onDestroyView() {
      // TODO Auto-generated method stub
      super.onDestroyView();
      this.getSherlockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

   }


   protected void doOpenUniversalLayout()
	{
       if (this.showInPaging)return;
       
       Bundle argument = new Bundle();
       
       argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, InspectReportListFragment.jobRequest);
       argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, InspectReportListFragment.customerSurveySite);
       argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, InspectReportListFragment.currentTask);
    
       Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
       boolean isShown = f.isVisible();
       argument.putBoolean(InstanceStateKey.KEY_ARGUMENT_SCREEN_STATE, isShown);
       argument.putBoolean(InstanceStateKey.KEY_ARGUMENT_IS_UNIVERSAL_LAYOUT, true);
       
       final FragmentManager fm = getActivity().getSupportFragmentManager();
       fm.addOnBackStackChangedListener(new OnBackStackChangedListener(){

         int count = 0;
         @Override
         public void onBackStackChanged() {
            // TODO Auto-generated method stub
            //Log.d("DEBUG_D_D", "back stack");
            if (count > 0){
               initial(currentView);
               fm.removeOnBackStackChangedListener(this);
            }
            count++;
         }
          
       });
       FragmentTransaction ft = fm.beginTransaction();
    
       DrawingInspectFragment drawingFragment = new DrawingInspectFragment();                 
       drawingFragment.setArguments(argument);
    
       ft.replace(R.id.content_frag, drawingFragment,DrawingInspectFragment.class.getName());
       //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
       ft.addToBackStack(null);
       ft.commit();
	}
	protected abstract boolean saveAllData();
	protected void doTakeGeneralPhoto()
	{
       Bundle argument = new Bundle();
       
       argument.putString(InstanceStateKey.KEY_ARGUMENT_TASK_CODE, InspectReportListFragment.currentTask.getTaskCode());
       argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, InspectReportListFragment.jobRequest);
       argument.putInt(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY_ID, InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID());
       argument.putBoolean(InstanceStateKey.KEY_ARGUMENT_TAKE_GENERAL_PHOTO, true);

       int photoSetId = 0;
       
       PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
       try {
          ArrayList<InspectDataObjectPhotoSaved> photoSavedList = 
                dataAdapter.getInspectDataObjectPhotoSavedWithGeneralImage(InspectReportListFragment.currentTask.getTaskCode(),
                      InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID());
          if (photoSavedList != null)
          {
             if (photoSavedList.size() > 0)
             {
                photoSetId = photoSavedList.get(0).getPhotoID();
             }
          }
       }
       catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }
       
       argument.putInt(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID,photoSetId);
       ActivityUtil.startNewActivityWithResult(getActivity(),
             InspectPhotoEntryActivity.class, 
             argument, 
             InstanceStateKey.RESULT_INSPECT_PHOTO_ENTRY);
	}
	protected void doOpenUniversalComment(JobRequestProduct jobRequestProduct,
	      ArrayList<InspectFormView> colProperties)
	{
	   Bundle argument = new Bundle();
	   argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, InspectReportListFragment.currentTask);
       argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, InspectReportListFragment.jobRequest);
       argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, InspectReportListFragment.customerSurveySite);
       argument.putParcelableArrayList(InstanceStateKey.KEY_ARGUMENT_UNIVERSAL_COL_PROPERTIES,colProperties);
       argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_PRODUCT_REQUEST, jobRequestProduct);
       
       ActivityUtil.startNewActivityWithResult(getActivity(),
             UniversalCommentActivity.class,
             argument,
             InstanceStateKey.RESULT_INSPECT_UNIVERSAL_SAVED_COMMENT);
	}
	protected void doActivityResultForCommentSaved(int requestCode,
          int resultCode,
          Intent data,
          JobRequestProduct currentJobRequestProduct,
          ListView lsViewt)
    {
       switch (requestCode) {
             case InstanceStateKey.RESULT_INSPECT_UNIVERSAL_SAVED_COMMENT:{
                 if (resultCode == Activity.RESULT_OK)
                 {
                    currentJobRequestProduct = data.getParcelableExtra(InstanceStateKey.KEY_ARGUMENT_JOB_PRODUCT_REQUEST);
                    currentJobRequestProduct.setHasCheckList(true);
                    onPhotoSetIdUpdated(currentJobRequestProduct);/*reload row*/
                 }
             }break;
       }
    }
             
	protected void doActivityResultForTakePhoto(int requestCode,
	      int resultCode,
	      Intent data,
	      JobRequestProduct currentJobRequestProduct,
	      ListView lsView,boolean isCarInspect)
	{
	   switch (requestCode) {
	         case InstanceStateKey.RESULT_INSPECT_PHOTO_ENTRY:{
	             if (resultCode == Activity.RESULT_OK)
	             {
	                 int photoId = data.getIntExtra(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID, -1);
	                 if (currentJobRequestProduct != null)
	                 {
	                    currentJobRequestProduct.setPhotoSetID(photoId);
	                    /*
	                     * update current jobrequestProduct
	                     */
	                    /*
	                     * update
	                     */
	                    try {
	                       /*
	                          super.getDataAdapter().updateInspectDataObjectSaved(
	                                currentItemState.getInspectDataObjectSaved());*/
	                       
	                       if (isCarInspect){
	                          super.getDataAdapter().updateCarPhotoSetIDInJobRequestProduct(currentJobRequestProduct);
	                       }else{
	                          super.getDataAdapter().updateUniversalPhotoSetIDInJobRequestProduct(currentJobRequestProduct);
	                       }
	                       
	                       if (isCarInspect){
	                          BaseAdapter adapter = (BaseAdapter)(lsView.getAdapter());
	                          if (adapter != null){
	                             adapter.notifyDataSetChanged();
	                             adapter.notifyDataSetInvalidated();
	                          }
	                       }else{
	                          onPhotoSetIdUpdated(currentJobRequestProduct);
	                       }
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
	      }	   
	}
	protected abstract void onPhotoSetIdUpdated(JobRequestProduct currentJobRequestProduct);
	protected abstract void onListViewUpdated();
	
	protected void doPopupCheckIn(){
	   Log.d("DEBUG_D_D",InspectReportListFragment.currentTask.getTaskCode()+" , "+InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID());
	      try {
	         CarInspectStampLocation carInspectStampLocation = 
	               this.getDataAdapter().findCarInspectStampLocation(InspectReportListFragment.currentTask.getTaskCode(), 
	                     InspectReportListFragment.currentTask.getTaskDuplicatedNo(), 
	                     InspectReportListFragment.currentTask.getJobRequest().getJobRequestID(), 
	                     InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID());
	         if (carInspectStampLocation == null)
	         {
	            if ((InspectReportListFragment.customerSurveySite.getLocationCheckInDate() == null)||(InspectReportListFragment.customerSurveySite.getLocationCheckInDate().isEmpty()))
	            {
	               //never stamp show dialog
	               CarInspectDialog dlg = CarInspectDialog.newInstance(currentTask, customerSurveySite);
	               dlg.setCarInspectUpdate(new CarInspectDialog.OnCarInspectUpdated() {
	                  
	                  @Override
	                  public void onUpdated() {
	                     // TODO Auto-generated method stub
	                     /*
	                     setupHeader(vGroupHeader);
	                     setupListView(lsView);
	                     initialControls(currentView);*/
	                     onListViewUpdated();
	                  }
	               });
	               dlg.show(getChildFragmentManager(), CarInspectDialog.class.getName());               
	            }else
	            {
	               
	               ArrayList<CustomerSurveySite> sites = 
	                     getDataAdapter().findCustomerSurveySite(currentTask.getTaskID());
	               if (sites != null)
	               {
	                  int iCountInsertNewTmp = 0;
	                  for(CustomerSurveySite site : sites)
	                  {
	                     if ((site.getLocationCheckInDate() != null)&&(!site.getLocationCheckInDate().isEmpty()))
	                     {
	                        //
	                        // update to 
	                        //
	                        CarInspectStampLocation tmp_carInspectStampLocation = 
	                              this.getDataAdapter().findCarInspectStampLocation(InspectReportListFragment.currentTask.getTaskCode(), 
	                                    InspectReportListFragment.currentTask.getTaskDuplicatedNo(), 
	                                    InspectReportListFragment.currentTask.getJobRequest().getJobRequestID(), 
	                              site.getCustomerSurveySiteID());
	                        if (tmp_carInspectStampLocation == null)
	                        {
	                           /*insert new*/
	                           CarInspectStampLocation carInspectStamp = new CarInspectStampLocation();
	                           carInspectStamp.setTaskID(currentTask.getTaskID());
	                           carInspectStamp.setTaskCode(currentTask.getTaskCode());
	                           carInspectStamp.setTaskDuplicateNo(currentTask.getTaskDuplicatedNo());
	                           carInspectStamp.setJobRequestID(currentTask.getJobRequest().getJobRequestID());
	                           carInspectStamp.setCustomerSurveySiteID(site.getCustomerSurveySiteID());
	                           carInspectStamp.setSiteAddress(site.getSiteAddress());
	                           carInspectStamp.setMilesNo(site.getMileNo());
	                          
	                           
	                           carInspectStamp.setTimeRecorded(site.getLocationCheckInDate());

	                           getDataAdapter().insertCarInspectStampLocation(carInspectStamp);
	                           iCountInsertNewTmp++;
	                        }
	                     }
	                  }
	                  if (iCountInsertNewTmp > 0){
	                     initial(currentView);
	                  }
	               }
	            }
	         }else
	         {
	            /*
	            setupHeader(vGroupHeader);
	            setupListView(lsView);
	            initialControls(currentView);*/
	            onListViewUpdated();
	         }
	      }
	      catch (Exception e) {
	         // TODO Auto-generated catch block
	         MessageBox.showMessage(getActivity(), 
	               R.string.text_error_title, 
	               e.getMessage());
	      }
	}
}
