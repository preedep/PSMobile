/**
 * 
 */
package com.epro.psmobile;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.adapter.PictureGridPreviewAdapter;
import com.epro.psmobile.adapter.TeamMemberAdapter;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.EmpAssignedInTeam;
import com.epro.psmobile.data.Employee;
import com.epro.psmobile.data.LicensePlate;
import com.epro.psmobile.data.MembersInTeamHistory;
import com.epro.psmobile.data.PhotoTeamHistory;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.data.TeamCheckInHistory;
import com.epro.psmobile.data.TeamCheckInHistory.HistoryType;
import com.epro.psmobile.data.TransactionStmtHolder;
import com.epro.psmobile.dialog.InfoDialog;
import com.epro.psmobile.dialog.EmployeeSpinnerDialog;
import com.epro.psmobile.dialog.EmployeeSpinnerDialog.EmployeeDialogListener;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.location.PSMobileLocationManager;
import com.epro.psmobile.location.PSMobileLocationManager.OnPSMLoctaionListener;
import com.epro.psmobile.util.AppFolderUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.ImageUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.SysInfoGetter;
import com.epro.psmobile.util.MessageBox.MessageConfirmType;
import com.epro.psmobile.view.LicensePlateSpinner;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author thrm0006
 *
 */
public class TeamTimeAttendanceActivity extends PsBaseActivity implements EmployeeDialogListener, OnPSMLoctaionListener {

	private ListView lvTeamMember;
//	private GridView gridViewMemberPhoto;
	private Team team;
	private Uri imageUri;
	//private int photoSetId;
	
	private ViewGroup vGroupPhotoContainer;
	private ArrayList<PhotoTeamHistory> photoTeams = new ArrayList<PhotoTeamHistory>();
	private ArrayList<EmpAssignedInTeam> empAssignedInTeamList = new ArrayList<EmpAssignedInTeam>();
	private boolean isTeamCheckInFirstTime = true;
	private View currentPhotoTeamImgView;
	private Location currentLocation;
	private Task currentTask;
	
	private EditText editNumberOfMiles;
//	private EditText editCarLicenseNumber;
	
	private LicensePlateSpinner licensePlateSpinner;
	
	private TeamCheckInHistory lastCheckInHistory;
	
	private HistoryType currentHistoryType = HistoryType.NONE;
	private PSMobileLocationManager locManager = null;
	
	private final static String KEY_LAST_CHECK_IN_HISTORY = "com.epro.psmobile.TeamTimeAttendanceActivity.KEY_LAST_CHECK_IN_HISTORY";
	private final static String KEY_PHOTO_TEAM_HISTORY = "com.epro.psmobile.TeamTimeAttendanceActivity.KEY_PHOTO_TEAM_HISTORY";
	private final static String KEY_TEAM_CHECK_IN = "com.epro.psmobile.TeamTimeAttendanceActivity.KEY_TEAM_CHECK_IN";
	private final static String KEY_TASK = "com.epro.psmobile.TeamTimeAttendanceActivity.KEY_TASK";
	/**
	 * 
	 */
	public TeamTimeAttendanceActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		this.setContentView(R.layout.ps_activity_team_time_attendance);
		
		Bundle bArgument =  this.getIntent().getExtras();
		if (bArgument != null)
		{
			currentTask = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
			int checkInTypeCode = bArgument.getInt(InstanceStateKey.KEY_ARGUMENT_CHECKIN_TYPE);
			currentHistoryType = HistoryType.getType(checkInTypeCode);
			
			if (currentHistoryType != HistoryType.START_DATE_CHECKIN)
				isTeamCheckInFirstTime = false;
			
		}
		
