package com.epro.psmobile;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.caldroid.CaldroidFragment;

import com.epro.psmobile.da.DataMasterBuilder;
import com.epro.psmobile.da.DataMasterHelper;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.da.PSBOManager;
import com.epro.psmobile.da.PSBODataAdapter.WhereInStatus;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.data.TeamCheckInHistory.HistoryType;
import com.epro.psmobile.dialog.InfoDialog;
import com.epro.psmobile.dialog.InfoDialog.OnAppInfoDialogListener;
import com.epro.psmobile.dialog.SyncDialog;
import com.epro.psmobile.dialog.TeamLoginDialog.OnLoginInterface;
import com.epro.psmobile.fragment.ActionBarInstaller;
import com.epro.psmobile.fragment.ContentViewFragment;
import com.epro.psmobile.fragment.DashboardCalendarFragment;
import com.epro.psmobile.fragment.DrawingInspectFragment;
import com.epro.psmobile.fragment.ExpenseFragment;
import com.epro.psmobile.fragment.CommonListMenuFragment;
import com.epro.psmobile.fragment.JobRequestPlanFragment;
import com.epro.psmobile.fragment.NewsFragment;
import com.epro.psmobile.fragment.ReportFragment;
import com.epro.psmobile.fragment.TaskListFragment;
import com.epro.psmobile.fragment.CommonListMenuFragment.MenuGroupType;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.location.PSMobileLocationManager;
import com.epro.psmobile.location.PSMobileLocationManager.OnPSMLoctaionListener;
import com.epro.psmobile.remote.api.Result;
import com.epro.psmobile.util.ActivityUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.FontUtil;
import com.epro.psmobile.util.MathEval;
import com.epro.psmobile.util.FontUtil.FontName;
import com.epro.psmobile.util.MessageBox.MessageConfirmType;
import com.epro.psmobile.util.SysInfoGetter;
import com.epro.psmobile.view.InspecItemViewCreator;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


public class PsMainActivity extends /*PsLayoutBaseActivity*/PsBaseActivity implements OnPSMLoctaionListener  {

	private static final String SELETED_TAB_INDEX = "tabIndex";

	/*
	 http://stackoverflow.com/questions/9451917/double-fragment-rotating-android-with-actionbar/13996054#13996054
	 */
	private MenuItem teamMemberCheckIn;
	private MenuItem dashBoardView;
	private ActionBar actionBar;
	private PSMobileLocationManager locManager;
	private SyncDialog syncDialog;
	private InfoDialog infoDialog;
	
	public PSMobileLocationManager getLocationManager(){
	   return locManager;
	}
	private static boolean showAlertNotLogout = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.ps_activity_main);
		//getWindow().setFeatureInt( Window.FEATURE_CUSTOM_TITLE, R.layout.ps_custom_view_title);
		/*
		 * clear all flag
		 * if application close from error
		 */
        SharedPreferenceUtil.saveCarInspectDataModified(getApplicationContext(), false);
        SharedPreferenceUtil.setAlreadyCommentSaved(getApplicationContext(), true);
        SharedPreferenceUtil.saveLayoutModified(getApplicationContext(), false);

		//super.onCreate( savedInstanceState );
		
		//getOverflowMenu();
		
		/* dump datas
		 */
		if (BuildConfig.DEBUG)
		{
			File dbPath =  Environment.getExternalStorageDirectory();
			DataMasterBuilder.dumpAppDB(this, dbPath.getAbsolutePath());
		}
		SharedPreferenceUtil.saveLayoutModified(this, false);/*reset flag*/

		/*
		MathEval mathEval = new MathEval();
		mathEval.setVariable("d", 1);
		mathEval.setVariable("w", 2);
		mathEval.setVariable("h", 3);
		double ret = mathEval.evaluate("((w-d)*d*h/2)+((d*d*h*0.785)/3)");
		*/
		
		actionBar = ActionBarInstaller.installActionBar(this);
        
		String deviceID = SysInfoGetter.getDeviceID(this);
		Log.d("DEBUG_D", "Device id = "+deviceID);
		Log.d("DEBUG_D", "short id ="+SysInfoGetter.getDeviceIDShort());
		Log.d("DEBUG_D", "Device unique preference id = "+SysInfoGetter.getPreferenceUniqueID(this));
		
		//this.getPSBOManager().loadDataTest();
		/*
		Result r = this.getPSBOManager().doLogin(SysInfoGetter.getDeviceIDShort(), "root", "test");
		
		if (!r.isbResult()){
			Log.d("DEBUG_D", r.getErrorMessage());
		}else{
			Log.d("DEBUG_D", "Login successful.");
		}*/
		
	
	
