package com.epro.psmobile.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.actionbarsherlock.app.SherlockListFragment;
import com.epro.psmobile.R;
import com.epro.psmobile.TabMenuListener;
import com.epro.psmobile.adapter.JobPlanItemsAdapter.TaskListType;
import com.epro.psmobile.adapter.NewsFragmentPageAdapter;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.da.PSBODataAdapter.WhereInStatus;
import com.epro.psmobile.data.Customer;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.ExpenseType;
import com.epro.psmobile.data.InspectType;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.LayoutItemScaleHistory;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.dialog.ReportTypeSelectDialog;
import com.epro.psmobile.dialog.ScaleSetupDialog;
import com.epro.psmobile.fragment.NewsFragment.NewsFilterBy;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.InspectServiceSupportUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.FontUtil;
import com.epro.psmobile.util.FontUtil.FontName;
import com.epro.psmobile.util.MessageBox;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class CommonListMenuFragment extends SherlockListFragment {
	public enum MenuGroupType
	{
		NEWS,
		EXPENSE,
		DO_PLAN_JOB,
		DO_TASK,
		DASH_BOARD,
		STANDARD,
		REPORT
	};
	public enum MenuGroupTypeCmd
	{
		NO_RESPONSE,
		SHOW_JOB_DETAIL,
		SHOW_LAYOUT,
		SHOW_LAST_INSPECT_RESULT,
		SHOW_LOCATION,
		SHOW_COMMENT,
		SHOW_REMARK,
		SHOW_SUMMARY_REPORT_INSPECT,
		SHOW_EXPENSE_BY_ID,
		SHOW_EXPENSE_ALL,
		SHOW_TASK_BY_STATUS_ALL/*plan*/,
		SHOW_TASK_BY_STATUS_DO_TASK_ALL/*job*/,
		SHOW_TASK_BY_STATUS/**/,
		SHOW_TASK_BY_STATUS_DO_TASK,
		SHOW_TASK_BY_TIME_ALL,
		SHOW_TASK_BY_TIME_TODAY,
		SHOW_TASK_BY_TIME_NEXT_WEEK,
		SHOW_TASK_BY_CUSTOM_TIME,
		SHOW_TASK_BY_INSPECT_ALL,
		SHOW_TASK_BY_INSPECT_ID,
		SHOW_NEWS_ALL,
		SHOW_NEWS_TODAY,
		SHOW_NEWS_7_DAYS,
		SHOW_NEWS_15_DAYS,
		SHOW_REPORT_ALL
	};
	
	class MenuItem
	{
		public String label;
		public String countItem;
		public boolean hasShowCountItem = true;
		public boolean isTitleGroup;
		public CustomerSurveySite customerSurveySite;
		public ExpenseType currentExpenseType;
		public MenuGroupTypeCmd groupCmd = MenuGroupTypeCmd.NO_RESPONSE;
		public TaskStatus sortByTaskStatus = TaskStatus.NOT;
		public Date selectedDate;
		public InspectType currentInspectType;
		public ArrayList<InspectType> allInspectType;
		public WhereInStatus whereInStatus = WhereInStatus.NONE;
	};
	
	private ArrayList<MenuItem> menuItemList;
	private JobRequest jobRequest;
	private Task currentTask;
	private MenuGroupType menuGroupType;
	
	private DatePickerDialog dp_dlg;
	private int mYear;
	private int mMonth;
	private int mDay;
	
	private ScaleSetupDialog scaleDlg;
	
	private Team team;
	
	class MenuListAdapter extends BaseAdapter{

		private Context context;
		private ArrayList<MenuItem> menuItems;
		private LayoutInflater inflater;
		
		public MenuListAdapter(Context ctxt,
				ArrayList<MenuItem> menuItems)
		{
			this.context = ctxt;
			this.menuItems = menuItems;
			inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menuItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return menuItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			MenuItem menuItem = (MenuItem)getItem(arg0);
			View viewMenuItem = null;
			
			if (menuItem.isTitleGroup)
			{
				viewMenuItem = inflater.inflate(R.layout.ps_list_fragment_group_title_name, null);
				
				TextView tvTitleName = (TextView)viewMenuItem.findViewById(R.id.tv_list_fragment_group_title_name);
				tvTitleName.setText(menuItem.label);
				
				FontUtil.replaceFontTextView(getActivity(), tvTitleName, FontName.THSARABUN_BOLD);
				
			}else{
				viewMenuItem = inflater.inflate(R.layout.ps_list_fragment_group_sub_type_name, null);
				TextView tvName = (TextView)viewMenuItem.findViewById(R.id.tv_ps_list_fragment_group_sub_type_name);
				tvName.setText(menuItem.label);
				
				FontUtil.replaceFontTextView(getActivity(), tvName, FontName.THSARABUN);

				TextView tvCount = (TextView)viewMenuItem.findViewById(R.id.tv_ps_list_fragment_group_sub_type_count);
				if (menuItem.hasShowCountItem){
					    tvCount.setVisibility(View.VISIBLE);
						tvCount.setText(menuItem.countItem);
						tvCount.setTextColor(Color.GREEN);
						FontUtil.replaceFontTextView(getActivity(), tvCount, FontName.THSARABUN);
				}else{
					tvCount.setVisibility(View.INVISIBLE);
				}
			}
			if (viewMenuItem != null)
			{
				viewMenuItem.setTag(menuItem);
			}
			return viewMenuItem;
		}		
	}
	
	//all who call on god in true faith , earnestly from the heart, will certainly be heard , and will receive what they 
	//they asked and desired
	public CommonListMenuFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//View group =  inflater.inflate(R.layout.ps_list_fragment_group_type,container, false);//super.onCreateView(inflater, container, savedInstanceState);
		//return group;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@SuppressWarnings("unused")
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		if (SharedPreferenceUtil.getLayoutModified(getActivity()))
		{
			MessageBox.showMessage(getActivity(), 
					R.string.text_error_title, 
					R.string.text_error_layout_not_saved_yet);
			return;
		}
		if (!SharedPreferenceUtil.isAlreadyCommentSaved(getActivity()))
		{
		   if (team != null){
		      if (team.isQCTeam()){
	              MessageBox.showMessage(getActivity(), 
	                    R.string.text_error_title, 
	                    R.string.text_error_comment_qc_no13_not_saved_yet);
		         
		      }else{
		           MessageBox.showMessage(getActivity(), 
		                 R.string.text_error_title, 
		                 R.string.text_error_comment_no13_not_saved_yet);
		      }
		   }else{
		      MessageBox.showMessage(getActivity(), 
                 R.string.text_error_title, 
                 R.string.text_error_comment_no13_not_saved_yet);
		   }
           
         return;
		}
	    
		if (SharedPreferenceUtil.getCarInspectDataModified(getActivity())){
	           MessageBox.showMessage(getActivity(), 
	                   R.string.text_error_title, 
	                   R.string.text_error_car_inspect_not_saved_yet);
	           return;
	    }
		//////////////////////
		Object objTag =  v.getTag();
		if (objTag instanceof MenuItem)
		{
			final MenuItem menuItem = (MenuItem)objTag;
			
			Fragment nextFragment = null;
			
			switch(menuItem.groupCmd)
			{
				case SHOW_REPORT_ALL:
				{
					 /*
	            	  * show report menu
	            	  */
	            	 ReportTypeSelectDialog reportTypeDlg = new ReportTypeSelectDialog();
	            	 reportTypeDlg.show(this.getActivity().getSupportFragmentManager(), 
	            			 ReportTypeSelectDialog.class.getName());
	            	 
	            	 ReportFragment reportFragment = new ReportFragment();
	            	 nextFragment = reportFragment;

				}break;
				case SHOW_NEWS_ALL:
				case SHOW_NEWS_TODAY:
				case SHOW_NEWS_7_DAYS:
				case SHOW_NEWS_15_DAYS:
				{
					Fragment currentFragment = this.getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frag);
					
					if (currentFragment instanceof NewsPagerFragment)
					{
						NewsPagerFragment pagerFragment = (NewsPagerFragment)currentFragment;
						if (pagerFragment.getViewPager() != null)
						{
							pagerFragment.getViewPager().setCurrentItem(0, true);
							//if (pagerFragment.getViewPager().getCurrentItem() == 0)
							{
								NewsFragmentPageAdapter pageAdapter = (NewsFragmentPageAdapter)pagerFragment.getViewPager().getAdapter();
								
								NewsFragment newsFrag = pageAdapter.getNewsFragment();
								if (menuItem.groupCmd == MenuGroupTypeCmd.SHOW_NEWS_ALL){
									newsFrag.showNewsPublishByFilter(NewsFilterBy.ALL);
								}else if (menuItem.groupCmd == MenuGroupTypeCmd.SHOW_NEWS_TODAY){
									newsFrag.showNewsPublishByFilter(NewsFilterBy.TODAY);
								}else if (menuItem.groupCmd == MenuGroupTypeCmd.SHOW_NEWS_7_DAYS){
									newsFrag.showNewsPublishByFilter(NewsFilterBy.DAY7);							
								}else if (menuItem.groupCmd == MenuGroupTypeCmd.SHOW_NEWS_15_DAYS){
									newsFrag.showNewsPublishByFilter(NewsFilterBy.DAY15);
								}
								
							}
						}
						
					}
				}break;
				case SHOW_TASK_BY_INSPECT_ID:
				case SHOW_TASK_BY_INSPECT_ALL:
				{
					//clearPreviusFragmentStacks();

					menuItem.sortByTaskStatus = TaskStatus.NOT;//show all
					Fragment currentFragment = this.getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frag);
					if (currentFragment instanceof BaseJobTaskList)
					{
						((BaseJobTaskList)currentFragment).setSelectedDate(null);
						((BaseJobTaskList)currentFragment).sortTaskListByStatus(menuItem.groupCmd,
								menuItem.currentInspectType,
								menuItem.allInspectType,
								menuItem.sortByTaskStatus,
								menuItem.whereInStatus);
					}
					else if (currentFragment instanceof DashboardCalendarFragment)
					{
						int inspectTypeId = -1;
						if (menuItem.currentInspectType != null)
						{
							inspectTypeId = menuItem.currentInspectType.getInspectTypeID();
						}
						((DashboardCalendarFragment)currentFragment).sortByInspectTypeId(inspectTypeId,
								menuItem.whereInStatus);
						((DashboardCalendarFragment)currentFragment).refreshView();	
					}
				}
				break;
				case SHOW_TASK_BY_TIME_ALL:
				case SHOW_TASK_BY_TIME_TODAY:
				case SHOW_TASK_BY_TIME_NEXT_WEEK:{
					
					//clearPreviusFragmentStacks();

					menuItem.sortByTaskStatus = TaskStatus.NOT;//show all
					Fragment currentFragment = this.getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frag);
					if (currentFragment instanceof BaseJobTaskList)
					{
						((BaseJobTaskList)currentFragment).setSelectedDate(null);
						((BaseJobTaskList)currentFragment).sortTaskListByStatus(menuItem.groupCmd,
								null,
								null,
								menuItem.sortByTaskStatus,
								menuItem.whereInStatus);
					}					
					else if (currentFragment instanceof DashboardCalendarFragment)
					{
						
					}

				}break;
				case SHOW_TASK_BY_CUSTOM_TIME:{
					menuItem.sortByTaskStatus = TaskStatus.NOT;
					
					 final Calendar calendar = Calendar.getInstance();
					  
					  mYear = calendar.get(Calendar.YEAR);
					  mMonth = calendar.get(Calendar.MONTH)+1;
					  mDay = calendar.get(Calendar.DAY_OF_MONTH);
					  
					  if (dp_dlg == null)
						  dp_dlg = new DatePickerDialog(this.getActivity(),
								  new DatePickerDialog.OnDateSetListener(){

									@Override
									public void onDateSet(DatePicker view, 
											int selectedYear,
											int selectedMonth, 
											int selectedDay) {
										// TODO Auto-generated method stub
										//clearPreviusFragmentStacks();

//										WhereInStatus whereStatus = menuItem.whereInStatus;
										MenuGroupType menuGroupType = CommonListMenuFragment.this.menuGroupType;
										if (menuGroupType == MenuGroupType.DO_PLAN_JOB)
										{
											menuItem.whereInStatus = WhereInStatus.DO_PLAN_TASK;
										}else if (menuGroupType == MenuGroupType.DO_TASK)
										{
											menuItem.whereInStatus = WhereInStatus.TO_DO_TASK;
										}
										Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frag);
										if (currentFragment instanceof BaseJobTaskList)
										{
											Calendar c = Calendar.getInstance();
											c.set(selectedYear,selectedMonth,selectedDay);
											((BaseJobTaskList)currentFragment).setSelectedDate(c.getTime());
											((BaseJobTaskList)currentFragment).sortTaskListByStatus(menuItem.groupCmd,
													null,
													null,
													menuItem.sortByTaskStatus,
													menuItem.whereInStatus);
										}
										else if (currentFragment instanceof DashboardCalendarFragment)
										{
											
										}

									}
							  
						  },mYear,
						  mMonth - 1,
						  mDay
								  );
					  
					  if (!dp_dlg.isShowing()){
						  dp_dlg.show();
					  }

				}break;
				case SHOW_TASK_BY_STATUS_DO_TASK_ALL:
				case SHOW_TASK_BY_STATUS_ALL:
				{
					//clearPreviusFragmentStacks();
					menuItem.sortByTaskStatus = TaskStatus.NOT;//show all
					Fragment currentFragment = this.getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frag);
					if (currentFragment instanceof BaseJobTaskList)
					{
						((BaseJobTaskList)currentFragment).setSelectedDate(null);
						((BaseJobTaskList)currentFragment).sortTaskListByStatus(menuItem.groupCmd,
								null,
								null,
								menuItem.sortByTaskStatus,menuItem.whereInStatus);
					}
					else if (currentFragment instanceof DashboardCalendarFragment)
					{
						((DashboardCalendarFragment)currentFragment).sortByTaskStatus(menuItem.sortByTaskStatus,
								menuItem.whereInStatus);
						((DashboardCalendarFragment)currentFragment).refreshView();
					}
				}
				break;
				case SHOW_TASK_BY_STATUS_DO_TASK:
				case SHOW_TASK_BY_STATUS:
				{
					//clearPreviusFragmentStacks();
					Fragment currentFragment = this.getActivity().
							getSupportFragmentManager().
							findFragmentById(R.id.content_frag);
					if (currentFragment instanceof BaseJobTaskList)
					{
						((BaseJobTaskList)currentFragment).setSelectedDate(null);
						((BaseJobTaskList)currentFragment).sortTaskListByStatus(menuItem.groupCmd,
								null,
								null,
								menuItem.sortByTaskStatus,
								menuItem.whereInStatus);
					}
					else if (currentFragment instanceof DashboardCalendarFragment)
					{
						((DashboardCalendarFragment)currentFragment).sortByTaskStatus(menuItem.sortByTaskStatus,
								menuItem.whereInStatus);
						((DashboardCalendarFragment)currentFragment).refreshView();
					}

				}break;
				case SHOW_EXPENSE_BY_ID:
				{
					clearPreviusFragmentStacks();

					Fragment currentFragment = this.getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frag);
					if (currentFragment instanceof ExpenseFragment)
					{
						try {
							((ExpenseFragment)currentFragment).bindListViewByWithExpenseTypeId(menuItem.currentExpenseType.getExpenseTypeID());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}					
				}break;
				case SHOW_EXPENSE_ALL:
				{
					clearPreviusFragmentStacks();

					Fragment currentFragment = this.getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frag);
					if (currentFragment instanceof ExpenseFragment)
					{
						try {
							((ExpenseFragment)currentFragment).bindListView();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}break;
				case SHOW_JOB_DETAIL:
				{
					//JobRequestPlanDetailFragment detailFragment = JobRequestPlanDetailFragment.newInstance(jobRequest);				
					//Bundle bundle = new Bundle();
					//bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
//					detailFragment.setArguments(bundle);
					
					CustomerInfoDetailFragment detailFragment = 
							CustomerInfoDetailFragment.newInstance(jobRequest,
									this.currentTask,false/*show in task*/);
					nextFragment = detailFragment;
					
				}break;
				case SHOW_LAYOUT:
				case SHOW_LAST_INSPECT_RESULT:
				{
					@SuppressWarnings("rawtypes")
					InspectHistoryFragment showLayoutFragment = new InspectHistoryFragment();					
					Bundle bundle = new Bundle();
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, menuItem.customerSurveySite);
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);
					bundle.putBoolean(InstanceStateKey.KEY_ARGUMENT_IS_SHOW_JUST_LAYOUT_BUILDING, 
							(menuItem.groupCmd == MenuGroupTypeCmd.SHOW_LAYOUT));
					
					showLayoutFragment.setArguments(bundle);

					nextFragment = showLayoutFragment;
				}break;				
				case SHOW_LOCATION:
				{
					final PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
					/*
					Team team = null;
					try {
						team = dataAdapter.getTeamByDeviceId(SharedPreferenceUtil.getDeviceId(getActivity()));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						MessageBox.showMessage(getActivity(), 
								getString(R.string.text_error_title),
								e1.getMessage());
						return;
					}*/
					
/*					
					if (team.isQCTeam())					
					{
						JobCommentFragment commentFragment = new JobCommentFragment();
						Bundle bundle = new Bundle();
					    bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
						bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);
						bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, menuItem.customerSurveySite);
						bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_TEAM, team);

						commentFragment.setArguments(bundle);
						
						nextFragment = commentFragment;
						
					}else{*/
					
					int inspectTypeID = jobRequest.getInspectType().getInspectTypeID();
					/*
					 * inspect type 1 , 2 about farmland product inspect
					 */
					if ((inspectTypeID == 1)||
						(inspectTypeID == 2))
					{
					
					DrawingInspectFragment drawingFragment = new DrawingInspectFragment();					
					Bundle bundle = new Bundle();
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, menuItem.customerSurveySite);
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);
					drawingFragment.setArguments(bundle);

					nextFragment = drawingFragment;
					
					/*
					 */
					try {
						LayoutItemScaleHistory scale = dataAdapter.getLayoutScale(currentTask.getTaskCode(), menuItem.customerSurveySite.getCustomerSurveySiteID());
						if (scale == null)
						{
							  scaleDlg = ScaleSetupDialog.newInstance(currentTask,
									menuItem.customerSurveySite, new ScaleSetupDialog.OnScaleSetupDialogInterface() {
										
										@Override
										public void onScaleSetupComplete(Task currentTask, CustomerSurveySite site,
												double siteWidth, double siteLong) {
											// TODO Auto-generated method stub
											LayoutItemScaleHistory scaleHistory = new 
													LayoutItemScaleHistory();
											scaleHistory.setCustomerSurveySiteId(site.getCustomerSurveySiteID());
											scaleHistory.setCustomerSurveySiteRowId(site.getCustomerSurveySiteRowID());
											scaleHistory.setDuplicatedNo(currentTask.getTaskDuplicatedNo());
											scaleHistory.setTaskCode(currentTask.getTaskCode());
											scaleHistory.setSiteWidth(siteWidth);
											scaleHistory.setSiteLong(siteLong);
											
											try {
												int rowEffected = dataAdapter.insertLayoutItemScaleHistory(scaleHistory);
												if (rowEffected > 0)
												{
													MessageBox.showSaveCompleteMessage(getActivity());
													scaleDlg.dismiss();
												}
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									});
							 /*
							if (!scaleDlg.isVisible()){
								scaleDlg.show(getChildFragmentManager(), ScaleSetupDialog.class.getName());
								scaleDlg.setCancelable(false);
							}*/
						 }
						} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					   }
					}else 
					{
					   Fragment inspectCarReportList = 
                             InspectReportListFragment.newInstance(jobRequest, 
                                     currentTask,
                                     menuItem.customerSurveySite);
                         nextFragment = inspectCarReportList;
                         if (nextFragment == null)
                         {
                             MessageBox.showMessage(getActivity(), 
                                     "Error", 
                                     ""+jobRequest.getInspectType().getInspectTypeName()+" not support now!");
                         }
					}
					   /*
					if (inspectTypeID == 4)
					{
						Fragment inspectCarReportList = 
								InspectReportListFragment.newInstance(jobRequest, 
										currentTask,
										menuItem.customerSurveySite);
							nextFragment = inspectCarReportList;
							if (nextFragment == null)
							{
								MessageBox.showMessage(getActivity(), 
										"Error", 
										""+jobRequest.getInspectType().getInspectTypeName()+" not support now!");
							}
					}else{
					   //
					   nextFragment = UniversalInspectListFragment.newInstance(jobRequest, 
					         currentTask, 
					         menuItem.customerSurveySite);
					   
					   if (nextFragment == null)
                       {
                           MessageBox.showMessage(getActivity(), 
                                   "Error", 
                                   ""+jobRequest.getInspectType().getInspectTypeName()+" not support now!");
                       }
					}*/
				}break;
				case SHOW_COMMENT:
				{
					JobCommentFragment commentFragment = new JobCommentFragment();
					Bundle bundle = new Bundle();
				    bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);
					commentFragment.setArguments(bundle);
					
					nextFragment = commentFragment;
				}break;				
				case SHOW_REMARK:/*opinion*/
				{
					JobOpinionFragment opionFragment = new JobOpinionFragment();
					opionFragment.setJobOpinionDataInterface(new JobOpinionFragment.JobOpinionDataInterface() {
						
						@Override
						public void jobOpinionSaved(String textRemark) {
							// TODO Auto-generated method stub
							if (currentTask != null)
								currentTask.setRemark(textRemark);
						}
					});
					Bundle bundle = new Bundle();
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);

					opionFragment.setArguments(bundle);
					
					nextFragment = opionFragment;
					
				}break;
				case SHOW_SUMMARY_REPORT_INSPECT:
				{
				   if (currentTask.getJobRequest().getInspectType().getInspectTypeID() == 
				         InspectServiceSupportUtil.SERVICE_CAR_INSPECT)
				   {
		                 PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
		                   try 
		                   {
		                      ArrayList<JobRequestProduct> jobRequestProducts = 
		                            dataAdapter.findJobRequestProductsByJobRequestIDAndOrderBy(currentTask.getJobRequest().getJobRequestID());
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
		                            return;   
		                         }
		                      }
		                   }catch(Exception e){
		                   // TODO Auto-generated catch block
		                      MessageBox.showMessage(getActivity(),
		                              getActivity().getString(R.string.text_error_title), e.getMessage());
		                      return;  
		                   }

				   }
				   
				   
					InspectSummaryReportFragment summaryReportFragment = new InspectSummaryReportFragment();
					Bundle bundle = new Bundle();
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
					bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);

					summaryReportFragment.setArguments(bundle);
					
					nextFragment = summaryReportFragment;
				}break;
			default:
				break;
			}
			
			if (nextFragment != null)
			{
				String tag = nextFragment.getClass().getName();
				int iCountStack = this.getChildFragmentManager().getBackStackEntryCount();
				for(int i = 0; i < iCountStack;i++)
					this.getChildFragmentManager().popBackStack();

				iCountStack = getActivity().getSupportFragmentManager().getBackStackEntryCount();
				for(int i = 0; i < iCountStack;i++)
					getActivity().getSupportFragmentManager().popBackStack();

				
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();				
				ft.replace(R.id.content_frag, nextFragment,tag);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(tag);
				ft.commit();
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	
		/*
	    ActionBar bar = getActivity().getActionBar();
	    bar.setDisplayHomeAsUpEnabled(false);
	    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    // Must call in order to get callback to onCreateOptionsMenu()
	    setHasOptionsMenu(true);
	      */ 
		populateLeftMenu(MenuGroupType.DASH_BOARD);
		
	    ListView lv = getListView();
        lv.setCacheColorHint(Color.TRANSPARENT); // Improves scrolling performance
        lv.setBackgroundColor(Color.LTGRAY);
        
        ViewTreeObserver observer = getListView().getViewTreeObserver();
        observer.addOnGlobalLayoutListener(layoutListener);		
	}
	public void buildMenu(Task task,
			JobRequest jobRequest,
			boolean needInspect)
	{
		this.currentTask = task;
		this.jobRequest = jobRequest;
		/*
		 * filter customer survey site for split task
		 */
		try{
			PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
			ArrayList<CustomerSurveySite> customerSites = new ArrayList<CustomerSurveySite>();
			ArrayList<Task> allTask = dataAdapter.getAllTasks();
			ArrayList<Task> allTaskHaveDuplicated = new ArrayList<Task>();
			boolean hasMoreThanOne = false;
			int iCount = 0;
			for(Task t : allTask)
			{
				if (t.getTaskCode().equalsIgnoreCase(currentTask.getTaskCode()))
				{
					allTaskHaveDuplicated.add(t);
					iCount++;										
				}else{
					String t_code = t.getTaskCode();
					String c_code = currentTask.getTaskCode();

					int idx_t = t_code.indexOf("_");
					int idx_c  = c_code.indexOf("_");
					
					String r_t_code = t_code;
					String r_c_code = c_code;
					if (idx_t >= 0){
						r_t_code = t_code.substring(0,idx_t);
					}
					if (idx_c >= 0)
					{
						r_c_code = c_code.substring(0, idx_c);
					}
					
					if (r_t_code.equalsIgnoreCase(r_c_code)){
						allTaskHaveDuplicated.add(t);
						iCount++;	
					}
				}
				/*
				if (t.getTaskCode().contains(currentTask.getTaskCode()))
				{
				}*/
			}
			hasMoreThanOne = iCount > 1;
			
			if (!hasMoreThanOne)
			{
				customerSites.addAll(jobRequest.getCustomerSurveySiteList());					
			}else{
				ArrayList<CustomerSurveySite> siteAll = new ArrayList<CustomerSurveySite>(
							jobRequest.getCustomerSurveySiteList());
				
				if (currentTask.getDuplicatedSurveyEndSiteID() == 0){
						ArrayList<Integer> idxList = new ArrayList<Integer>();
						for(Task td : allTaskHaveDuplicated){
							for(int i = 0; i < siteAll.size();i++)
							{
								CustomerSurveySite s  = siteAll.get(i);
								if (td.getDuplicatedSurveyEndSiteID() == s.getCustomerSurveySiteID())
								{
									idxList.add(i);
									break;
								}
							}
						}
						for(Integer idx : idxList)
						{
							siteAll.remove(idx.intValue());
						}
						customerSites.addAll(siteAll);
				}else{
					for(CustomerSurveySite s : siteAll)
					{
						if (s.getCustomerSurveySiteID() == currentTask.getDuplicatedSurveyEndSiteID()){
							customerSites.add(s);
							break;
						}
					}
				}
			}
			populateMenuTask(/*jobRequest.getCustomerSurveySiteList()*/customerSites,needInspect);
			getListView().invalidateViews();
		}catch(Exception ex)
		{
			MessageBox.showMessage(getActivity(), 
					getActivity().getString(R.string.text_error_title),
					ex.getMessage());
		}
	}
	private void populateMenuTask(ArrayList<CustomerSurveySite> customerSurveySiteList,
			boolean needInspect){
		ArrayList<MenuItem> menus = new ArrayList<MenuItem>();
		
		String[] menu_title_group_task_list = 
				this.getActivity().getResources().getStringArray(R.array.array_header_name_of_group_job_inspect);
		
		final PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
		//Team team = null;
		try {
			team = dataAdapter.getTeamByDeviceId(SharedPreferenceUtil.getDeviceId(getActivity()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			MessageBox.showMessage(getActivity(), 
					getString(R.string.text_error_title),
					e1.getMessage());
			return;
		}
		for(int i = 0 ; i < menu_title_group_task_list.length;i++){
			
			if (!needInspect)
			{
				if (( i >= 1)&&(i < menu_title_group_task_list.length)){
					continue;
				}
			}
			if ((i == 1)||(i == 3)){
				//not show group name
				if (!team.isQCTeam())
				{
					MenuItem item = new MenuItem();
					item.isTitleGroup = true;
					item.label = menu_title_group_task_list[i];
					menus.add(item);
				}
			}else{
				MenuItem item = new MenuItem();
				item.isTitleGroup = true;
				item.label = menu_title_group_task_list[i];
				menus.add(item);				
			}
			switch(i)
			{
				case 0:
				{
					String[] subItem = this.getActivity().getResources().getStringArray(R.array.array_list_of_inspect_item);
					for(int j = 0; j < subItem.length ;j++)
					{
						MenuItem subMenuItem = new MenuItem();
						subMenuItem.isTitleGroup = false;
						subMenuItem.hasShowCountItem = false;
						subMenuItem.label = subItem[j];
						if (j == 0){
							subMenuItem.groupCmd = MenuGroupTypeCmd.SHOW_JOB_DETAIL; 
						}else if (j == 1){
							subMenuItem.groupCmd = MenuGroupTypeCmd.SHOW_LAYOUT;
						}else if (j == 2){
							subMenuItem.groupCmd = MenuGroupTypeCmd.SHOW_LAST_INSPECT_RESULT;
						}
						menus.add(subMenuItem);
					}
				}break;
				case 1:
				{
					
					if (!team.isQCTeam())
					{
						if (needInspect)
						{
							for(CustomerSurveySite surveySite : customerSurveySiteList)
							{
								MenuItem subMenuItem = new MenuItem();
								subMenuItem.isTitleGroup = false;
								subMenuItem.hasShowCountItem = false;
								subMenuItem.label = surveySite.getCustomerName() + "\n(" + surveySite.getSiteAddress() +")";
								subMenuItem.customerSurveySite = surveySite;
								subMenuItem.groupCmd = MenuGroupTypeCmd.SHOW_LOCATION;
								menus.add(subMenuItem);
							}
						}
					}
				}break;
				case 2:
				case 3:
				case 4:
				{
					if (needInspect)
					{						
						MenuItem subMenuItem = new MenuItem();
						subMenuItem.isTitleGroup = false;
						subMenuItem.hasShowCountItem = false;
						subMenuItem.label = customerSurveySiteList.get(0).getCustomerName();
						if (i == 2)
						{
						   if (team.isQCTeam())
						   {
						      /*
						       * if qc team show comment menu always
						       */
                              subMenuItem.groupCmd = MenuGroupTypeCmd.SHOW_COMMENT;
                              menus.add(subMenuItem);                              
                              
						   }else{
						      /*
						       * if car inspect not show 13questions exceped QC Team
						       */
	                              //if (this.currentTask.getJobRequest().getInspectType().getInspectTypeID() 
	                              //      <= InspectServiceSupportUtil.SERVICE_FARM_LAND_2)
						          if (InspectServiceSupportUtil.checkSupportQuestionare(
						                currentTask.getJobRequest().
						                getInspectType().
						                getInspectTypeID())){
	                                 subMenuItem.groupCmd = MenuGroupTypeCmd.SHOW_COMMENT;
	                                 menus.add(subMenuItem);                              
	                              }						         
						   }
						}else if (i == 3){
							if (!team.isQCTeam())
							{
								subMenuItem.groupCmd = MenuGroupTypeCmd.SHOW_REMARK;
								menus.add(subMenuItem);
							}
						}else if (i == 4){
							subMenuItem.groupCmd = MenuGroupTypeCmd.SHOW_SUMMARY_REPORT_INSPECT;
							menus.add(subMenuItem);
						}
					}
				}break;
			}
		}
		MenuListAdapter adapter = new MenuListAdapter(this.getActivity(),menus);
		this.setListAdapter(adapter);
	}
	private void populateLeftMenu(MenuGroupType menuGroupType)
	{
		ArrayList<MenuItem> menus = new ArrayList<MenuItem>();
		
		String[] menu_title_group_sorts = 
				this.getActivity().getResources().getStringArray(R.array.menu_group_sort_array);
		String[] sub_menu_title = null;
		for(int i = 0 ; i < menu_title_group_sorts.length;i++)
		{
			/*
			 * skip sort by time , if current content is calendar
			 */
			if (menuGroupType == MenuGroupType.DASH_BOARD)/*skip sort by time*/
			{
				if (i == 1)continue;
			}else if ((menuGroupType == MenuGroupType.DO_PLAN_JOB)||
					(menuGroupType == MenuGroupType.DO_TASK))
			{
				//Fragment f = this.getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frag);
				//if (f instanceof DashboardCalendarFragment)
				boolean isShowInList = SharedPreferenceUtil.checkJobAndTaskShownInListView(getActivity());
				if (!isShowInList)
				{
					if (i == 1)continue;
				}
			}
			/////////////////////////
			
			
			MenuItem item = new MenuItem();
			item.isTitleGroup = true;
			item.label = menu_title_group_sorts[i];
			if (i == 0)
			{
				if (menuGroupType == MenuGroupType.DO_PLAN_JOB){
					item.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_ALL;
					item.whereInStatus = WhereInStatus.DO_PLAN_TASK;
				}
				else if (menuGroupType == MenuGroupType.DO_TASK){
					item.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_DO_TASK_ALL;
					item.whereInStatus = WhereInStatus.TO_DO_TASK;
				}else if (menuGroupType == MenuGroupType.DASH_BOARD){					
					item.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_ALL;
					item.whereInStatus = WhereInStatus.NONE;					
				}
			}else if (i == 1){
				item.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_TIME_ALL;
				if (menuGroupType == MenuGroupType.DO_PLAN_JOB)
				{
					item.whereInStatus = WhereInStatus.DO_PLAN_TASK;
				}else if (menuGroupType == MenuGroupType.DO_TASK)
				{
					item.whereInStatus = WhereInStatus.TO_DO_TASK;
				}
			}else if (i == 2){
				item.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_INSPECT_ALL;
				PSBODataAdapter dataAdapter =  PSBODataAdapter.getDataAdapter(this.getActivity());
				try {
					item.allInspectType = dataAdapter.getAllInspectType();
					if (menuGroupType == MenuGroupType.DO_PLAN_JOB)
					{
						item.whereInStatus = WhereInStatus.DO_PLAN_TASK;
					}else if (menuGroupType == MenuGroupType.DO_TASK)
					{
						item.whereInStatus = WhereInStatus.TO_DO_TASK;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			menus.add(item);
			
			switch(i)
			{
				case 0:
				{
					if (menuGroupType == MenuGroupType.DO_TASK){
						sub_menu_title = this.getActivity().getResources().getStringArray(R.array.sub_menu_group_task_sort_by_task_status);
					}else if (menuGroupType == MenuGroupType.DO_PLAN_JOB) 
					{
						sub_menu_title = this.getActivity().getResources().getStringArray(R.array.sub_menu_group_task_sort_by_plan_job_status);						
					}else if (menuGroupType == MenuGroupType.DASH_BOARD)
					{
						sub_menu_title = this.getActivity().getResources().getStringArray(R.array.sub_menu_group_task_sort_by_dashboard);												
					}
					for(int j = 0; j < sub_menu_title.length;j++)
					{
						item = new MenuItem();
						item.label = sub_menu_title[j];
						if (menuGroupType == MenuGroupType.DO_PLAN_JOB)
							item.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_STATUS;
						else
							item.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_DO_TASK;
						
						item.countItem = "";	
						switch(j)
						{
							case 0:{
								if (menuGroupType == MenuGroupType.DO_PLAN_JOB){
									item.sortByTaskStatus = TaskStatus.WAIT_TO_CONFIRM;								
								}
								else if (menuGroupType == MenuGroupType.DO_TASK){
									item.sortByTaskStatus = TaskStatus.FINISH;
								}else if (menuGroupType == MenuGroupType.DASH_BOARD)
								{
									item.sortByTaskStatus = TaskStatus.WAIT_TO_CONFIRM;
									item.whereInStatus = WhereInStatus.DO_PLAN_TASK;
								}
							}break;
							case 1:{
								if (menuGroupType == MenuGroupType.DO_PLAN_JOB){									
									item.sortByTaskStatus = TaskStatus.CONFIRM_INSPECT;
								}
								else if (menuGroupType == MenuGroupType.DO_TASK)
								{
									item.sortByTaskStatus = TaskStatus.LOCAL_SAVED;
								}else if (menuGroupType == MenuGroupType.DASH_BOARD)
								{
									item.sortByTaskStatus = TaskStatus.CONFIRM_INSPECT;
									item.whereInStatus = WhereInStatus.DO_PLAN_TASK;
								}
							}break;
							case 2:{
								if (menuGroupType == MenuGroupType.DO_PLAN_JOB){
									item.sortByTaskStatus = TaskStatus.SHIFT;
								}
								else if (menuGroupType == MenuGroupType.DO_TASK)
								{
									item.sortByTaskStatus = TaskStatus.CONFIRMED_FROM_WEB;
									
								}else if (menuGroupType == MenuGroupType.DASH_BOARD)
								{
									item.sortByTaskStatus = TaskStatus.SHIFT;
									item.whereInStatus = WhereInStatus.DO_PLAN_TASK;
								}
							}break;
							case 3:{
								if (menuGroupType == MenuGroupType.DO_PLAN_JOB){
									item.sortByTaskStatus = TaskStatus.CANCEL;
									
								}else if (menuGroupType == MenuGroupType.DO_TASK)
								{
									item.sortByTaskStatus = TaskStatus.ALLOW_EDIT;									
								}else if (menuGroupType == MenuGroupType.DASH_BOARD)
								{
									item.sortByTaskStatus = TaskStatus.CANCEL;
									item.whereInStatus = WhereInStatus.DO_PLAN_TASK;
								}
							}break;
							case 4:{
								if (menuGroupType == MenuGroupType.DASH_BOARD){
									item.sortByTaskStatus = TaskStatus.FINISH;
									item.whereInStatus = WhereInStatus.TO_DO_TASK;
								}
							}break;
							case 5:{
								if (menuGroupType == MenuGroupType.DASH_BOARD){
									item.sortByTaskStatus = TaskStatus.LOCAL_SAVED;
									item.whereInStatus = WhereInStatus.TO_DO_TASK;
								}
							}break;
							case 6:{
								if (menuGroupType == MenuGroupType.DASH_BOARD){
									item.sortByTaskStatus = TaskStatus.CONFIRMED_FROM_WEB;
									item.whereInStatus = WhereInStatus.TO_DO_TASK;
								}
							}break;
							case 7:{
								if (menuGroupType == MenuGroupType.DASH_BOARD){
									item.sortByTaskStatus = TaskStatus.ALLOW_EDIT;
									item.whereInStatus = WhereInStatus.TO_DO_TASK;
								}
							}break;
						}

						if (menuGroupType == MenuGroupType.DO_PLAN_JOB)
						{						
							item.whereInStatus = WhereInStatus.DO_PLAN_TASK;
						}
						else if (menuGroupType == MenuGroupType.DO_TASK)
						{
							item.whereInStatus = WhereInStatus.TO_DO_TASK;
						}
						
						menus.add(item);
					}
					
				}break;
				case 1:
				{
					sub_menu_title = this.getActivity().getResources().getStringArray(R.array.sub_menu_group_sort_array_2);
					for(int j = 0; j < sub_menu_title.length;j++)
					{
						item = new MenuItem();
						item.label = sub_menu_title[j];
						item.countItem = "";//""+(int)Math.random();
						switch(j){
							case 0:{
								item.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_TIME_TODAY;
							}break;
							case 1:{
								item.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_TIME_NEXT_WEEK;
							}break;
							case 2:{
								item.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_CUSTOM_TIME;
							}break;
						}
						
						
						if (menuGroupType == MenuGroupType.DO_PLAN_JOB)
						{						
							item.whereInStatus = WhereInStatus.DO_PLAN_TASK;
						}
						else
						{
							item.whereInStatus = WhereInStatus.TO_DO_TASK;
						}

						menus.add(item);
					}
				}break;
				case 2:
				{
					
					PSBODataAdapter dataAdapter =  PSBODataAdapter.getDataAdapter(this.getActivity());
					try {
						ArrayList<InspectType> inspectTypes =  dataAdapter.getAllInspectType();
						if (inspectTypes != null)
						{
							for(InspectType inspect_item : inspectTypes)
							{
								item = new MenuItem();
								item.label = inspect_item.getInspectTypeName().trim();
								item.countItem = "";//""+(int)Math.random();
								item.currentInspectType = inspect_item;
								item.allInspectType = inspectTypes;
								if (menuGroupType == MenuGroupType.DO_PLAN_JOB)
								{
									item.whereInStatus = WhereInStatus.DO_PLAN_TASK;
								}else if (menuGroupType == MenuGroupType.DO_TASK)
								{
									item.whereInStatus = WhereInStatus.TO_DO_TASK;
								}
								item.groupCmd = CommonListMenuFragment.MenuGroupTypeCmd.SHOW_TASK_BY_INSPECT_ID;
								menus.add(item);							
							}
						}
						
					} catch(Exception ex)
					{
						ex.printStackTrace();
					}
					
				}break;
			}
		}
		/*
		 initial menu retrive from database 
		 summary
		 */
		
		MenuListAdapter adapter = new MenuListAdapter(this.getActivity(),menus);
		this.setListAdapter(adapter);
	}
	//private void pop
	private void populateReport(){
		String[] reportTypes = this.getResources().getStringArray(R.array.array_list_of_reports);
		ArrayList<MenuItem> menus = new ArrayList<MenuItem>();
		for(int i = 0; i < reportTypes.length;i++)
		{
			if (i == 0)
			{
				MenuItem item = new MenuItem();
				item.isTitleGroup = true;
				item.label = reportTypes[i];
				item.groupCmd = MenuGroupTypeCmd.SHOW_REPORT_ALL;
				menus.add(item);				
			}
		}
		MenuListAdapter adapter = new MenuListAdapter(this.getActivity(),menus);
		this.setListAdapter(adapter);
	}
	private void populateNewsMenu(){
		String[] newsGroupTypes = this.getResources().getStringArray(R.array.array_list_of_news);
		ArrayList<MenuItem> menus = new ArrayList<MenuItem>();

		for(int i = 0;i < newsGroupTypes.length;i++)
		{
			if (i == 0)
			{
				MenuItem item = new MenuItem();
				item.isTitleGroup = true;
//				item.label = this.getString(R.string.show_expense_all);//menu_title_group_sorts[i];
				item.label = newsGroupTypes[i];
				item.groupCmd = MenuGroupTypeCmd.SHOW_NEWS_ALL;
				menus.add(item);				
			}else 
			{
				MenuItem item = new MenuItem();
				item.label = newsGroupTypes[i];
				item.countItem = "";
				if (i == 1)
					item.groupCmd = MenuGroupTypeCmd.SHOW_NEWS_TODAY;
				else if (i == 2)
					item.groupCmd = MenuGroupTypeCmd.SHOW_NEWS_7_DAYS;
				else if (i == 3)
					item.groupCmd = MenuGroupTypeCmd.SHOW_NEWS_15_DAYS;
				
				menus.add(item);
			}
		}
		
		MenuListAdapter adapter = new MenuListAdapter(this.getActivity(),menus);
		this.setListAdapter(adapter);
		
	}
	private void populateExpenseTypeMenu(){
		ArrayList<MenuItem> menus = new ArrayList<MenuItem>();
		
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
		try {
			ArrayList<ExpenseType> expenseTypeList = dataAdapter.getAllExpenseType();
			
			MenuItem item = new MenuItem();
			item.isTitleGroup = true;
			item.label = this.getString(R.string.show_expense_all);//menu_title_group_sorts[i];
			item.groupCmd = MenuGroupTypeCmd.SHOW_EXPENSE_ALL;
			menus.add(item);
			
			for(ExpenseType type : expenseTypeList)
			{
				item = new MenuItem();
				item.label = type.getExpenseTypeName();
				item.countItem = "";
				item.currentExpenseType = type;
				item.groupCmd = MenuGroupTypeCmd.SHOW_EXPENSE_BY_ID;
				menus.add(item);
			}

			MenuListAdapter adapter = new MenuListAdapter(this.getActivity(),menus);
			this.setListAdapter(adapter);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void reloadListMenus(MenuGroupType menuGroupType)
	{
		this.menuGroupType = menuGroupType;
		if (menuGroupType == MenuGroupType.EXPENSE){
			populateExpenseTypeMenu();
		}else if (menuGroupType == MenuGroupType.DO_PLAN_JOB)
		{
			populateLeftMenu(MenuGroupType.DO_PLAN_JOB);
		}else if (menuGroupType == MenuGroupType.DO_TASK){
			populateLeftMenu(MenuGroupType.DO_TASK);			
		}else if (menuGroupType == MenuGroupType.DASH_BOARD)
		{
			populateLeftMenu(MenuGroupType.DASH_BOARD);			
		}else if (menuGroupType == MenuGroupType.NEWS)
		{
			populateNewsMenu();
		}else if (menuGroupType == MenuGroupType.REPORT)
		{
			populateReport();
		}
		((MenuListAdapter)this.getListView().getAdapter()).notifyDataSetChanged();
	}
    // Because the fragment doesn't have a reliable callback to notify us when
    // the activity's layout is completely drawn, this OnGlobalLayoutListener provides
    // the necessary callback so we can add top-margin to the ListView in order to dodge
    // the ActionBar. Which is necessary because the ActionBar is in overlay mode, meaning
    // that it will ordinarily sit on top of the activity layout as a top layer and
    // the ActionBar height can vary. Specifically, when on a small/normal size screen,
    // the action bar tabs appear in a second row, making the action bar twice as tall.
    ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int barHeight = getActivity().getActionBar().getHeight();
            ListView listView = getListView();
            FrameLayout.LayoutParams params = (LayoutParams) listView.getLayoutParams();
            // The list view top-margin should always match the action bar height
            if (params.topMargin != barHeight) {
                params.topMargin = barHeight;
                //params.topMargin += 10;//padding
                listView.setLayoutParams(params);
            }
            // The action bar doesn't update its height when hidden, so make top-margin zero
            if (!getActivity().getActionBar().isShowing()) {
              params.topMargin = 0;
              listView.setLayoutParams(params);
            }
        }
    };
    
    
    private void clearPreviusFragmentStacks(){
		if (this.getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
		{
			int iCountStack = this.getActivity().getSupportFragmentManager().getBackStackEntryCount();
			for(int i = 0; i < iCountStack;i++)
					this.getActivity().getSupportFragmentManager().popBackStack();
		}
    }
}
