/**
 * 
 */
package com.epro.psmobile;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.StateSet;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuInflater;
//import com.actionbarsherlock.view.MenuItem;
//import com.epro.psmobile.adapter.PictureGridPreviewAdapter;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.InspectDataObjectPhotoSaved;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectDataSavedSpinnerDisplay;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.location.PSMobileLocationManager;
import com.epro.psmobile.location.PSMobileLocationManager.OnPSMLoctaionListener;
import com.epro.psmobile.util.ImageUtil;
import com.epro.psmobile.util.InspectServiceSupportUtil;
import com.epro.psmobile.util.LocationUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.view.InspectDataSavedSpinner;

/**
 * @author nickmsft
 *
 */
public class InspectPhotoEntryActivity extends /*PsBaseActivity*/Activity implements OnPSMLoctaionListener {

    
	private class ImgTag
	{
		int no;
		boolean bSelected;
	};
	private Uri imageUri;
	private int photoSetId;
	private ArrayList<InspectDataObjectPhotoSaved> inspectPhotoSavedList = null;
	private ArrayList<InspectDataObjectPhotoSaved> allInspectPhotoSavedList = null;
    
	private ArrayList<InspectDataObjectSaved> inspectDataSavedList = null;

	private JobRequestProduct jobRequestProduct;
	
	private ViewGroup hScrollViewPhotoContainer;
	private ViewGroup productListContainer;
	private InspectDataSavedSpinner inspectDataSavedSpinner;
	private ImageView currentImageView;
	private EditText edtComment;
	private Spinner spnAngleDetail;
	private TextView currentTextProductName;
	private JobRequest jobRequest;
	private String currentTaskCode;
	
	private final static String KEY_SAVED_LIST = "com.epro.psmobile.InspectPhotoEntryActivity.KEY_SAVED_LIST";
	private final static String KEY_DATA_SAVED_LIST = "com.epro.psmobile.InspectPhotoEntryActivity.KEY_DATA_SAVED_LIST";

	private final static String SAVE_IMAGE_KEY_NAME = "com.epro.psmobile.InspectPhotoEntryActivity.SAVE_IMAGE_KEY_NAME";
	private Location currentLocation;
	private PSMobileLocationManager locManager = null;