/*
		PSBODataAdapter d = PSBODataAdapter.getDataAdapter(this);
		try {
			ArrayList<Task> taskList =  d.getAllTasks();
			int xxx = 0;
			xxx++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//getOverflowMenu();
	}
	/*
	  private void getOverflowMenu() {

	     try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	  }*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main_menu, menu);
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);	 
	    
	    for(int i = 0; i < menu.size();i++)
	    {
	    	if (menu.getItem(i).getItemId() == R.id.menu_social_bb_cc){
	    		teamMemberCheckIn = menu.getItem(i);
	    		setupTeamCheckInMenuItem();
	    	}else if (menu.getItem(i).getItemId() == R.id.menu_view_as_grid){
	    		dashBoardView = menu.getItem(i);
//	    		restoreDashboardView();
	    		dashBoardView.setVisible(false);
	    	}
	    }
	    /*
	    int tabIdx = actionBar.getNavigationItemCount();
	    if ((tabIdx == 1)||(tabIdx == 2)){
	    	if (AppStateUtil.checkJobAndTaskShownInListView(this))
	    	{
				dashBoardView.setIcon(R.drawable.ic_collections_sort_by_size);	    		
	    	}else{
				dashBoardView.setIcon(R.drawable.ic_collections_view_as_grid);	    		
	    	}	    
	    }*/
	    /*
	    if (AppStateUtil.checkDashboardIsListView(this))
		{
			dashBoardView.setIcon(R.drawable.ic_collections_view_as_grid);
		}else{
			dashBoardView.setIcon(R.drawable.ic_collections_sort_by_size);
		}*/
	    
	    return super.onCreateOptionsMenu(menu);
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPrepareOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (dashBoardView != null)
		{
			if (actionBar != null)
			{
				int tabIdx = actionBar.getSelectedNavigationIndex();
			    if ((tabIdx == 0)){
			    	dashBoardView.setVisible(false);
			    }
			    else if ((tabIdx == 1)||(tabIdx == 2))
			    {
			    	dashBoardView.setVisible(true);
			    	if (SharedPreferenceUtil.checkJobAndTaskShownInListView(this))
			    	{
			    		dashBoardView.setIcon(R.drawable.ic_collections_view_as_grid);	
			    	}else{
						dashBoardView.setIcon(R.drawable.ic_collections_sort_by_size);	    							    		
			    	}	    
			    }				
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		Fragment fragment =	getSupportFragmentManager().findFragmentById(R.id.content_frag);
		if (fragment != null)
			fragment.onActivityResult(arg0, arg1, arg2);
		//super.onActivityResult(arg0, arg1, arg2);
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		     case R.id.action_about:{
		      String message = 
		            this.getString(R.string.text_msg_about);
		      message += "  "+SharedPreferenceUtil.getTextVersion(this);
		      MessageBox.showMessage(this,"", message);
		      
		     }
		     break;
			case R.id.menu_refresh:{
				/*
				 * sync
				 */
			    /*
			     * check 
			     */
			    PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this);
			    try 
			    {
			       ArrayList<Task> tasks = dataAdapter.getAllTasks(WhereInStatus.TO_DO_TASK);
			       boolean hasTaskInspectPending = false;
			       if (tasks != null){
	                   for(Task t : tasks)
	                   {
	                      if (t.getTaskStatus() == TaskStatus.LOCAL_SAVED){
	                         hasTaskInspectPending = true;
	                         break;
	                      }
	                   }			          
			       }
			       if (hasTaskInspectPending)
			       {
			          MessageBox.showMessageWithConfirm(this,
			                this.getString(R.string.text_warning_title), 
			                this.getString(R.string.text_warning_sync), new MessageBox.MessageConfirmListener() {
                              
                              @Override
                              public void onConfirmed(MessageConfirmType confirmType) {
                                 // TODO Auto-generated method stub
                                 if (confirmType == MessageConfirmType.OK)
                                 {
                                    showSyncDialog();
                                 }
                              }
                           });
			       }else{
			          showSyncDialog();
			       }
			    }
			    catch (Exception e) {
			       // TODO Auto-generated catch block
			       //e.printStackTrace();
			       MessageBox.showMessage(this, R.string.text_error_title, e.getMessage());
			    }   
			    

			}
			break;
			case R.id.menu_social_bb_cc:{
				teamMemberCheckIn = item;
				
				Bundle bArgument = new Bundle();
				bArgument.putInt(InstanceStateKey.KEY_ARGUMENT_CHECKIN_TYPE,
						HistoryType.START_DATE_CHECKIN.getCode());
				
				ActivityUtil.startNewActivity(
						this, 
						TeamTimeAttendanceActivity.class,
						bArgument);
			}
			break;
			case R.id.menu_view_as_grid:{
				restoreDashboardView();
			}break;
			case R.id.menu_account:{
				/*
				 * show alert confirm message
				 */
				 if (infoDialog != null)
				 {
					 infoDialog.dismiss();
					 infoDialog = null;
					 System.gc();
				 }
				 
				 infoDialog = InfoDialog.newInstance(R.string.text_title_logout, 
						Calendar.getInstance().getTime(), 
						this.getString(R.string.text_message_logout), 
						R.string.text_logout_ok, 
						R.string.text_logout_cancel,
						false);
				infoDialog.setOnAppInfoDlgListener(new OnAppInfoDialogListener(){

					@Override
					public void onClickOk() {
						// TODO Auto-generated method stub
						PSBOManager dbMgn = new PSBOManager(PsMainActivity.this);
						dbMgn.logout();
						//dbMgn.loadDataTest();
						
						
						 SharedPreferenceUtil.setHomeShownInListView(PsMainActivity.this, false);//home show in calendar first time
						 SharedPreferenceUtil.setJobAndTaskInListView(PsMainActivity.this, true);//job and task show in list default first time
						 
						 SharedPreferenceUtil.setStateTeamCheckIn(PsMainActivity.this, false);

						 
						 /*PsMainActivity.this.onRestart();
						 
						 Intent i = new Intent(PsMainActivity.this, PsMainActivity.class);  //your class
						 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						 PsMainActivity.this.startActivity(i);
						 */
						 PsMainActivity.this.onResume();
					}

					@Override
					public void onClickCancel() {
						// TODO Auto-generated method stub
						
					}
					
				});
				
				if (!infoDialog.isVisible())
					infoDialog.show(getSupportFragmentManager(), "InfoDialog");
				 
			}break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void showSyncDialog(){
       if (syncDialog != null)
       {
           syncDialog.dismiss();
           syncDialog = null;
           System.gc();
       }
       
       if (syncDialog == null)
           syncDialog = SyncDialog.newInstance();
       
       if (!syncDialog.isVisible()){
           syncDialog.setCancelable(false);
           syncDialog.setOnSyncDialogInterface(new SyncDialog.OnSyncDialogInterface() {
               
               @Override
               public void onDialogDismiss() {
                   // TODO Auto-generated method stub
                  /*
                    PsMainActivity.this.finish();  
                    Intent intent1 = new Intent(PsMainActivity.this,PsMainActivity.class);  
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);                            */
                  
                  Intent intent = PsMainActivity.this.getIntent();
                  PsMainActivity.this.finish();  
                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(intent);
               }
           });
           syncDialog.show(getSupportFragmentManager(), 
                   SyncDialog.class.getName());
       }
	}
	/* (non-Javadoc)
	 * @see com.epro.psmobile.PsBaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		setupTeamCheckInMenuItem();
		
		if (locManager == null)
			locManager = new PSMobileLocationManager(this);
		
		try {
		  
			locManager.startPSMLocationListener(this);
			//locManager.requestLocationUpdated();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (getLogin() != null)
		{
			getLogin().setOnLoginInterface(new OnLoginInterface(){

			@Override
			public void onLoginCompleted() {
				// TODO Auto-generated method stub
			   
			     Intent intent = PsMainActivity.this.getIntent();
				 PsMainActivity.this.finish();  
//			     Intent intent1 = new Intent(PsMainActivity.this,PsMainActivity.class);  
//				 intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
				 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			     startActivity(intent); 
			}
		  });
		}
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/*
		if (locManager != null)
		{
			locManager.stopRequestLocationUpdated();
			locManager = null;
		}*/
		
	}
	/* (non-Javadoc)
    * @see com.actionbarsherlock.app.SherlockFragmentActivity#onDestroy()
    */
   @Override
   protected void onDestroy() {
      // TODO Auto-generated method stub
      super.onDestroy();
//      this.getIntent().getExtras().
   }
   @Override
   protected void onNewIntent(Intent intent) {
       super.onNewIntent(intent);
       // getIntent() should always return the most recent
       Log.d("DEBUG_X_X_X", "onNewIntent");
       try{
          if (getIntent() != null){
             Log.d("DEBUG_X_X_X", "has intent");
             
             ArrayList<String> keys = new ArrayList<String>(getIntent().getExtras().keySet());
             
             for (String key : keys) {
                Object value = getIntent().getExtras().get(key);
                if (value != null){
                   Log.d("DEBUG_X_X_X", String.format("%s %s (%s)", key,  
                         value.toString(), value.getClass().getName()));
                }
                getIntent().getExtras().remove(key);
            }                      
          }
       }catch(Exception ex){
          ex.printStackTrace();
       }

       
       setIntent(intent);
   }
   /* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		
		  // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        	if (actionBar != null)
        	{
        		Fragment fContent = this.getSupportFragmentManager().findFragmentById(R.id.content_frag);
            	if (fContent instanceof DrawingInspectFragment)
            	{
            		DrawingInspectFragment drawingInspectFragment = (DrawingInspectFragment)fContent;
            		if (!drawingInspectFragment.isShowFullScreen)
            		{
                    	Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
                    	if (f != null){
                    		FragmentTransaction ft =  this.getSupportFragmentManager().beginTransaction();
                    		ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    		ft.show(f);
                    		ft.commit();
                    	}
            		}
            	}else{
            		if (!actionBar.isShowing())
            			actionBar.show();
            		
            		
                	Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
                	if (f != null){
                		FragmentTransaction ft =  this.getSupportFragmentManager().beginTransaction();
                		ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                		ft.show(f);
                		ft.commit();
                	}

            	}
        	}
        	
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
        	if (actionBar != null)
        	{
            	Fragment fContent = this.getSupportFragmentManager().findFragmentById(R.id.content_frag);
            	if (fContent instanceof DrawingInspectFragment)
            	{
            		DrawingInspectFragment drawingInspectFragment = (DrawingInspectFragment)fContent;
            	}else{
            		if (!actionBar.isShowing())
            			actionBar.show();
            	}
        	}
        	Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
        	if (f != null){
        		FragmentTransaction ft =  this.getSupportFragmentManager().beginTransaction();
        		ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        		ft.hide(f);
        		ft.commit();
        	}
        }
        this.invalidateOptionsMenu();
	}
	private void setupTeamCheckInMenuItem(){
		if (SharedPreferenceUtil.checkTeamAlreadyCheckIn(this))
		{
			//change option menu icon
		   
		    long l_previousOpenJob = SharedPreferenceUtil.getLastTimeOpenJob(this);
		    if (l_previousOpenJob > 0)
		    {
		       Calendar c = Calendar.getInstance();
		       Date today = DataUtil.getZeroTimeDate(c.getTime());
		       Date previous = new Date(l_previousOpenJob);
		    
		       if (today.compareTo(previous) != 0)
		       {
		       /*
		        *
		        * 
		        */
		        if (!showAlertNotLogout)
		        {
	                MessageBox.showMessageWithConfirm(this, 
	                      this.getString(R.string.text_error_title),
	                      this.getString(R.string.text_alert_not_logout), new MessageBox.MessageConfirmListener() {
	                        
	                        @Override
	                        public void onConfirmed(MessageConfirmType confirmType) {
	                           // TODO Auto-generated method stub
	                           if (confirmType == MessageConfirmType.OK){
	                              onOptionsItemSelected(teamMemberCheckIn);                    
	                           }
	                           showAlertNotLogout = false;/*reset*/
	                        }
	                     },true);
	                showAlertNotLogout = true; /*mark show a time*/
		        }
		       }else{
		          if (teamMemberCheckIn != null)
		          {
		             teamMemberCheckIn.setIcon(R.drawable.ic_active_cc_bcc);
		          }
		       }
		    }
		}else{
			if (teamMemberCheckIn != null)
			{
				teamMemberCheckIn.setIcon(R.drawable.ic_social_cc_bcc);
			}			
		}
	}
	private void restoreDashboardView(){

		FragmentManager fm = this.getSupportFragmentManager();
		fm.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
		
//		if (f != null){
			FragmentTransaction ft = fm.beginTransaction();
			
			int tabIdx = actionBar.getSelectedNavigationIndex();

    		//DashboardCalendarFragment calendarFragment = null;
    	    Fragment f = null;
    	    String tagName = null;
				if (tabIdx == 0)
				{
					if (SharedPreferenceUtil.checkHomeShownInListView(this))
					{
						f = new JobRequestPlanFragment();
						tagName = JobRequestPlanFragment.class.getName();
						//ft.replace(R.id.content_frag, jobRequestPlanFragment,JobRequestPlanFragment.class.getName());
						//ft.addToBackStack(JobRequestPlanFragment.class.getName());
						//dashBoardView.setIcon(R.drawable.ic_collections_view_as_grid);
						
						//AppStateUtil.setHomeShownInListView(this, false);//set false for next is calendar

					}else{
	        			f = DashboardCalendarFragment.newInstance(WhereInStatus.NONE);
	        			tagName = DashboardCalendarFragment.class.getName();
	        			
						//dashBoardView.setIcon(R.drawable.ic_collections_sort_by_size);
						
						//AppStateUtil.setHomeShownInListView(this, true);

					}
				}else if (tabIdx == 1)
				{
					if (SharedPreferenceUtil.checkJobAndTaskShownInListView(this))
					{
	        			f = DashboardCalendarFragment.newInstance(WhereInStatus.DO_PLAN_TASK);
	        			tagName = DashboardCalendarFragment.class.getName();

						//ft.replace(R.id.content_frag, jobRequestPlanFragment,JobRequestPlanFragment.class.getName());
						//ft.addToBackStack(JobRequestPlanFragment.class.getName());
						//dashBoardView.setIcon(R.drawable.ic_collections_view_as_grid);
						//AppStateUtil.setJobAndTaskInListView(this, false);
					}else{
						f  = new JobRequestPlanFragment();
						tagName = JobRequestPlanFragment.class.getName();
	        			
						//dashBoardView.setIcon(R.drawable.ic_collections_sort_by_size);
						//AppStateUtil.setJobAndTaskInListView(this, true);						
					}
				}else if (tabIdx == 2)
				{
					
					if (SharedPreferenceUtil.checkJobAndTaskShownInListView(this))
					{
	        			f = DashboardCalendarFragment.newInstance(WhereInStatus.TO_DO_TASK);
	        			tagName = DashboardCalendarFragment.class.getName();

						//ft.replace(R.id.content_frag, taskListFragment,TaskListFragment.class.getName());
						//ft.addToBackStack(TaskListFragment.class.getName());
						
						//dashBoardView.setIcon(R.drawable.ic_collections_view_as_grid);

						//AppStateUtil.setJobAndTaskInListView(this, false);
					}else{
						f = new TaskListFragment();
						tagName = TaskListFragment.class.getName();
	        			
						//dashBoardView.setIcon(R.drawable.ic_collections_sort_by_size);

						//AppStateUtil.setJobAndTaskInListView(this, true);						
					}
					
				}	
				
			if (f != null)
			{
				ft.replace(R.id.content_frag, f, tagName);
				ft.addToBackStack(tagName);	
				if ((tabIdx == 1)||(tabIdx == 2))
				{
					if (SharedPreferenceUtil.checkJobAndTaskShownInListView(this)){
						dashBoardView.setIcon(R.drawable.ic_collections_sort_by_size);
						SharedPreferenceUtil.setJobAndTaskInListView(this, false);						
					}else{
						dashBoardView.setIcon(R.drawable.ic_collections_view_as_grid);
						SharedPreferenceUtil.setJobAndTaskInListView(this, true);												
					}
				}
			}
			ft.commit();
			reloadLeftMenu();
//		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		if (actionBar != null)
			outState.putInt(SELETED_TAB_INDEX, actionBar.getSelectedTab().getPosition());
	       
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (savedInstanceState != null) {
			if (actionBar != null){
	            int index = savedInstanceState.getInt(SELETED_TAB_INDEX);
	            actionBar.setSelectedNavigationItem(index);				
			}
        }
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	public void onLocationUpdated(Location location) {
		// TODO Auto-generated method stub
		//this.sendBroadcast(intent)
	    Log.d("DEBUG_D", "Send bdx location updated!!");
		Intent intent = new Intent();
		intent.setAction(InstanceStateKey.BDX_LOC_UPDATED_ACTION);
		intent.putExtra(InstanceStateKey.KEY_BDX_LOC_UPDATED, location);
		this.sendBroadcast(intent);
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() 
	{
	   try{
	      // TODO Auto-generated method stub
	        if (SharedPreferenceUtil.getLayoutModified(this))
	        {
	           MessageBox.showMessage(this, 
	                   R.string.text_error_title, 
	                   R.string.text_error_layout_not_saved_yet);
	           return;
	        }
	        if (SharedPreferenceUtil.getCarInspectDataModified(this)){
	           MessageBox.showMessage(this, 
	                   R.string.text_error_title, 
	                   R.string.text_error_car_inspect_not_saved_yet);
	           return;
	       }

	        Fragment fContent = this.getSupportFragmentManager().findFragmentById(R.id.content_frag);
	        boolean allowBackPress = true;
	        DrawingInspectFragment drawingFragment = null;
	        if (fContent instanceof DrawingInspectFragment)
	        {
	           drawingFragment = (DrawingInspectFragment)fContent;
	            if (actionBar != null){
	                if (!actionBar.isShowing()){
	                    allowBackPress = false;
	                }
	            }
	        }
	        Fragment fMenuList = this.getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
	        if (!fMenuList.isVisible())
	        {
	            allowBackPress = false;
	        }
	        
	        if (allowBackPress){
	           super.onBackPressed();
	        
	        if (drawingFragment != null){
	           if (drawingFragment.isUniversalLayout){
	              return;/* should not reload left menu*/
	           }
	        }
	        reloadLeftMenu();
	      }else{
	          Toast.makeText(this, "Please close full screen mode", 3000).show();
	      }
	   }catch(Exception ex){
	      ex.printStackTrace();
	   }

	}

	public void reloadLeftMenu(){
		if (actionBar != null)
		{
			Tab tab = actionBar.getSelectedTab();
			 try{
				 Fragment lsMenu = getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
				 if (lsMenu != null)
				 {
					if (tab.getPosition() == 1)
						 ((CommonListMenuFragment)lsMenu).reloadListMenus(MenuGroupType.DO_PLAN_JOB);
					 else if (tab.getPosition() == 2){
						 ((CommonListMenuFragment)lsMenu).reloadListMenus(MenuGroupType.DO_TASK);					 
					 }else if (tab.getPosition() == 3){
						 ((CommonListMenuFragment)lsMenu).reloadListMenus(MenuGroupType.EXPENSE);					 
					 }else{
						 ((CommonListMenuFragment)lsMenu).reloadListMenus(MenuGroupType.DASH_BOARD);					 					 
					 }
				 }
			 }catch(Exception ex){
				 ex.printStackTrace();
			 }			
		}
	}
	/*
	private void getOverflowMenu() {

	     try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}*/
}
