package com.epro.psmobile.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.epro.psmobile.PsMainActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.TeamTimeAttendanceActivity;
import com.epro.psmobile.adapter.JobPlanItem;
import com.epro.psmobile.adapter.JobPlanItemsAdapter;
import com.epro.psmobile.adapter.JobPlanItemsAdapter.TaskListType;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.da.PSBODataAdapter.WhereInStatus;
import com.epro.psmobile.data.Customer;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectType;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.data.TeamCheckInHistory.HistoryType;
import com.epro.psmobile.dialog.InfoDialog;
import com.epro.psmobile.dialog.TaskDuplicateDialog;
import com.epro.psmobile.fragment.CommonListMenuFragment.MenuGroupTypeCmd;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.location.PSMobileLocationManager;
//import com.epro.psmobile.report.InspectReportObject;
//import com.epro.psmobile.report.PDFFileWriter;
import com.epro.psmobile.util.ActivityUtil;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.ImageUtil;
import com.epro.psmobile.util.InspectServiceSupportUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.MessageBox.MessageConfirmType;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TaskListFragment extends BaseJobTaskList implements  
com.epro.psmobile.fragment.ContentViewBaseFragment.BoardcastLocationListener 
 {

	private JobPlanItemsAdapter adapter;
	private ContextMenu currentContextMenu;
	private Task taskSelected;
	private ListView lvJobPlan;
	private boolean hasShownInspectContent = false;
	private InfoDialog infoDialog = null;
	
	private MenuGroupTypeCmd groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_DO_TASK_ALL;
	private InspectType selectedInspectType = null;
	private ArrayList<InspectType> allInspectType = null;
	private TaskStatus status = TaskStatus.NOT;
	private WhereInStatus whereInStatus = WhereInStatus.TO_DO_TASK;
	private PSMobileLocationManager locManager = null;

	public TaskListFragment() {
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#getView()
	 */
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
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
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View currentContentView = inflater.inflate(R.layout.ps_fragment_task_list_view, container, false);
		initialViews(currentContentView);
		return currentContentView;
	}
	/* (non-Javadoc)
    * @see android.support.v4.app.Fragment#onDestroyView()
    */
   @Override
   public void onDestroyView() {
      // TODO Auto-generated method stub
      super.onDestroyView();
      if (locManager != null)
      {
         locManager.stopRequestLocationUpdated();
      }
   }
   private void initialViews(View currentContentView)
	{
		 lvJobPlan = (ListView)currentContentView.findViewById(R.id.lv_fragment_task_list);
		 registerForContextMenu(lvJobPlan);

		 if (locManager == null)
		 {
		    PsMainActivity a = null;
		    if (getSherlockActivity() instanceof PsMainActivity){
		       a = (PsMainActivity)getSherlockActivity();
		       locManager = a.getLocationManager();
		    }
		 }
		 
		 try 
		 {
	        setBoardcastLocationListener(this);
		    locManager.requestLocationUpdated();
		 }
		 catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		 }
		/*
		 Query task
		 and add tasks to JobPlanItems
		 */
		
		ArrayList<Task> tasks = null;
		/*
		try {
			tasks = getDataAdapter().getAllTasks(WhereInStatus.TO_DO_TASK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		this.showTaskList(lvJobPlan, 
				tasks,
				TaskStatus.NOT,
				null,
				null,
				MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_DO_TASK_ALL, 
				TaskListType.DO_TASK,
				WhereInStatus.TO_DO_TASK);
	}
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		final AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuInfo;
		int pos = adapterMenuInfo.position;
		ListView lv = (ListView)v;
		JobPlanItem item = (JobPlanItem)lv.getAdapter().getItem(pos);
		if (!item.isTitle)
		{
			   
			   this.taskSelected = item.task;
			   
			   MenuInflater inflater = getActivity().getMenuInflater();
			   inflater.inflate(R.menu.context_menu_do_task, menu);
			   currentContextMenu = menu;
			   /*
			    * 
			    <string name="txt_context_menu_do_task_start">���������������</string>
    			<string name="txt_context_menu_do_task_split">������������������</string>
    			<string name="txt_context_menu_do_task_finish">������������������������������</string>    
    			<string name="txt_context_menu_do_finish_task_report">���������������������������������������</string>
    			<string name="txt_context_menu_do_task_inspect">���������������������������������</string>
    			<string name="txt_context_menu_do_task_delete_duplicated">������������������������</string>
    			<string name="txt_context_menu_do_task_delete_duplicated_confirm">������������������������������������������������������������������������������������������������?</string>
    			<string name="txt_context_menu_do_task_change">������������������������������������</string>
 
			    <item android:id="@+id/do_task_start" android:title="@string/txt_context_menu_do_task_start" />
   				<item android:id="@+id/do_task_split" android:title="@string/txt_context_menu_do_task_split"/>
   				<item android:id="@+id/do_task_duplicated_delete" android:title="@string/txt_context_menu_do_task_delete_duplicated"/>   
   				<item android:id="@+id/do_task_change" android:title="@string/txt_context_menu_do_task_change"/>   
   				<item android:id="@+id/do_task_finish" android:title="@string/txt_context_menu_do_task_finish"/>
   				<item android:id="@+id/do_task_report_inspect" android:title="@string/txt_context_menu_do_finish_task_report"/>
   				<item android:id="@+id/do_task_inspect" android:title="@string/txt_context_menu_do_task_inspect"/>
 
			    */
			   if ((this.taskSelected.getTaskStatus() == TaskStatus.CONFIRM_INSPECT)||
				   (this.taskSelected.getTaskStatus() == TaskStatus.CONFIRMED_FROM_WEB)||
				   (this.taskSelected.getTaskStatus() == TaskStatus.DUPLICATED))
			   {
				   MenuItem report_inspect = currentContextMenu.findItem(R.id.do_task_report_inspect);
				   report_inspect.setVisible(false);				   

				   /*
				    *show menu in case duplicate 
				    */
				   MenuItem task_split = currentContextMenu.findItem(R.id.do_task_split);
				   task_split.setVisible(!this.taskSelected.isDuplicatedTask());
				   
				   MenuItem task_split_del = currentContextMenu.findItem(R.id.do_task_duplicated_delete);
				   task_split_del.setVisible(this.taskSelected.isDuplicatedTask());
				   ////////////////////////////
				   //MenuItem task_inspect = currentContextMenu.findItem(R.id.do_task_inspect);
				   //task_inspect.setVisible(true);
				   
				   MenuItem task_finish = currentContextMenu.findItem(R.id.do_task_finish);
				   task_finish.setVisible(false);

				   MenuItem task_change = currentContextMenu.findItem(R.id.do_task_change);
				   task_change.setVisible(false);
				   
				   Calendar cal = Calendar.getInstance();
				   Date today = DataUtil.getZeroTimeDate(cal.getTime());

				   if (today.compareTo(
						     DataUtil.getZeroTimeDate(taskSelected.getTaskDate())
						   ) != 0)
				   {
					   /*
					    * can not inspect
					    */
					   MenuItem task_inspect = currentContextMenu.findItem(R.id.do_task_inspect);
					   task_inspect.setVisible(false);
					   task_split.setVisible(false);
					   task_split_del.setVisible(false);
				   }
			   }
			   else if (this.taskSelected.getTaskStatus() == TaskStatus.FINISH)
			   {
				   //MenuItem report_inspect = currentContextMenu.findItem(R.id.do_task_report_inspect);
				   //report_inspect.setVisible(true);				   

				   MenuItem task_split = currentContextMenu.findItem(R.id.do_task_split);
				   task_split.setVisible(false);

				   MenuItem task_split_del = currentContextMenu.findItem(R.id.do_task_duplicated_delete);
				   task_split_del.setVisible(false);
				   
				   MenuItem task_finish = currentContextMenu.findItem(R.id.do_task_finish);
				   task_finish.setVisible(false);
				   
				   MenuItem task_inspect = currentContextMenu.findItem(R.id.do_task_inspect);
				   task_inspect.setVisible(false);

				   MenuItem task_change = currentContextMenu.findItem(R.id.do_task_change);
				   task_change.setVisible(false);

			   }else if (
					   (this.taskSelected.getTaskStatus() == TaskStatus.LOCAL_SAVED)||
					   (this.taskSelected.getTaskStatus() == TaskStatus.ALLOW_EDIT))
			   {
				   MenuItem report_inspect = currentContextMenu.findItem(R.id.do_task_report_inspect);
				   report_inspect.setVisible(false);
				   
				   MenuItem task_inspect = currentContextMenu.findItem(R.id.do_task_inspect);
				   task_inspect.setVisible(false);
				   
				   //MenuItem task_change = currentContextMenu.findItem(R.id.do_task_change);
				   //task_change.setVisible(true);
				   
				   MenuItem task_split_del = currentContextMenu.findItem(R.id.do_task_duplicated_delete);
				   task_split_del.setVisible(false);
				
				   /*
				    *show menu in case duplicate 
				    */
				   MenuItem task_split = currentContextMenu.findItem(R.id.do_task_split);
				   task_split.setVisible(!this.taskSelected.isDuplicatedTask());

			   }
		}			
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		boolean bRet = true;
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		///////////////////////////
		Object objTag = info.targetView.getTag();
		if (objTag != null)
		{
	        @SuppressWarnings("unused")
	        JobPlanItem jobPlanItem = null;
	        if (objTag instanceof JobPlanItem)
	        {
	           JobPlanItem jobItem = (JobPlanItem)objTag;
	           try{
	              // if (jobItem.task.getJobRequest().getInspectType().getInspectTypeID() == 4)
	              if (!InspectServiceSupportUtil.checkSupport(jobItem.task.getJobRequest().getInspectType().getInspectTypeID()))
	              {
	                 MessageBox.showMessage(getActivity(), 
	                    R.string.text_error_title,
	                    jobItem.task.getJobRequest().getInspectType().getInspectTypeName()+" not support in this version");
	              return bRet;
	             }
	           }catch(Exception ex){
	              ex.printStackTrace();
	           }
	        }		   
		}
		//////////////////
		switch(item.getItemId())
		{
			case R.id.do_task_duplicated_delete:
			{

				MessageBox.showMessageWithConfirm(getActivity(),
						getActivity().getString(R.string.txt_context_menu_do_task_delete_duplicated),
						getActivity().getString(R.string.txt_context_menu_do_task_delete_duplicated_confirm), 
						new MessageBox.MessageConfirmListener() {
					
					@Override
					public void onConfirmed(MessageConfirmType confirmType) {
						// TODO Auto-generated method stub
						if (confirmType == MessageConfirmType.OK)
						{
							Object obj = info.targetView.getTag();
							@SuppressWarnings("unused")
							JobPlanItem jobPlanItem = null;
							if (obj instanceof JobPlanItem)
							{
								jobPlanItem = (JobPlanItem)obj;
								taskSelected = jobPlanItem.task;
								
								PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
								int rowEffected = dataAdapter.deleteDuplicatedTask(taskSelected.getTaskCode(), 
										taskSelected.getTaskDuplicatedNo());
								if (rowEffected > 0)
								{
									ArrayList<Task> taskList = null;									
									try 
									{
										
										if (getSelectedDate() != null){
											taskList = getTaskListBySelectedDate(whereInStatus);
										}else{
											taskList = getTaskListByStatus(status,whereInStatus);
										}
										
										
										showTaskList(lvJobPlan, 
												taskList,
												status,
												selectedInspectType,
												allInspectType,
												groupCmd,TaskListType.DO_TASK,whereInStatus);

									}
									catch(Exception e){
//										e.printStackTrace();
										MessageBox.showMessage(getActivity(), "Error", e.getMessage());
									}
								}
							}							
						}
					}
				});
				
			}
			break;
			case R.id.do_task_split:
			{
			
				ArrayList<CustomerSurveySite> sites = taskSelected.getJobRequest().getCustomerSurveySiteList();
				if ((sites != null)&&(sites.size() == 1))
				{
					MessageBox.showMessage(getActivity(),
							R.string.text_error_title, 
							R.string.text_warning_not_allow_split);
					return bRet;
				}
				if (taskSelected.getTaskStatus() == TaskStatus.DUPLICATED){
					/*
					 * cannot duplicate from duplicated task
					 */
					MessageBox.showMessage(getActivity(),
							R.string.text_error_title, 
							R.string.text_error_duplicate_must_from_original);
					return bRet;

				}
				/*
				PSBODataAdapter dataAdapter = 
						PSBODataAdapter.getDataAdapter(getActivity());
				try {
					ArrayList<Task> taskDuplicates = dataAdapter.findTaskDuplicates(taskSelected.getTaskCode());
					if ()
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					MessageBox.showMessage(getActivity(), R.string.text_error_title, e1.getMessage());
					return bRet;
				}
				*/
				Calendar c = Calendar.getInstance();
				TaskDuplicateDialog dlg = TaskDuplicateDialog.newInstance(taskSelected,c.getTime());
				dlg.setOnClickTaskDuplicateDialog(new TaskDuplicateDialog.OnClickTaskDuplicateDialog() {
					
					@Override
					public void onClickOk(String locationStart,int startSiteID,
							String locationTo,int endSiteID,
							double distance) {
						// TODO Auto-generated method stub

						Object obj = info.targetView.getTag();
						@SuppressWarnings("unused")
						JobPlanItem jobPlanItem = null;
						if (obj instanceof JobPlanItem)
						{
							jobPlanItem = (JobPlanItem)obj;
							taskSelected = jobPlanItem.task;
							try{
							
							Task newTask = getDataAdapter().createDuplicateTask(taskSelected, 
									locationStart, 
									startSiteID,
									locationTo, 
									endSiteID,
									distance);
								if (newTask != null)
								{
									//reload listview
									Log.d("DEBUG_D", "create duplicate task complete");
									
									ArrayList<Task> taskList = null;
									
									try 
									{
										
										if (getSelectedDate() != null){
											taskList = getTaskListBySelectedDate(whereInStatus);
										}else{
											taskList = getTaskListByStatus(status,whereInStatus);
										}
									}
									catch(Exception e){
										e.printStackTrace();
									}
									showTaskList(lvJobPlan, 
											taskList,
											status,
											selectedInspectType,
											allInspectType,
											groupCmd,TaskListType.DO_TASK,whereInStatus);
								}
														
							}catch(Exception ex){
//								ex.printStackTrace();
								MessageBox.showMessage(getActivity(), "Error", ex.getMessage());
							}
						}
						
					}
					
					@Override
					public void onClickCancel() {
						// TODO Auto-generated method stub
						
					}
				});
				
				dlg.show(getFragmentManager(),"TaskDuplicateDialog");
			}
			break;
			case R.id.do_task_change:
			case R.id.do_task_inspect:
			{
               SharedPreferenceUtil.setAlreadyCommentSaved(getActivity(),true);
               SharedPreferenceUtil.saveCarInspectDataModified(getActivity(),false);
               SharedPreferenceUtil.saveLayoutModified(getActivity(), false);

				if (item.getItemId() == R.id.do_task_inspect)
				{
					if (!SharedPreferenceUtil.checkTeamAlreadyCheckIn(getActivity()))
					{
						MessageBox.showMessage(getActivity(), 
							R.string.text_error_title, 
							R.string.text_warning_not_login_team_of_day);
					return bRet;
					}
				}
				////////
				Object obj = info.targetView.getTag();
				@SuppressWarnings("unused")
				JobPlanItem jobPlanItem = null;
				if (obj instanceof JobPlanItem)
				{
					jobPlanItem = (JobPlanItem)obj;
					taskSelected = jobPlanItem.task;
					//taskSelected.setTaskStatus(TaskStatus.CONFIRM_INSPECT);
					
					Bundle bArgument = new Bundle();
					bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK,
							taskSelected);
					bArgument.putInt(InstanceStateKey.KEY_ARGUMENT_CHECKIN_TYPE,
							HistoryType.START_INSPECT_CHECKIN.getCode());
					
					if (item.getItemId() == R.id.do_task_inspect)
					{
						/*
						 * 
						 */
						ActivityUtil.startNewActivityWithResult(getActivity(),
							TeamTimeAttendanceActivity.class, 
							bArgument, 
							InstanceStateKey.RESULT_TEAM_CHECK_INT_BEFORE_INSPECT);
					}else{
						hasShownInspectContent = true;//need to show inpsect fragment for edit
						showInspectFragment(true/*editable*/);
					}
				}
			}break;
			case R.id.do_task_report_inspect:{
				Object obj = info.targetView.getTag();
				@SuppressWarnings("unused")
				JobPlanItem jobPlanItem = null;
				if (obj instanceof JobPlanItem)
				{
					jobPlanItem = (JobPlanItem)obj;
					
					InspectSummaryReportFragment inspectSummaryReport = new InspectSummaryReportFragment();
					Bundle bArugment = new Bundle();
					bArugment.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, jobPlanItem.task);
					inspectSummaryReport.setArguments(bArugment);
					
					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
					ft.replace(R.id.content_frag, inspectSummaryReport);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.addToBackStack(null);
					ft.commit();
				}
			}break;
			case R.id.do_task_finish:{

				Object obj = info.targetView.getTag();
				@SuppressWarnings("unused")
				JobPlanItem jobPlanItem = null;
				if (obj instanceof JobPlanItem)
				{
					jobPlanItem = (JobPlanItem)obj;
					taskSelected = jobPlanItem.task;
					
					if (!taskSelected.isInspectReportGenerated())
					{

						MessageBox.showMessage(getActivity(), 
								R.string.text_error_title, 
								R.string.text_error_report_not_generated_yet);
						
						bRet = super.onContextItemSelected(item);
						return bRet;
					}
					if (taskSelected.getJobRequest()
					      .getInspectType()
					      .getInspectTypeID() == InspectServiceSupportUtil.SERVICE_CAR_INSPECT)
					{
					      if (SharedPreferenceUtil.getCarInspectDataModified(getActivity())){
					           MessageBox.showMessage(getActivity(), 
					                   R.string.text_error_title, 
					                   R.string.text_error_car_inspect_not_saved_yet);
		                        bRet = super.onContextItemSelected(item);
		                        return bRet;                        
					       }  
					}else{
					   if (SharedPreferenceUtil.getLayoutModified(getActivity()))
					   {
					      MessageBox.showMessage(getActivity(), 
								R.string.text_error_title, 
								R.string.text_error_layout_not_saved_yet);
						bRet = super.onContextItemSelected(item);
						return bRet;						
					   }
					}
					
					if (taskSelected.getJobRequest()
                          .getInspectType()
                          .getInspectTypeID() <= InspectServiceSupportUtil.SERVICE_FARM_LAND_2)
                    {
	                    PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
	                    try 
	                    {
	                        ArrayList<TaskFormDataSaved> taskFormDataSavedList = 
	                                dataAdapter.findTaskFormDataSavedListByJobRequestId(taskSelected.getJobRequest().getJobRequestID(), taskSelected.getTaskCode());
	                        if (taskFormDataSavedList == null)
	                        {
	                            MessageBox.showMessage(getActivity(), 
	                                    R.string.text_error_title, 
	                                    R.string.text_error_comments_not_complete_yet);
	                            bRet = super.onContextItemSelected(item);
	                            return bRet;                                                    
	                        }
	                    } catch (Exception e) {
	                        // TODO Auto-generated catch block
	                        MessageBox.showMessage(getActivity(),
	                                getActivity().getString(R.string.text_error_title), e.getMessage());
	                        bRet = super.onContextItemSelected(item);
	                        return bRet;                                                
	                    }
                    }else if (taskSelected.getJobRequest()
                          .getInspectType()
                          .getInspectTypeID() == InspectServiceSupportUtil.SERVICE_CAR_INSPECT)
                    {
                       /*car*/
                       PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
                       try 
                       {
                          ArrayList<JobRequestProduct> jobRequestProducts = 
                                dataAdapter.findJobRequestProductsByJobRequestIDAndOrderBy(taskSelected.getJobRequest().getJobRequestID());
                          if (jobRequestProducts != null)
                          {
                             Hashtable<String,ArrayList<JobRequestProduct>> vinTable = 
                                   new Hashtable<String,ArrayList<JobRequestProduct>>();
                             
                             for(JobRequestProduct jrp : jobRequestProducts){
                                if (!vinTable.containsKey(jrp.getcVin())){
                                   ArrayList<JobRequestProduct> tmpJobRequestProductList = 
                                         new  ArrayList<JobRequestProduct>();
                                   tmpJobRequestProductList.add(jrp);
                                   vinTable.put(jrp.getcVin(), tmpJobRequestProductList);
                                }else{
                                   vinTable.get(jrp.getcVin()).add(jrp);
                                }
                             }
                             
                             boolean hasDuplicated = false;
                             for(String vin : vinTable.keySet()){
                                if (vinTable.get(vin).size() > 1){
                                   hasDuplicated = true;
                                   break;
                                }
                             }

                             if (hasDuplicated)
                             {
                                MessageBox.showMessage(getActivity(), 
                                      R.string.text_error_title, 
                                      R.string.text_error_cannot_finish_vin_dup);
                                bRet = super.onContextItemSelected(item);
                                return bRet;   
                             }
                          }
                       }catch(Exception e){
                       // TODO Auto-generated catch block
                          MessageBox.showMessage(getActivity(),
                                  getActivity().getString(R.string.text_error_title), e.getMessage());
                          bRet = super.onContextItemSelected(item);
                          return bRet;  
                       }
                       
                    }else{
                       /*check condition for universal*/
                    }
				}
				///////////////////////////////
				if (infoDialog == null)
				{
					Calendar cal = Calendar.getInstance();
					infoDialog = InfoDialog.newInstance(R.string.title_alert_confirm_finish_task, 
						cal.getTime(), 
						getActivity().getString(R.string.text_alert_confirm_finish_task), 
						R.string.label_simple_dlg_btn_end_of_inspect_ok, 
						R.string.label_btn_cancel);
				
					infoDialog.setOnAppInfoDlgListener(new InfoDialog.OnAppInfoDialogListener() {
					
						@Override
						public void onClickOk() {
							// TODO Auto-generated method stub
							Object obj = info.targetView.getTag();
							@SuppressWarnings("unused")
							JobPlanItem jobPlanItem = null;
							if (obj instanceof JobPlanItem)
							{
								jobPlanItem = (JobPlanItem)obj;
								taskSelected = jobPlanItem.task;
								
								int rowEffected = 0;
								
								PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
								
								Calendar cal = Calendar.getInstance();
								
								/*
								 java.sql.Timestamp currentTimestamp,
								 String taskCode,
								 int taskDuplicateNo,
								 int historyType,
								 String customerName,
								 String customerSurveySites,
								 String inspectTypeName,
								 boolean isTaskCompleted
								 */
								String customerName = "";
								String customerSurveySites = "";
								
								if (taskSelected.getJobRequest().getCustomerSurveySiteList() != null){
									customerName = taskSelected.getJobRequest().getCustomerSurveySiteList().get(0).getCustomerName();
									for(CustomerSurveySite site : taskSelected.getJobRequest().getCustomerSurveySiteList()){
										customerSurveySites += site.getSiteAddress()+"\r\n";
									}
								}
								rowEffected = 
										dataAdapter.updateLastTeamCheckOutDateTime(
										new java.sql.Timestamp(cal.getTime().getTime()), 
										taskSelected.getTaskCode(), 
										taskSelected.getTaskDuplicatedNo(), 
										HistoryType.START_INSPECT_CHECKIN.getCode(),
										customerName,
										customerSurveySites,
										taskSelected.getJobRequest().getInspectType().getInspectTypeID(),
										taskSelected.getJobRequest().getInspectType().getInspectTypeName(),
										true);
								if (rowEffected > 0)
								{
									
									taskSelected.setTaskStatus(TaskStatus.FINISH);
									rowEffected = dataAdapter.updateTaskStatus(taskSelected.getTaskCode(),
											taskSelected.getTaskDuplicatedNo(), 
											taskSelected.getTaskStatus());
									
									ArrayList<Task> taskList = null;									
									try 
									{
										if (getSelectedDate() != null){
											taskList = getTaskListBySelectedDate(whereInStatus);
										}else{
											taskList = getTaskListByStatus(status,whereInStatus);
										}
										showTaskList(lvJobPlan, 
												taskList,
												status,
												selectedInspectType,
												allInspectType,
												groupCmd,TaskListType.DO_TASK,whereInStatus);
									}
									catch(Exception e){
										MessageBox.showMessage(getActivity(), "Error", e.getMessage());
									}
								}
							}						
							infoDialog = null;
						}
					
						@Override
						public void onClickCancel() {
						// TODO Auto-generated method stub
							infoDialog = null;
						}
					});
				}								
				if (!infoDialog.isVisible())
					infoDialog.show(getFragmentManager(),
							InfoDialog.class.getName());
			}break;
			case R.id.do_task_start:
			{
				Object obj = info.targetView.getTag();
				JobPlanItem jobPlanItem = null;
				if (obj instanceof JobPlanItem)
				{
					jobPlanItem = (JobPlanItem)obj;
					taskSelected = jobPlanItem.task;
					taskSelected.setTaskStatus(TaskStatus.NOT);
				}
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				CustomerInfoDetailFragment detail = CustomerInfoDetailFragment.newInstance(
															jobPlanItem.task.getJobRequest(),
															jobPlanItem.task,
															false);
//				detail.setArguments(bundle);
				ft.replace(R.id.content_frag, detail);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
				
				Fragment f   = getActivity().getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
				if (f instanceof CommonListMenuFragment)
				{
					if (jobPlanItem.task.getTaskStatus() == TaskStatus.CONFIRM_INSPECT){
						((CommonListMenuFragment)f).buildMenu(jobPlanItem.task,
								jobPlanItem.task.getJobRequest(),true);						
					}else{
						((CommonListMenuFragment)f).buildMenu(jobPlanItem.task,
								jobPlanItem.task.getJobRequest(),false);
					}
				}
			}break;
			default:
			{
				bRet = super.onContextItemSelected(item);					
			}break;
		}
		return bRet;
		
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d("DEBUG_D", "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK)
		{
			if (requestCode == InstanceStateKey.RESULT_TEAM_CHECK_INT_BEFORE_INSPECT)
			{
				if (taskSelected != null)
				{
					PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
					int rowEffected = dataAdapter.updateTaskStatus(taskSelected.getTaskCode(), 
							taskSelected.getTaskDuplicatedNo(), TaskStatus.LOCAL_SAVED);
					if (rowEffected > 0){
						taskSelected.setTaskStatus(TaskStatus.LOCAL_SAVED);
						hasShownInspectContent = true;
					}
					else{
						MessageBox.showMessage(getActivity(), "Error", "Can't update task status");
					}
				}
			}
		}else
			hasShownInspectContent = false;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showInspectFragment(false);
	}
	private void showInspectFragment(boolean isEditable){
		if (this.hasShownInspectContent){
			if (taskSelected != null){

				/*
				 * fix bug show edit when taskstatus is duplicated
				 */
				/*
				if (taskSelected.getTaskStatus() == TaskStatus.DUPLICATED){
					isEditable = true;
				}*/
				
				int iCountStack = getActivity().getSupportFragmentManager().getBackStackEntryCount();
				for(int i = 0; i < iCountStack;i++)
					getActivity().getSupportFragmentManager().popBackStack();
				
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				
				CustomerInfoDetailFragment detail = CustomerInfoDetailFragment.newInstance(
						taskSelected.getJobRequest(), taskSelected,false);
				ft.replace(R.id.content_frag, detail, CustomerInfoDetailFragment.class.getName());
				ft.addToBackStack(null);
				ft.commit();
				
				Fragment f   = getActivity().getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
				if (f instanceof CommonListMenuFragment)
				{
					if (isEditable){
						((CommonListMenuFragment)f).buildMenu(taskSelected,
								taskSelected.getJobRequest(),true);												
					}else
					{
						if ((taskSelected.getTaskStatus() == TaskStatus.CONFIRM_INSPECT)||
							(taskSelected.getTaskStatus() == TaskStatus.CONFIRMED_FROM_WEB)||
							(taskSelected.getTaskStatus() == TaskStatus.LOCAL_SAVED)
							)
						{
							((CommonListMenuFragment)f).buildMenu(taskSelected,
								taskSelected.getJobRequest(),true);						
						}else{
							((CommonListMenuFragment)f).buildMenu(taskSelected,
									taskSelected.getJobRequest(),false);
						}
					}
				}
				hasShownInspectContent = false;
			}
		}

	}
	@Override
	public void sortTaskListByStatus(MenuGroupTypeCmd groupCmd,
			InspectType selectedInspectType,
			ArrayList<InspectType> allInspectType,
			TaskStatus status,WhereInStatus whereInStatus) {
		// TODO Auto-generated method stub
		this.groupCmd = groupCmd;
		this.selectedInspectType = selectedInspectType;
		this.allInspectType = allInspectType;
		this.status = status;
		this.whereInStatus = whereInStatus;
		
		
		ArrayList<Task> taskList;
		try {
			
			if (this.getSelectedDate() != null){
				taskList = this.getTaskListBySelectedDate(whereInStatus);
			}else{
				taskList = getTaskListByStatus(status,whereInStatus);
			}
				
			showTaskList(lvJobPlan, 
					taskList,
					status,
					selectedInspectType,
					allInspectType,
					groupCmd,TaskListType.DO_TASK,whereInStatus);
			
			/*
			if (this.locManager != null){
	            JobPlanItemsAdapter adapter = (JobPlanItemsAdapter)lvJobPlan.getAdapter();
	            adapter.setCurrentLocation(locManager.getLastknowLocation());
	            adapter.notifyDataSetChanged();			   
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onBoardcastLocationUpdated(Location currentLocation) {
		// TODO Auto-generated method stub
	    Log.d("DEBUG_D", "Location updated!!!");
		if (lvJobPlan != null)
		{
			JobPlanItemsAdapter adapter = (JobPlanItemsAdapter)lvJobPlan.getAdapter();
			adapter.setCurrentLocation(currentLocation);
			adapter.notifyDataSetChanged();
		}
	}
}