	private boolean isTakeGeneralImage = false;
	/**
	 * 
	 */
	public InspectPhotoEntryActivity() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.epro.psmobile.PsBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ps_activity_inspect_photo_entry);
	
		inspectPhotoSavedList = new ArrayList<InspectDataObjectPhotoSaved>();
		
		
		inspectDataSavedList = this.getIntent().getParcelableArrayListExtra(
				InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_SAVED);
		
		isTakeGeneralImage = this.getIntent().getBooleanExtra(InstanceStateKey.KEY_ARGUMENT_TAKE_GENERAL_PHOTO,
		      false);
		
		
		jobRequest = this.getIntent().getParcelableExtra(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
		currentTaskCode = this.getIntent().getStringExtra(InstanceStateKey.KEY_ARGUMENT_TASK_CODE);
		if (jobRequest != null)
		{
		   if (jobRequest.getInspectType().getInspectTypeID() > InspectServiceSupportUtil.SERVICE_FARM_LAND_2)
		   {
		      jobRequestProduct = 
		            this.getIntent().getParcelableExtra(InstanceStateKey.KEY_ARGUMENT_JOB_PRODUCT_REQUEST);
		      
		   }
		}
		try{
			init();
			addPhotos();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

		if (locManager == null)
		locManager = new PSMobileLocationManager(this);

		try {
			locManager.startPSMLocationListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (locManager != null)
		{
			locManager.stopRequestLocationUpdated();
			locManager = null;
			System.gc();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.del_photo)
		{
			 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
				if (currentImageView != null)
				{
					hScrollViewPhotoContainer.removeView(currentImageView);
					if (inspectPhotoSavedList != null){
						inspectPhotoSavedList.remove(currentImageView.getTag());
					}
					if (productListContainer != null)
					{
						productListContainer.removeAllViews();
					}
					currentImageView = null;
					
					if (this.jobRequestProduct == null){
					   reloadProductList();
					}
				}
				// }
			// }
		}else if (item.getItemId() == R.id.del_product_name)
		{
			if (currentTextProductName != null){
				
				Object objCurrentImageView = currentImageView.getTag();
				Object tagProductName = currentTextProductName.getTag();
				
				
				if (objCurrentImageView instanceof InspectDataObjectPhotoSaved)
				{
					InspectDataObjectPhotoSaved objSaved = (InspectDataObjectPhotoSaved)objCurrentImageView;
					try{
						InspectDataSavedSpinnerDisplay display = (InspectDataSavedSpinnerDisplay)tagProductName;
						objSaved.removeProductName(display.toString());
					}catch(Exception ex){
						if (tagProductName instanceof String)
						{
							objSaved.removeProductName((String)tagProductName);							
						}
					}
					//objSaved.setAngleDetail(angleDetail)
					/*
					objSaved.removeProductName(
							(String)currentTextProductName.getTag());*/
					objSaved.setInspectDataTextSelected(objSaved.generateProductNamesList());
					productListContainer.removeView(currentTextProductName);
					
					if (this.jobRequestProduct == null){
					   reloadProductList();
					}
				}
				/*
				((InspectDataObjectPhotoSaved)currentImageView.getTag()).removeProductName(
												(String)currentTextProductName.getTag());
				 */
				/*
				((InspectDataObjectPhotoSaved)currentImageView.getTag()).setInspectDataTextSelected(
						((InspectDataObjectPhotoSaved)currentImageView.getTag()).generateProductNamesList());
				 */
			}
		}
		return super.onContextItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		android.view.MenuInflater inflater =  this.getMenuInflater();
		if (v instanceof ImageView){
			inflater.inflate(R.menu.context_menu_del_photo, menu);
		}else{
			inflater.inflate(R.menu.context_menu_del_product_name, menu);
			if (v instanceof TextView){
				currentTextProductName = (TextView)v;
			}
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		android.view.MenuInflater inflater = new android.view.MenuInflater(this);
		inflater.inflate(R.menu.time_attendance_menu, menu);//reuse option menu of time attendance
		return true;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
			case R.id.menu_time_attendance_take_photo:
			{
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
			    intent.putExtra(MediaStore.EXTRA_OUTPUT,
			            Uri.fromFile(photo));
			    setImageUri(Uri.fromFile(photo));
			    startActivityForResult(intent, InstanceStateKey.TAKE_PICTURE);
			}break;
			case R.id.menu_time_attendance_add_new:
			{
				 Intent i = new Intent(
	                        Intent.ACTION_PICK,
	                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

	             startActivityForResult(i, InstanceStateKey.RESULT_LOAD_IMAGE_FROM_GALLERY);
	               
			}
			break;
			case R.id.menu_time_attendance_save:{
				
				if ((inspectPhotoSavedList != null)&&(inspectPhotoSavedList.size() > 0))
				{
					/*
					 * save all
					 */
					//if (inspectPhotoSavedList != null)
					{
						for(int i = 0; i < inspectPhotoSavedList.size();i++)
						{
							if ((inspectPhotoSavedList.get(i).getInspectDataTextSelected() == null)||
									(inspectPhotoSavedList.get(i).getInspectDataTextSelected().trim().isEmpty()))
							{
								MessageBox.showMessage(this, 
										R.string.message_box_title_error,
										R.string.text_error_inspect_photo_entry_saved);
								return super.onOptionsItemSelected(item);
							}
							inspectPhotoSavedList.get(i).setPhotoNo(i+1);
						}
						
						AsyncTask<Void,Void,Boolean> saveTask = new AsyncTask<Void,Void,Boolean>(){

							ProgressDialog progressDialog;
							/* (non-Javadoc)
							 * @see android.os.AsyncTask#onPreExecute()
							 */
							@Override
							protected void onPreExecute() {
								// TODO Auto-generated method stub
								super.onPreExecute();
								progressDialog = 
										ProgressDialog.show(InspectPhotoEntryActivity.this, "", getString(R.string.text_save_processing));
								progressDialog.setCancelable(false);
							}
							/* (non-Javadoc)
							 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
							 */
							@Override
							protected void onPostExecute(Boolean result) {
								// TODO Auto-generated method stub
								if (progressDialog != null)
									progressDialog.dismiss();
								
								super.onPostExecute(result);
								if (result.booleanValue())
								{
									Intent i = new Intent();
									i.putExtra(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID, photoSetId);
									setResult(RESULT_OK, i);
									finish();	
								}
							}

			

							@Override
							protected Boolean doInBackground(Void... params) {
								// TODO Auto-generated method stub
//								int maxNo = dataAdapter.getMaxPhotoNoByID(photoSetId);
								try{
									PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(InspectPhotoEntryActivity.this);						
									/*
									for(InspectDataObjectPhotoSaved obj : inspectPhotoSavedList){
									   obj.setInspectTypeID(InspectPhotoEntryA)
									}*/
									if (isTakeGeneralImage)
									{
									   for(InspectDataObjectPhotoSaved photoSaved : inspectPhotoSavedList)
									   {
									      photoSaved.setFlagGeneralImage("Y");
									   }
									}
									int rowEffected = dataAdapter.insertInspectPhotoEntry(inspectPhotoSavedList);
									if (rowEffected > 0)return true;
								}catch(Exception ex){
										ex.printStackTrace();
								}
								return false;
							}
						};
						saveTask.execute();
					}
				}else{
				   /*case need delete all and saved*/
				   if (jobRequest != null)
				   {
				      if (jobRequest.getInspectType().getInspectTypeID() > InspectServiceSupportUtil.SERVICE_FARM_LAND_2){
				         PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this);
				         try{
				            dataAdapter.deleteInspectPhotoEntryByPhotoSetID(photoSetId);
				            Intent i = new Intent();
                            i.putExtra(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID,-1);
                            setResult(RESULT_OK, i);
                            finish();
				         }catch(Exception ex){
				            MessageBox.showMessage(this, R.string.message_box_title_error, "Delete data error "+ex.getMessage());
				         }
				      }
				   }
				}
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case InstanceStateKey.RESULT_LOAD_IMAGE_FROM_GALLERY:{
			if (resultCode == Activity.RESULT_OK)
			{
				Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA,
	            		MediaStore.Images.ImageColumns.LATITUDE,
	            		MediaStore.Images.ImageColumns.LONGITUDE};
	 
	            Cursor cursor = null;
	            Bitmap bitmap = null;
	            
	            try{
	            	cursor = getContentResolver().query(selectedImage,
	                    filePathColumn, null, null, null);
	            	cursor.moveToFirst();
	 
	            	int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            	String picturePath = cursor.getString(columnIndex);
	            	
	            	int latcol = cursor.getColumnIndex (filePathColumn[1]);
	            	int loncol = cursor.getColumnIndex(filePathColumn[2]);
	            	// Get the actual values returned
	            	
	            	double latval = cursor.getDouble(latcol);
	            	double lonval = cursor.getDouble(loncol);
	            	
	            	bitmap = BitmapFactory.decodeFile(picturePath);
	            	////////////////////
	            	Bitmap bmpResized = null;
	            	if (bitmap != null)
	            	{
	            	   bmpResized = ImageUtil.createBitmapScaleFollowWidth(bitmap, ImageUtil.IMG_MAX_WIDTH);
	            	}
	            	bitmap = bmpResized;
	            	/////////////////
	            	
	            	ExifInterface exfInfo = new ExifInterface(picturePath);
                    float[] latLons = new float[2];
                    exfInfo.getLatLong(latLons);
	            	latval = latLons[0];
	            	lonval = latLons[1];
	            	String dateTime = exfInfo.getAttribute(ExifInterface.TAG_DATETIME);
	            	Log.d("DEBUG_D", "Load image from gallery -> "+picturePath);
	            	Log.d("DEBUG_D", "get lat , lon from image "+latval+","+lonval);
	            	Log.d("DEBUG_D", "Date time of image -> "+dateTime);
	            	////////////////
	            	/*
	            	if ((latval <= 0)||(lonval <= 0))
	            	{
	            		if (currentLocation != null)
	            		{
	            			latval = currentLocation.getLatitude();
	            			lonval = currentLocation.getLongitude();
	            		}
	            	}*/
	            	
	            	saveBitmap(bitmap,latval,lonval,dateTime);
	            	
	            	File fileTmp = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
	                 if (fileTmp.exists())
	                 {
	                	 fileTmp.delete();
	                	 Log.d("DEBUG_D", "Deleted file -> "+fileTmp.getAbsolutePath());
	                	 
		     				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
		     						Uri.parse("file://" + Environment.getExternalStorageDirectory())));

	                 }
	            }catch(Exception ex){
	            	MessageBox.showMessage(this,
	            			getString(R.string.text_error_title), 
	            			ex.getMessage());
	            }finally{
	            	if (bitmap != null)
	            	{
	            		bitmap.recycle();
	            		bitmap = null;
	            		System.gc();
	            	}
	            	if (cursor != null)
	            	{
	            		try{
	            			cursor.close();
	            		}catch(Exception ex){}
	            	}
	            }
			}
		}break;
		case InstanceStateKey.TAKE_PICTURE:
		{
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = getImageUri();
				File f = new File(selectedImage.toString());
				boolean hasFile = f.exists();
				getContentResolver().notifyChange(selectedImage, null);
	            
				ContentResolver cr = getContentResolver();
		        Bitmap bitmap = null;
		        try {
		                 bitmap = android.provider.MediaStore.Images.Media
		                 .getBitmap(cr, selectedImage);

		           
		                 
		                 double latVal = 0;
		                 double lonVal = 0;
		              
		                 
		                 ExifInterface exfInfo = new ExifInterface(f.getPath());
		                 /*
		                 float[] latLons = new float[2];
		                 exfInfo.getLatLong(latLons);
		                 
		                 latVal = latLons[0];
		                 lonVal = latLons[1];
		                 */
		                 String takeTime = exfInfo.getAttribute(ExifInterface.TAG_DATETIME);
		                 if (currentLocation != null)
		                 {
		                	 latVal = currentLocation.getLatitude();
		                	 lonVal = currentLocation.getLongitude();
		                 }
		                 if (takeTime == null)
		                 {
		                    SimpleDateFormat fmt_Exif = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		                    takeTime = fmt_Exif.format(Calendar.getInstance().getTime());
		                 }
		                 /////////////////
		                 Bitmap bmpResized = null;
		                 if (bitmap != null)
		                 {
		                       bmpResized = ImageUtil.createBitmapScaleFollowWidth(bitmap, ImageUtil.IMG_MAX_WIDTH);
		                 }
		                 bitmap = bmpResized;
		                 /////////////////
		                 saveBitmap(bitmap,latVal,lonVal,takeTime);;
		                 
		                 File fileTmp = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
		                 if (fileTmp.exists())
		                 {
		                	 fileTmp.delete();
		                	 Log.d("DEBUG_D", "Deleted file -> "+fileTmp.getAbsolutePath());
		                	 
		     				
		     				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
		     						Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		                 }
		        } catch (Exception ex) {
		        	MessageBox.showMessage(this,
	            			getString(R.string.text_error_title), 
	            			ex.getMessage());
		        }
		        finally{
		        	if (bitmap != null){
		        	   bitmap.recycle();
		        	   bitmap = null;
		        	   System.gc();
		        	}
		        }
			}
		}
		break;
 }
	}

	private void saveBitmap(Bitmap bitmap,double lat,double lon,String takeTime) throws Exception
	{
		if (bitmap == null)return;
		/*
    	 * save to storage
    	 */	
    	 Date d = new Date();
    	 String fileName = d.getTime()+".jpg";
    	 String fullName = ImageUtil.saveImageFile(this,
    			 ImageUtil.FOLDER_NAME_PHOTO_INSPECT_ENTRY+"/"+photoSetId, 
    			 fileName, bitmap);
    	 
    	 if (bitmap != null)
    	 {
    		 bitmap.recycle();
    		 bitmap = null;
    		 System.gc();
    	 }
    	 //if (currentLocation != null)
    	 {
    	    
    	    ExifInterface exf = new ExifInterface(fullName);
    	    exf.setAttribute(/*ExifInterface.TAG_DATETIME*/"UserComment", takeTime);
            if (lat >= 0)
            {
                exf.setAttribute(ExifInterface.TAG_GPS_LATITUDE, 
                    LocationUtil.convert(lat));
                exf.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, 
                    LocationUtil.latitudeRef(lat));
            } 
            if (lon >= 0){
                exf.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, 
                        LocationUtil.convert(lon));
                exf.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, 
                        LocationUtil.longitudeRef(lon));
            }               
            exf.saveAttributes();
            
            
            Log.d("DEBUG_DD", fullName +" -> take date = "+exf.getAttribute(ExifInterface.TAG_DATETIME));
            

    	    /*
    		 ExifInterface exf = new ExifInterface(fullName);
    		 
    		 float[] latLons = new float[2];
    		 exf.getLatLong(latLons);

    		 Log.d("DEBUG_D", "save get from file lat , lon from image "+latLons[0]+","+latLons[1]);
             
    		 if ((latLons[0] == 0)&&(latLons[1] == 0))
    		 {
    		    
    		     // if exif file not containing lat and lon
    		     //
    		     // i'll use lat lon from gps module
    		     
                if (lat >= 0)
                {
                    exf.setAttribute(ExifInterface.TAG_GPS_LATITUDE, 
                        LocationUtil.convert(lat));
                    exf.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, 
                        LocationUtil.latitudeRef(lat));
                } 
                if (lon >= 0){
                    exf.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, 
                            LocationUtil.convert(lon));
                    exf.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, 
                            LocationUtil.longitudeRef(lon));
                }    		    
    		 }
    		 exf.saveAttributes();
    		 */
    	 }
    	 
    	 //if (this.jobRequestProduct == null)/**/
    	 //{
            InspectDataObjectPhotoSaved photoSaved = new InspectDataObjectPhotoSaved();
            photoSaved.setPhotoID(photoSetId);
            photoSaved.setFileName(fullName);
            InspectDataSavedSpinnerDisplay display = 
                  (InspectDataSavedSpinnerDisplay)inspectDataSavedSpinner.getSelectedItem();
            if (display != null)
            {
               if (this.jobRequestProduct != null)
               {
                  photoSaved.setCustomerSurveySiteID(this.jobRequestProduct.getcWareHouse());
                  photoSaved.setTaskCode(currentTaskCode);
                  photoSaved.setInspectDataObjectID(-1);
               }else{
                 if (display.dataSaved != null)
                 {
                    photoSaved.setCustomerSurveySiteID(display.dataSaved.getCustomerSurveySiteID());
                    photoSaved.setTaskCode(display.dataSaved.getTaskCode());
                    photoSaved.setInspectDataObjectID(display.dataSaved.getInspectDataObjectID());                    
                 }
               }
               if (jobRequest != null)
               {
                   photoSaved.setInspectTypeID(jobRequest.getInspectTypeID());
               }
            }
            inspectPhotoSavedList.add(photoSaved);    	    
    	// }    	 
    	 ////////////////////////
    	 addPhotos();
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if (inspectPhotoSavedList != null)
		{
			outState.putParcelableArrayList(KEY_SAVED_LIST, inspectPhotoSavedList);
		}
		if (inspectDataSavedList != null)
		{
			outState.putParcelableArrayList(KEY_DATA_SAVED_LIST, inspectDataSavedList);
		}
		if (this.imageUri != null)
		{
			outState.putString(SAVE_IMAGE_KEY_NAME, imageUri.toString());
		}