		initialViews();
		
		
		if (currentHistoryType == HistoryType.START_DATE_CHECKIN)
		{
			if (SharedPreferenceUtil.getStateTeamCheckIn(this)){
				restoreLastHistory();				
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.time_attendance_menu, menu);
		return true;
        
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
				EmployeeSpinnerDialog ep_s_dialog = new EmployeeSpinnerDialog(this);
				/*
				 * 
				 */
				if (empAssignedInTeamList != null)
				{
					String[] empIgnoreds = new String[empAssignedInTeamList.size()+1];
					for(int i = 0; i < empAssignedInTeamList.size();i++)
					{
						empIgnoreds[0] =  empAssignedInTeamList.get(i).getEmployeeCode();
					}
					ep_s_dialog.setEmployeesIgnored(empIgnoreds);
				}
				ep_s_dialog.setOnEmployeeDialogListener(this);
				ep_s_dialog.show();
			}
			break;
			case R.id.menu_time_attendance_save:{
				if (lvTeamMember != null)
				{
					if (photoTeams != null){
						if (photoTeams.size() == 0)
						{
							MessageBox.showMessage(this,
									"Error", 
									this.getString(R.string.team_check_alert_message_no_photo));
							return false;
						}						
					}else{
						MessageBox.showMessage(this,
								"Error", 
								this.getString(R.string.team_check_alert_message_no_photo));
						return false;
					}

					if (editNumberOfMiles.getText().toString().isEmpty())
					{
						MessageBox.showMessage(this,
								"Error", 
								this.getString(R.string.team_check_alert_message_no_miles));
						return false;
					}
					final TeamMemberAdapter teamAdapter = (TeamMemberAdapter)lvTeamMember.getAdapter();
                    ArrayList<EmpAssignedInTeam>  empAssignedInTeamList = 
                                    new ArrayList<EmpAssignedInTeam>();
                    for(int i = 0; i < teamAdapter.getCount();i++)
                    {
                        EmpAssignedInTeam empInTeamItem = (EmpAssignedInTeam)teamAdapter.getItem(i);
                        empAssignedInTeamList.add(empInTeamItem);
                    }
					
                    if (empAssignedInTeamList.size() == 1){
                       
					MessageBox.showMessageWithConfirm(this, 
					      this.getString(R.string.text_warning_title), 
					      this.getString(R.string.text_warning_add_team_member),
					      new MessageBox.MessageConfirmListener() {
                           
                           @Override
                           public void onConfirmed(MessageConfirmType confirmType) {
                              // TODO Auto-generated method stub
                              if (confirmType == MessageConfirmType.OK)
                              {
                                 addTimeAttendance(teamAdapter);
                              }
                           }
                        });
                    }else if (empAssignedInTeamList.size() > 1){
                       addTimeAttendance(teamAdapter);
                    }else{
                       MessageBox.showMessage(this, R.string.text_error_title, R.string.text_error_add_team_member);
                    }
				}
				
			}break;
		}
		return super.onOptionsItemSelected(item);
	}
	   
	private void addTimeAttendance(TeamMemberAdapter teamAdapter){
       PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this);
       try {
           
           int rowEffected = 0;                        
           rowEffected = dataAdapter.updateEmpAssignedInTeams(empAssignedInTeamList);
           Log.d("DEBUG_D", "Row effected : "+rowEffected);
           if (rowEffected > 0)
           {
               int photoSetId = dataAdapter.getMaxTeamPhotoID()+1;
               //insert to history 
               TeamCheckInHistory teamCheckInHistory = new TeamCheckInHistory();
               teamCheckInHistory.setTeamID(team.getTeamID());
               teamCheckInHistory.setLastCheckInDateTime(new java.sql.Timestamp(new Date().getTime()));
               //if (currentLocation != null)
               {
                   if (currentLocation != null){
                       teamCheckInHistory.setTeamStartLatLoc(currentLocation.getLatitude());
                       teamCheckInHistory.setTeamStartLonLoc(currentLocation.getLongitude());
                   }
                   
                   if (licensePlateSpinner != null)
                       teamCheckInHistory.setCarLicenseNumber(licensePlateSpinner.getSelectedItem().toString());
                   
                   teamCheckInHistory.setNumberOfMilesAtStartPoint(this.editNumberOfMiles.getText().toString());
                   
                   
                   teamCheckInHistory.setImgTeamHistoryID(photoSetId);
                   teamCheckInHistory.setHistoryType(currentHistoryType);
                   if (currentHistoryType == HistoryType.START_INSPECT_CHECKIN){
                       if (currentTask != null)
                       {
                           teamCheckInHistory.setTaskDuplicateNo(currentTask.getTaskDuplicatedNo());
                           teamCheckInHistory.setTaskID(currentTask.getTaskID());
                           teamCheckInHistory.setTaskCode(currentTask.getTaskCode());
                       }
                   }
                   
                   rowEffected = dataAdapter.insertTeamCheckInHistory(teamCheckInHistory);
                   if (rowEffected > 0)
                   {
                       int currentTeamCheckInHistoryID = dataAdapter.getMaxTeamCheckInHistoryID(currentHistoryType);
                       ArrayList<MembersInTeamHistory> memberList = new ArrayList<MembersInTeamHistory>();
                       for(int i = 0; i < teamAdapter.getCount();i++)
                       {
                           String empId = ((EmpAssignedInTeam)teamAdapter.getItem(i)).getEmployeeCode();
                           MembersInTeamHistory member = new MembersInTeamHistory();
                           member.setEmployeeCode(empId);
                           member.setTeamCheckInHistoryID(currentTeamCheckInHistoryID);
                           memberList.add(member);
                       }
                       rowEffected = dataAdapter.insertMembersInTeamHistory(memberList);
                       if (rowEffected > 0)
                       {
                           if (photoTeams != null)
                           {
                               
                               
                               for(int i = 0; i < photoTeams.size();i++)
                               {
                                   photoTeams.get(i).setPhotoTeamCheckInHistoryId(photoSetId);
                                   photoTeams.get(i).setPhotoTeamCheckInNo(i+1);
                                   photoTeams.get(i).setPhotoTeamCheckInHistoryId(currentTeamCheckInHistoryID);
                               }
                               rowEffected = dataAdapter.insertPhotoTeamHistory(photoTeams);
                               if (rowEffected > 0)
                               {
                                   //alert show complete
                               }
                           }
                       }
                   }
               }
               
           }
           /*
            * open job
            */
           if (this.currentHistoryType == HistoryType.START_DATE_CHECKIN)
           {
               SharedPreferenceUtil.setStateTeamCheckIn(this, true);//sharepreference
               
               Calendar cal = Calendar.getInstance();
               Date dToday = cal.getTime();
               long l_today = DataUtil.getZeroTimeDate(dToday).getTime();
               SharedPreferenceUtil.setLastTimeOpenJob(this, l_today);
           }
           
           if (!isTeamCheckInFirstTime){
               this.setResult(RESULT_OK);
               this.finish();
           }else{
               this.finish();                  
           }
       } catch (Exception e) {
           // TODO Auto-generated catch block
           MessageBox.showMessage(this, "Error", e.getMessage());
       }  
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
				case InstanceStateKey.TAKE_PICTURE:
				{
					if (resultCode == Activity.RESULT_OK) {
						Uri selectedImage = getImageUri();
						getContentResolver().notifyChange(selectedImage, null);
			            
						ContentResolver cr = getContentResolver();
				        Bitmap bitmap = null;
				        try {
				                 bitmap = android.provider.MediaStore.Images.Media
				                 .getBitmap(cr, selectedImage);

				             if (bitmap != null)
				             {
				                    Bitmap bmpResized = null;
				                    if (bitmap != null)
				                    {
				                       bmpResized = ImageUtil.createBitmapScaleFollowWidth(bitmap, ImageUtil.IMG_MAX_WIDTH);
				                    }
				                    bitmap = bmpResized;
				                    /////////////////
				            	 /*
				            	  * 
					            	 * save to storage
					            	 */	
				            	 	 String root = Environment.getExternalStorageDirectory().toString();		    
				      		   
				            	 	 String imagePath = 
				            	 			 ImageUtil.FOLDER_NAME_PHOTO_TEAM_CHECK_INT+"/"+this.currentHistoryType.getCode()+"/"+DataUtil.convertDateToStringYYYYMMDD(new Date());
				            	 
				            	 	 File folderImage = new File(root+"/"+imagePath+"/");
				            	 	 if (folderImage.exists())
				            	 	 {
				            	 		 if (folderImage.isDirectory()){
				            	 			 File[] files = folderImage.listFiles();
				            	 			 for(File f  : files){
				            	 				 f.delete();//delete all in folder
				            	 			 }
				            	 		 }
				            	 	 }
				            	 	 
					            	 Date d = new Date();
					            	 String fileName = d.getTime()+".jpg";
					            	 String fullName = ImageUtil.saveImageFile(this,
					            			 imagePath
					            			 , 
					            			 fileName, bitmap);
					            	 
				            	 PhotoTeamHistory photoTeam = new PhotoTeamHistory();
				            	 //photoTeams.ad`
				            	 //photoTeam.setPhotoTeamCheckInHistoryId(photoSetId);
				            	 photoTeam.setPhotoTeamCheckInFileName(fullName);
				            	 
				            	 if (photoTeams == null){
				            		 photoTeams =  new ArrayList<PhotoTeamHistory>();
				            	 }
				            	 
				            	 photoTeams.clear();/*have 1 picture only*/
				            	 
				            	 photoTeams.add(photoTeam);
				            	 addPhotos();
				            	 
				             }
				        } catch (Exception e) {
				                //Log.e("Camera", e.toString());
				        	MessageBox.showMessage(this,"Error", e.getMessage());
				        }
				        finally{
				        	if (bitmap != null)
				        	{
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


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.lv_team_members)
		{
			android.view.MenuInflater inflater =  this.getMenuInflater();
		    inflater.inflate(R.menu.context_menu_del_team_member, menu);
		}else{
			android.view.MenuInflater inflater =  this.getMenuInflater();
		    inflater.inflate(R.menu.context_menu_del_photo, menu);			
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())
        {
        	case R.id.del_team_member:{
        		View vItem = info.targetView;
        		if (info.position > 0){        		
        			EmpAssignedInTeam empAssignedInTeam =  (EmpAssignedInTeam)vItem.getTag();
        			((TeamMemberAdapter)lvTeamMember.getAdapter()).removeEmpAssignedInTeam(empAssignedInTeam);    			
        		}
        	}
        	break;
        	case R.id.del_photo:{
        		if (currentPhotoTeamImgView != null)
        		{
           			photoTeams.remove(currentPhotoTeamImgView.getTag());           		 
        			vGroupPhotoContainer.removeView(currentPhotoTeamImgView);
        			vGroupPhotoContainer.invalidate();
        		}
        	}break;
        }
		
		return super.onContextItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see com.epro.psmobile.PsBaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (locManager == null)
			locManager = new PSMobileLocationManager(this);

		try {
			locManager.startPSMLocationListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if (isTeamCheckInFirstTime){
			initialDialogLogout();
		}
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (locManager != null)
		{
			locManager.stopRequestLocationUpdated();
			locManager = null;			
		}
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putParcelable(KEY_LAST_CHECK_IN_HISTORY, lastCheckInHistory);
		outState.putParcelableArrayList(KEY_PHOTO_TEAM_HISTORY, photoTeams);
		outState.putParcelableArrayList(KEY_TEAM_CHECK_IN, empAssignedInTeamList);
		outState.putParcelable(KEY_TASK, currentTask);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		super.onRestoreInstanceState(savedInstanceState);
		
		lastCheckInHistory = savedInstanceState.getParcelable(KEY_LAST_CHECK_IN_HISTORY);
		photoTeams = savedInstanceState.getParcelableArrayList(KEY_PHOTO_TEAM_HISTORY);
		empAssignedInTeamList = savedInstanceState.getParcelableArrayList(KEY_TEAM_CHECK_IN);
		currentTask = savedInstanceState.getParcelable(KEY_TASK);
		if (lastCheckInHistory != null)
		{
			if (editNumberOfMiles != null){
				editNumberOfMiles.setText(lastCheckInHistory.getNumberOfMilesAtStartPoint());
			}
			if (licensePlateSpinner != null){
				for(int i = 0; i < licensePlateSpinner.getAdapter().getCount();i++)
				{
					if (licensePlateSpinner.getAdapter().getItem(i).toString().equalsIgnoreCase(lastCheckInHistory.getCarLicenseNumber()))
					{
						licensePlateSpinner.setSelection(i);
						break;
					}
				}
//				editCarLicenseNumber.setText(lastCheckInHistory.getCarLicenseNumber());
			}
		}
		
		if (photoTeams != null)
				addPhotos();
		
		bindEmpsInTeam();
	}

	private void initialDialogLogout(){
		if (SharedPreferenceUtil.checkTeamAlreadyCheckIn(this))
		{
			final InfoDialog appInfoDlg = InfoDialog.newInstance(R.string.label_simple_dlg_title_log_out, 
					new Date(),
					this.getString(R.string.label_logout_message), 
					R.string.label_simple_dlg_log_out_ok, 
					R.string.label_simple_dlg_btn_cancel,true);
			
			appInfoDlg.setOnAppInfoDlgListener(new InfoDialog.OnAppInfoDialogListener() {
				
				@Override
				public void onClickOk() {
					// TODO Auto-generated method stub
					//logout
					/*
					 * update logout time
					 */
								
					try{
						PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(TeamTimeAttendanceActivity.this);
						int currentTeamCheckInHistoryID = dataAdapter.getMaxTeamCheckInHistoryID(currentHistoryType);
					
						int licensePlateId = 0;
						String licensePlateNo = "";
						String numberOfMilesAtEndPoint= "";
						if (appInfoDlg.getLicensePlate() != null)
						{
							LicensePlate licensePlate = (LicensePlate)appInfoDlg.getLicensePlate().getSelectedItem();
							if (licensePlate != null)
							{
								licensePlateId = licensePlate.getLicensePlateID();
								licensePlateNo = licensePlate.getLicensePlate();
							}
							numberOfMilesAtEndPoint = appInfoDlg.getCarMileEndValue();
						}
						/*
						 * public int updateLastTeamCheckOutDateTimeDay(java.sql.Timestamp currentTimestamp,
							String numberOfMilesAtEndPoint,
							String carLicenseNumber,
							int licensePlateId,
							int teamCheckInHistoryID)
						 */
						int rowEffected = dataAdapter.updateLastTeamCheckOutDateTimeDay(
							new java.sql.Timestamp(new Date().getTime()), 
							numberOfMilesAtEndPoint,
							licensePlateNo,
							licensePlateId,
							currentTeamCheckInHistoryID);
					
						if (rowEffected > 0){
							SharedPreferenceUtil.setStateTeamCheckIn(TeamTimeAttendanceActivity.this, false);
							TeamTimeAttendanceActivity.this.finish();
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				
				@Override
				public void onClickCancel() {
					// TODO Auto-generated method stub
					
				}
			});
			appInfoDlg.show(this.getSupportFragmentManager(), "appInfoDlg");
			
		}
	}
	private void initialViews(){
		

		//83cc7d16-d60e-31cc-a9ef-094770ebebc0
		//String deviceId = SysInfoGetter.getDeviceID(this);
		//Log.d("DEBUG_D", "DeviceID = "+deviceId);
		//83cc7d16-d60e-31cc-a9ef-094770ebebc0
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this);
		try {
			team = dataAdapter.getTeamByDeviceId(SharedPreferenceUtil.getDeviceId(this));
			if (team != null)
			{
				/*
				int photo_id = 0;
				
				if (getIntent() != null){
					photo_id = getIntent().getIntExtra(InstanceStateKey.KEY_ARGUMENT_TEAM_PHOTOS_ID, 0);
				}				
				if (photo_id > 0){
					photoSetId = photo_id;
					
					//inspectPhotoSavedList = dataAdapter.getInspectDataObjectPhotoSaved(photoSetId);
				}else{
					photoSetId = (dataAdapter.getMaxTeamPhotoID())+1;
				}*/

				
				
				TextView tvTeamName = (TextView)this.findViewById(R.id.tv_team_zone);
				TextView tvTeamAddress = (TextView)this.findViewById(R.id.tv_team_address);
				
				
				tvTeamName.setText(team.getTeamName());

				String teamAddress = "";
				String teamMobileNo = "";
				
				if ((team.getTeamAddress() != null)&&(!team.getTeamAddress().equalsIgnoreCase("null"))){
					teamAddress = team.getTeamAddress();
				}

				if ((team.getTeamMobileNo() != null)&&(!team.getTeamMobileNo().equalsIgnoreCase("null"))){
					teamMobileNo = team.getTeamMobileNo();
				}

				if (teamAddress.isEmpty() && teamMobileNo.isEmpty()){
					tvTeamAddress.setVisibility(View.GONE);
				}
				tvTeamAddress.setText(
							teamAddress+", "+teamMobileNo
						);
				/*
				if (currentHistoryType == HistoryType.START_DATE_CHECKIN)
				{
					empAssignedInTeamList = dataAdapter.getAllEmpAssignedInTeamEx(team.getTeamID());
				}else{
					ArrayList<EmpAssignedInTeam> empTempList = dataAdapter.getAllEmpAssignedInTeamEx(team.getTeamID());
					
					for(EmpAssignedInTeam empInTeam : empTempList)
					{
						if (empInTeam.isTeamLeader())
						{
							if (empAssignedInTeamList == null)
								empAssignedInTeamList = new ArrayList<EmpAssignedInTeam>();
								
							empAssignedInTeamList.add(empInTeam);						
						}
					}					
				}*/
				
				ArrayList<EmpAssignedInTeam> empTempList = dataAdapter.getAllEmpAssignedInTeamEx(team.getTeamID());
				
				for(EmpAssignedInTeam empInTeam : empTempList)
				{
					if (empInTeam.isTeamLeader())
					{
						if (empAssignedInTeamList == null)
							empAssignedInTeamList = new ArrayList<EmpAssignedInTeam>();
							
						empAssignedInTeamList.add(empInTeam);	
						break;
					}
				}			
				
				lvTeamMember = (ListView)this.findViewById(R.id.lv_team_members);
				
				lvTeamMember.setOnTouchListener(new ListView.OnTouchListener() {
			        @Override
			        public boolean onTouch(View v, MotionEvent event) {
			            int action = event.getAction();
			            switch (action) {
			            case MotionEvent.ACTION_DOWN:
			                // Disallow ScrollView to intercept touch events.
			                v.getParent().requestDisallowInterceptTouchEvent(true);
			                break;

			            case MotionEvent.ACTION_UP:
			                // Allow ScrollView to intercept touch events.
			                v.getParent().requestDisallowInterceptTouchEvent(false);
			                break;
			            }

			            // Handle ListView touch events.
			            v.onTouchEvent(event);
			            return true;
			        }
			    });
				
				/*
				gridViewMemberPhoto = (GridView)this.findViewById(R.id.gv_time_attendance_member_photos);
				
				PictureGridPreviewAdapter photoAdapter = new PictureGridPreviewAdapter(this,new ArrayList<Bitmap>());
				gridViewMemberPhoto.setAdapter(photoAdapter);
				*/
				vGroupPhotoContainer = (ViewGroup)this.findViewById(R.id.time_attendance_member_photos_container);
				
				
				TeamMemberAdapter teamAdapter = new TeamMemberAdapter(this,team,empAssignedInTeamList);
				lvTeamMember.setAdapter(teamAdapter);
				
				
				this.editNumberOfMiles = (EditText)this.findViewById(R.id.et_team_car_mile);
				this.licensePlateSpinner = (LicensePlateSpinner)this.findViewById(R.id.sp_license_late);
				this.licensePlateSpinner.initial();
				
				for(int i = 0; i < this.licensePlateSpinner.getAdapter().getCount();i++)
				{
					LicensePlate licensePlate  = (LicensePlate)this.licensePlateSpinner.getAdapter().getItem(i);
					if (licensePlate.getLicensePlateID() == team.getLicensePlateId()){
						this.licensePlateSpinner.setSelection(i);
						break;
					}
				}
				
				registerForContextMenu(lvTeamMember);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void ready(Employee employee) {
		// TODO Auto-generated method stub
		if (lvTeamMember != null)
		{
			if (lvTeamMember.getAdapter() instanceof TeamMemberAdapter){
				if (team != null)
				{
					EmpAssignedInTeam empAssignedInTeam = new EmpAssignedInTeam();
					empAssignedInTeam.setEmployee(employee);
					empAssignedInTeam.setTeam(team);
					
//					empAssignedInTeamList.add(empAssignedInTeam);
					
					/*
					 empAssignedInTeamList added in addNewEmpAssignedInTeam
					 */
					((TeamMemberAdapter)lvTeamMember.getAdapter()).addNewEmpAssignedInTeam(
							empAssignedInTeam
							);
					
					
					//lvTeamMember.requestLayout();
					
				}
			}
		}
	}

	@Override
	public void cancelled() {
		// TODO Auto-generated method stub
		
	}
	private void bindEmpsInTeam(){
		
		if (lvTeamMember != null){
			TeamMemberAdapter teamAdapter = new TeamMemberAdapter(this,team,empAssignedInTeamList);
			lvTeamMember.setAdapter(teamAdapter);
		}
		
	}
	private void addPhotos(){
		if (vGroupPhotoContainer != null)
		{
			vGroupPhotoContainer.removeAllViews();
			for(PhotoTeamHistory photoTeam : photoTeams)
			{

				try{
				final ImageView imgView = new ImageView(this);

				LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParam.setMargins(10, 0, 10, 0);
				
				Bitmap resizedBmp = ImageUtil.getResizedBitmapFromFile(
						photoTeam.getPhotoTeamCheckInFileName()/*must full name*/);			
				//Bitmap resizedBmp = ImageUtil.getResizedBitmapScale(bmp, 320,240);
				
				imgView.setImageBitmap(resizedBmp);
				
				imgView.setTag(photoTeam);
				
				imgView.setLayoutParams(layoutParam);
				imgView.setImageBitmap(resizedBmp);
				
				imgView.setLongClickable(true);
				registerForContextMenu(imgView);
				imgView.setOnLongClickListener(new OnLongClickListener(){

					@Override
					public boolean onLongClick(View arg0) {
						// TODO Auto-generated method stub
						currentPhotoTeamImgView = arg0;
						openContextMenu(arg0);						
						return true;
					}
					
				});
					imgView.invalidate();
					imgView.requestLayout();
					vGroupPhotoContainer.addView(imgView);
					vGroupPhotoContainer.invalidate();
					
					//bmp.recycle();
					//bmp = null;
					System.gc();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			vGroupPhotoContainer.requestLayout();
		}
	}
	public Uri getImageUri() {
		return imageUri;
	}

	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}
	
	private void restoreLastHistory()
	{
		try{
			PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this);
			lastCheckInHistory = dataAdapter.getLastTeamCheckInHistory();
			if (lastCheckInHistory != null)
			{
				
				if (editNumberOfMiles != null){
					editNumberOfMiles.setText(lastCheckInHistory.getNumberOfMilesAtStartPoint());
				}
				/*
				if (licensePlateSpinner != null){
					editCarLicenseNumber.setText(lastCheckInHistory.getCarLicenseNumber());
				}*/
				if (licensePlateSpinner != null){
					for(int i = 0; i < licensePlateSpinner.getAdapter().getCount();i++)
					{
						if (licensePlateSpinner.getAdapter().getItem(i).toString().equalsIgnoreCase(lastCheckInHistory.getCarLicenseNumber()))
						{
							licensePlateSpinner.setSelection(i);
							break;
						}
					}
//					editCarLicenseNumber.setText(lastCheckInHistory.getCarLicenseNumber());
				}
				
				photoTeams = dataAdapter.getPhotoTeamHistoryByImgTeamHistoryID(
																lastCheckInHistory.getImgTeamHistoryID());
				addPhotos();
				
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void onLocationUpdated(Location location) {
		// TODO Auto-generated method stub
		currentLocation = location;
		Log.d("DEBUG_D", "Location updated! lat = "+location.getLatitude()+" , lon = "+location.getLongitude());
	}

}