//		outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
	    
		super.onSaveInstanceState(outState);
		//invokeFragmentManagerNoteStateNotSaved();
		
		//int orientation =this.getDisplayOrientation();
	    //Lock the screen orientation to the current display orientation : Landscape or Potrait
	    //this.setRequestedOrientation(orientation);

	}
	
	//A method found in stackOverflow, don't remember the author, to determine the right screen orientation independently of the phone or tablet device 
	public int getDisplayOrientation(){
	    Display getOrient = getWindowManager().getDefaultDisplay();

	    int orientation = getOrient.getOrientation();

	    // Sometimes you may get undefined orientation Value is 0
	    // simple logic solves the problem compare the screen
	    // X,Y Co-ordinates and determine the Orientation in such cases
	    if(orientation==Configuration.ORIENTATION_UNDEFINED){

	        Configuration config = getResources().getConfiguration();
	        orientation = config.orientation;

	        if(orientation==Configuration.ORIENTATION_UNDEFINED){
	        //if height and widht of screen are equal then
	        // it is square orientation
	            if(getOrient.getWidth()==getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_SQUARE;
	            }
	            else{ //if widht is less than height than it is portrait
	                if(getOrient.getWidth() < getOrient.getHeight()){
	                    orientation = Configuration.ORIENTATION_PORTRAIT;
	                }else{ // if it is not any of the above it will defineitly be landscape
	                orientation = Configuration.ORIENTATION_LANDSCAPE;
	                }
	            }
	        }
	    }
	    return orientation; // return value 1 is portrait and 2 is Landscape Mode
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void invokeFragmentManagerNoteStateNotSaved() {
	    /**
	     * For post-Honeycomb devices
	     */
	    if (Build.VERSION.SDK_INT < 11) {
	        return;
	    }
	    try {
	        Class cls = getClass();
	        do {
	            cls = cls.getSuperclass();
	        } while (!"Activity".equals(cls.getSimpleName()));
	        Field fragmentMgrField = cls.getDeclaredField("mFragments");
	        fragmentMgrField.setAccessible(true);

	        Object fragmentMgr = fragmentMgrField.get(this);
	        cls = fragmentMgr.getClass();

	        Method noteStateNotSavedMethod = cls.getDeclaredMethod("noteStateNotSaved", new Class[] {});
	        noteStateNotSavedMethod.invoke(fragmentMgr, new Object[] {});
	        Log.d("DLOutState", "Successful call for noteStateNotSaved!!!");
	    } catch (Exception ex) {
	        Log.e("DLOutState", "Exception on worka FM.noteStateNotSaved", ex);
	    }
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		
		inspectPhotoSavedList = savedInstanceState.getParcelableArrayList(KEY_SAVED_LIST);
		inspectDataSavedList = savedInstanceState.getParcelableArrayList(KEY_DATA_SAVED_LIST);
		String strImageUri = savedInstanceState.getString(SAVE_IMAGE_KEY_NAME);
		if (strImageUri != null)
			this.imageUri = Uri.parse(strImageUri);
		addPhotos();
	}

	private void init() throws Exception
	{
		int photo_id = 0;
		InspectDataSavedSpinnerDisplay lastInspectDataSavedDisplay = null;
		String taskCode = "";
		int customersurveySiteId = 0;
		if (getIntent() != null){
			photo_id = getIntent().getIntExtra(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID, 0);
			lastInspectDataSavedDisplay = getIntent().getParcelableExtra(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_SELECTED_PHOTO);
			taskCode = getIntent().getStringExtra(InstanceStateKey.KEY_ARGUMENT_TASK_CODE);
			customersurveySiteId = getIntent().getIntExtra(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY_ID, -1);
			
		}
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this);		

		allInspectPhotoSavedList = dataAdapter.getInspectDataObjectPhotoSaved(taskCode, customersurveySiteId);
		
		if (photo_id > 0){
			photoSetId = photo_id;
			
			inspectPhotoSavedList = dataAdapter.getInspectDataObjectPhotoSaved(photoSetId);
			if (inspectPhotoSavedList == null)
			   inspectPhotoSavedList = new ArrayList<InspectDataObjectPhotoSaved>();
			
		}else{
			photoSetId = (dataAdapter.getMaxPhotoID())+1;
		}
		
		hScrollViewPhotoContainer = (ViewGroup)this.findViewById(R.id.inspect_photo_entry_picture_container);
		productListContainer = (ViewGroup)this.findViewById(R.id.ll_products_details_list_container);
		
		
	    inspectDataSavedSpinner = 
		(InspectDataSavedSpinner)this.findViewById(R.id.sp_inspect_data_saved);
	    
	    if (jobRequestProduct != null){
	       /*
	        *   show list for car
	        */
	       if (jobRequest.getInspectType().getInspectTypeID() == InspectServiceSupportUtil.SERVICE_CAR_INSPECT){
	             inspectDataSavedSpinner.initial(
	                   jobRequestProduct,
	                   taskCode,
	                   customersurveySiteId);
	       }else{
	          /*universal display*/
	          inspectDataSavedSpinner.initialUniversal(
                    jobRequestProduct,
                    taskCode,
                    customersurveySiteId);
	       }
	    }else{
	       inspectDataSavedSpinner.initial(inspectDataSavedList,
	          lastInspectDataSavedDisplay,
	          taskCode,
	          customersurveySiteId);
	    }
	    
	    /*
	    if ((inspectPhotoSavedList != null)&&(inspectPhotoSavedList.size() > 0)){
	    	InspectDataObjectPhotoSaved savedPhoto =  inspectPhotoSavedList.get(0);
	    	for(int i = 0; i < inspectDataSavedSpinner.getAdapter().getCount();i++)
	    	{
	    		String text = inspectDataSavedSpinner.getAdapter().getItem(i).toString();
	    		if (text.equalsIgnoreCase(savedPhoto.getInspectDataTextSelected())){
	    			inspectDataSavedSpinner.setSelection(i);
	    			break;
	    		}
	    	}
	    }*/
	    
	    Button btnAddProduct = (Button)this.findViewById(R.id.btn_add_products_detail);
	    btnAddProduct.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				tv.setText("test");
				if (currentImageView == null)
					{
					MessageBox.showMessage(InspectPhotoEntryActivity.this, R.string.text_error_title, R.string.text_error_not_add_photo);
					return;
					}
				
				if (inspectDataSavedSpinner != null)
				{
					if (productListContainer != null)
					{
						TextView tv = new TextView(InspectPhotoEntryActivity.this);
						tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
						tv.setText("\t-"+inspectDataSavedSpinner.getSelectedItem()+"");
						tv.setTextSize(20);
						tv.setTextColor(getResources().getColor(R.color.caldroid_darker_gray));
						tv.setTag(inspectDataSavedSpinner.getSelectedItem());
						
						///////////////////////////////////////
						((InspectDataObjectPhotoSaved)currentImageView.getTag()).addProductName(
								inspectDataSavedSpinner.getSelectedItem()+""
								);
						
						((InspectDataObjectPhotoSaved)currentImageView.getTag()).setInspectDataTextSelected(
								((InspectDataObjectPhotoSaved)currentImageView.getTag()).generateProductNamesList());
						productListContainer.addView(tv);
						registerForContextMenu(tv);
						
						/*
						    
						 */
						if (jobRequestProduct == null)/**/
						{
						   reloadProductList();
						}
					}
				}
			}
	    	
	    });
		edtComment = (EditText)this.findViewById(R.id.edt_inspect_photo_entry_comment);

		edtComment.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (currentImageView != null)
				{
					((InspectDataObjectPhotoSaved)currentImageView.getTag()).setComment(arg0.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		spnAngleDetail = (Spinner)this.findViewById(R.id.spn_angle_detail);
		String[] angleDetails = this.getResources().getStringArray(R.array.array_angle_details);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_dropdown_item, angleDetails);
		spnAngleDetail.setAdapter(spinnerArrayAdapter);
		spnAngleDetail.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				try{
					String strItem = arg0.getItemAtPosition(arg2).toString();
					if  (currentImageView != null){
						((InspectDataObjectPhotoSaved)currentImageView.getTag()).setAngleDetail(strItem);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		
	}

	private void addPhotos(){
		if (inspectPhotoSavedList != null)
		{
			int no = 0;
			if (hScrollViewPhotoContainer != null){
				hScrollViewPhotoContainer.removeAllViews();
				for(InspectDataObjectPhotoSaved photoSaved : inspectPhotoSavedList)
				{
					try{
						final ImageView imgView = new ImageView(this);	
						ImgTag imgTag = new ImgTag();
						imgTag.no = no++;
						
						//imgView.setTag(imgTag);
						LinearLayout.LayoutParams vGroup = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
						vGroup.setMargins(10, 0, 10, 0);
						imgView.setLayoutParams(vGroup);
						
						
						Bitmap resizedBmp = ImageUtil.getResizedBitmapFromFile(photoSaved.getFileName()/*must full name*/);
						
						Log.d("DEBUG_D", "show image file -> "+photoSaved.getFileName());
						
						LayerDrawable d = new LayerDrawable(new Drawable[]{
								new BitmapDrawable(resizedBmp), 
								getResources().getDrawable(R.drawable.frame_imgview_unselected)
							}
					);


						/*
						sld.addState(new int[] { android.R.attr.state_pressed }, d);
						sld.addState(StateSet.WILD_CARD, new BitmapDrawable(resizedBmp));
					     */  
						 
						imgView.setPadding(10, 10, 10, 10);
						imgView.setImageDrawable(d);
						//imgView.setImageBitmap(resizedBmp);
						
						photoSaved.setPhotoNo(imgTag.no);
						
						imgView.setTag(photoSaved);
						imgView.setLongClickable(true);
//						imgView.setRotation(90);
						imgView.setOnLongClickListener(new OnLongClickListener(){

							@Override
							public boolean onLongClick(View v) {
								// TODO Auto-generated method stub
								currentImageView = (ImageView)v;
								openContextMenu(currentImageView);
								return true;
							}
							
						});
						imgView.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								
								
								Object objTag = v.getTag();
								InspectDataObjectPhotoSaved tag = (InspectDataObjectPhotoSaved)objTag;
								int currentNo = tag.getPhotoNo();
//								v.getDrawableState()[1] = R.drawable.frame_imgview_selected;
//								v.setBackgroundResource(R.drawable.frame_imgview_selected);
								ImageView imgView = (ImageView)v;
								LayerDrawable ld = (LayerDrawable)imgView.getDrawable();	
								
								LayerDrawable newLd = new LayerDrawable(
										new Drawable[]{
												ld.getDrawable(0),
												getResources().getDrawable(R.drawable.frame_imgview_selected)												
										}

										);
								imgView.setImageDrawable(newLd);
								
								v.setPadding(10, 10, 10, 10);
								v.invalidate();
								/*
								 * 
								 */
								for(int i = 0; i < hScrollViewPhotoContainer.getChildCount();i++)
								{
									View vChild = hScrollViewPhotoContainer.getChildAt(i);
									if (vChild instanceof ImageView)
									{
										InspectDataObjectPhotoSaved imgTag = (InspectDataObjectPhotoSaved)vChild.getTag();
										if (imgTag.getPhotoNo() != currentNo)
										{
											//v.setBackgroundResource(0);/*remove*/
//											v.getDrawableState()[1] = 0;
											ImageView imgViewChild = (ImageView)vChild;
											LayerDrawable ldChild = (LayerDrawable)imgViewChild.getDrawable();
											
											
											LayerDrawable newLd_unselected  = new LayerDrawable(
													new Drawable[]{
															ldChild.getDrawable(0),
															getResources().getDrawable(R.drawable.frame_imgview_unselected)												
													}

													);
											imgViewChild.setImageDrawable(newLd_unselected);
											v.invalidate();
										}
									}
								}
								//openContextMenu(imgView);
								if (edtComment != null)
								{
									currentImageView = (ImageView)v;
									InspectDataObjectPhotoSaved obj_p_saved = (InspectDataObjectPhotoSaved)currentImageView.getTag();
									String strComment = obj_p_saved.getComment();
									edtComment.setText(strComment);
									
									
									String strAngleDetail = obj_p_saved.getAngleDetail();
									for(int i = 0; i < spnAngleDetail.getCount();i++)
									{
										String item = (String)spnAngleDetail.getAdapter().getItem(i);
										if (item.equalsIgnoreCase(strAngleDetail)){
											spnAngleDetail.setSelection(i);
											break;
										}
									}
									
									if ((strAngleDetail == null)||(strAngleDetail.isEmpty())){
										obj_p_saved.setAngleDetail((String)spnAngleDetail.getAdapter().getItem(0));
									}
								}
								if (productListContainer != null)
								{
									productListContainer.removeAllViews();
									
									InspectDataObjectPhotoSaved obj_p_saved = (InspectDataObjectPhotoSaved)currentImageView.getTag();
//									String lastProductNameList = obj_p_saved.generateProductNamesList();
//									obj_p_saved.setProductNamesList
//									ArrayList<String> tmp_ls = new ArrayList<String>(obj_p_saved.productDisplayDetailList);
									String textSelected = obj_p_saved.getInspectDataTextSelected();
									if (textSelected == null)return;
									obj_p_saved.setProductNamesList(textSelected);
									ArrayList<String> productDisplay = obj_p_saved.productDisplayDetailList;
//									productDisplay.addAll(tmp_ls);
									
									for(String display : productDisplay)
									{
										TextView tv = new TextView(InspectPhotoEntryActivity.this);
										tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
										if (display.equalsIgnoreCase(""))continue;
										
										tv.setText("\t-"+display+"");
										tv.setTextSize(20);
										tv.setTextColor(getResources().getColor(R.color.caldroid_darker_gray));
										tv.setTag(display);
										productListContainer.addView(tv);
										registerForContextMenu(tv);
									}
								}
							}
							
						});
						registerForContextMenu(imgView);
						hScrollViewPhotoContainer.addView(imgView);
						hScrollViewPhotoContainer.invalidate();
						
						//bmp.recycle();
						//bmp = null;
					}catch(Exception ex){
						/*
						 * may be have someone delete file via file manager
						 */
						ex.printStackTrace();
					}
					finally{
						
					}
				}
				/*
				if (hScrollViewPhotoContainer.getChildCount() > 0)
					registerForContextMenu(hScrollViewPhotoContainer);
				else
					unregisterForContextMenu(hScrollViewPhotoContainer);
				*/
			}
		}
		if (hScrollViewPhotoContainer != null){
			if (hScrollViewPhotoContainer.getChildCount() > 0){
				View v = hScrollViewPhotoContainer.getChildAt(0);
				v.performClick();
			}			
		}

		if (this.jobRequestProduct == null){
		   reloadProductList();
		}
	}
	public Uri getImageUri() {
		return imageUri;
	}

	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}

	@Override
	public void onLocationUpdated(Location location) {
		// TODO Auto-generated method stub
		currentLocation = location;
	}
	private void reloadProductList(){
	   /*
	    * 
        */
       ArrayList<String> productSelectedList = new ArrayList<String>();
       
       for(int i = 0; i < hScrollViewPhotoContainer.getChildCount();i++)
       {
           View vChild = hScrollViewPhotoContainer.getChildAt(i);
           if (vChild instanceof ImageView)
           {
               //if (vChild.getTag() == null)continue;
               
               InspectDataObjectPhotoSaved imgTag = (InspectDataObjectPhotoSaved)vChild.getTag();
               if (imgTag.getInspectDataTextSelected() != null){
                  imgTag.setProductNamesList(imgTag.getInspectDataTextSelected());
                  ArrayList<String> items = imgTag.getProductDisplayDetailList();
                  for(String item : items){
                     if (!item.trim().isEmpty()){
                        productSelectedList.add(item);
                     }
                  }                  
               }
           }
       }
       
       
       if (allInspectPhotoSavedList != null)
       {
          for(InspectDataObjectPhotoSaved photoSaved : allInspectPhotoSavedList)
          {
             if (photoSaved.getPhotoID() == photoSetId){
                continue;
             }
             
             photoSaved.setProductNamesList(photoSaved.getInspectDataTextSelected());
             ArrayList<String> items = photoSaved.getProductDisplayDetailList();
             for(String item : items){
                if (!item.trim().isEmpty())
                {
                   boolean duplicated = false;
                   for(String p : productSelectedList){
                      if (p.trim().equalsIgnoreCase(item.trim())){
                         duplicated = true;
                         break;
                      }
                   }
                   if (!duplicated){
                      productSelectedList.add(item);
                   }
                }
             }
          }
       }
       
       
       inspectDataSavedSpinner.filter(productSelectedList);
	}
}
